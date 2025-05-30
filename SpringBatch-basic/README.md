# 목차

- [1. 프로젝트 요약](#1프로젝트-요약)
- [2. 코드 요약](#2코드-요약)

---
# 1.프로젝트 요약

### **1. Job Configuration: Batch 작업의 구성 단계**

- **Configuration → Job Builder → Step Builder**:
    - **Configuration**: `@Configuration`으로 설정 클래스를 정의합니다(`HelloJobConfig`, `CustomerFileJobConfig`).
    - **Job Builder**: `JobBuilder`를 사용해 Job을 구성하며, Step을 순차적으로 연결합니다(`helloJob`, `customerFileJob`).
    - **Step Builder**: `StepBuilder`로 Step을 정의하며, Tasklet 또는 Chunk 기반 처리를 설정합니다(`helloStep`, `customerFileStep`).

### **2. Execution Flow: Job이 실행되는 흐름**

- **Job Launcher → Job Execution → Step Execution → Chunk Processing**:
    - **Job Launcher**: `JobLauncherController`에서 `jobLauncher.run()`으로 Job을 시작합니다.
    - **Job Execution**: Job이 실행되며 `JobExecution` 객체가 생성되어 전체 실행 상태를 관리합니다.
    - **Step Execution**: 각 Step이 실행되며 `StepExecution`이 개별 Step 상태를 관리합니다.
    - **Chunk Processing**: `customerFileStep`에서 Chunk 단위로 데이터를 읽고, 처리하고, 쓰는 과정이 진행됩니다.

### **3. Data Processing**

- **Read → Process → Write → Transaction Commit**:
    - **Read**: `customerFileReader()`가 CSV 파일에서 고객 데이터를 읽습니다.
    - **Process**: `customerProcessor()`가 각 고객 데이터에 등록 날짜를 추가합니다.
    - **Write**: `customerWriter()`가 처리된 데이터를 로깅합니다.
    - **Transaction Commit**: `transactionManager`가 Chunk 단위로 트랜잭션을 커밋하여 데이터 무결성을 보장합니다.

---
# 2.코드 요약

### 1. **Job Launcher (Job을 실행시킴)**

- **설명**: Job을 실행하는 데 사용되는 클래스이며, 외부 이벤트나 스케줄러에 의해 트리거될 수 있습니다.
- **코드에서의 구현**:
    - 코드에서 `JobLauncherController`라는 클래스가 `JobLauncher`를 사용하여 Job을 실행합니다. 이 컨트롤러는 HTTP 요청(예: REST API 호출)을 통해 Job을 시작하는 엔드포인트를 제공합니다.
    - 예를 들어, `helloJob`과 `customerFileJob`은 현재 날짜와 시간을 기반으로 고유한 `JobParameters`를 생성한 후 `jobLauncher.run()` 메서드로 실행됩니다.

    ```java
    JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
            .addString("datetime", LocalDateTime.now().toString())
            .toJobParameters();
    JobExecution jobExecution = jobLauncher.run(helloJob, jobParameters);
    
    ```

### 2. **Job (배치 처리의 기본 단위)**

- **설명**: 배치 처리의 최상위 단위로, 여러 Step을 묶어 하나의 작업을 구성하며, 실행 상태는 `JobRepository`에 저장됩니다.
- **코드에서의 구현**:
    - **helloJob**: `HelloJobConfig` 클래스에서 정의된 Job으로, 두 개의 Step(`helloStep`, `helloStep2`)을 포함합니다. 이 Job은 단순히 로그 메시지를 출력합니다.

        ```java
        @Bean
        public Job helloJob() {
            return new JobBuilder("helloJob", jobRepository)
                    .incrementer(new RunIdIncrementer())
                    .start(helloStep())
                    .next(helloStep2())
                    .build();
        }
        
        ```

    - **customerFileJob**: `CustomerFileJobConfig` 클래스에서 정의된 Job으로, CSV 파일에서 데이터를 읽고 처리하는 단일 Step(`customerFileStep`)을 포함합니다. 이 Job은 더 복잡한 데이터 처리와 멀티스레딩을 보여줍니다.
    - 두 Job 모두 `JobRepository`에 의해 상태(성공, 실패 등)가 관리되며, `RunIdIncrementer`를 통해 각 실행마다 고유한 ID를 부여받아 재실행 가능성을 지원합니다.

### 3. **Step (Job 내부의 세부 작업 단위)**

- **설명**: Job 내의 개별 작업 단위로, Chunk 기반 처리(대량 데이터 처리)와 Tasklet 기반 처리(단일 작업 처리) 두 가지 방식이 있습니다.
- **코드에서의 구현**:
    - **Tasklet 기반 Step**: `HelloJobConfig`의 `helloStep`과 `helloStep2`는 Tasklet을 사용하여 간단한 작업(로그 출력)을 수행합니다.

        ```java
        @Bean
        public Step helloStep() {
            return new StepBuilder("helloStep", jobRepository)
                    .tasklet((contribution, chunkContext) -> {
                        log.info("Hello Spring Batch!");
                        return RepeatStatus.FINISHED;
                    }, transactionManager)
                    .build();
        }
        
        ```

    - **Chunk 기반 Step**: `CustomerFileJobConfig`의 `customerFileStep`은 Chunk 기반 처리를 사용합니다. 이 Step은 CSV 파일에서 데이터를 읽고(`reader`), 처리(`processor`), 출력(`writer`)하는 과정을 Chunk 단위(10개 항목)로 수행합니다.

        ```java
        @Bean
        public Step customerFileStep() {
            return new StepBuilder("customerFileStep", jobRepository)
                    .<Customer, Customer>chunk(10, transactionManager)
                    .reader(customerFileReader())
                    .processor(customerProcessor())
                    .writer(customerWriter())
                    .taskExecutor(taskExecutor)  // 멀티스레드 설정
                    .build();
        }
        
        ```

        - 이 Step은 대량 데이터를 효율적으로 처리하며, 멀티스레딩(`taskExecutor`)을 통해 성능을 최적화합니다.

### 4. **Job Repository (Job, Step의 실행 상태와 메타데이터 관리)**

- **설명**: Job과 Step의 실행 상태(성공/실패 여부, 오류 발생 시점 등)를 기록하며, Job이 중단된 후 재실행 시 이전 상태를 복구합니다.
- **코드에서의 구현**:
    - 코드에서 `JobRepository`는 직접 보이지 않지만, Spring Batch가 자동으로 제공합니다. `JobBuilder`와 `StepBuilder`에 `jobRepository`가 주입되어 사용되며, Job과 Step의 메타데이터를 데이터베이스에 저장합니다.
    - 예를 들어, `application.yml`에서 데이터베이스 설정을 통해 `JobRepository`가 동작하며, Job 실행 상태를 관리합니다.
    - 재실행 가능성은 `RunIdIncrementer`와 결합되어 보장됩니다.

### 5. **Job Configuration (스프링의 DI를 통해 설정)**

- **설명**: Job과 Step의 구성 요소를 XML 또는 Java 기반으로 정의하며, 스프링의 의존성 주입(DI)을 활용합니다.
- **코드에서의 구현**:
    - `HelloJobConfig`와 `CustomerFileJobConfig`는 Java 기반 설정을 사용합니다. `@Configuration`과 `@Bean` 어노테이션을 통해 Job, Step, Reader, Processor, Writer 등을 정의합니다.

        ```java
        @Configuration
        public class HelloJobConfig {
            @Bean
            public Job helloJob() {
                return new JobBuilder("helloJob", jobRepository)
                        .incrementer(new RunIdIncrementer())
                        .start(helloStep())
                        .next(helloStep2())
                        .build();
            }
        }
        
        ```

    - 스프링의 DI를 통해 `jobRepository`, `transactionManager`, `taskExecutor` 등이 주입됩니다.
