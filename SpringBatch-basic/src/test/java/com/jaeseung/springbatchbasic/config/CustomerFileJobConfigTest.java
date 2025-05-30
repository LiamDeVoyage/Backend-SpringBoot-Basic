package com.jaeseung.springbatchbasic.config;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBatchTest
@SpringBootTest
class CustomerFileJobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobExplorer jobExplorer;

//    @TestConfiguration
//    static class TestJobConfig {
//        @Bean
//        public JobLauncherTestUtils jobLauncherTestUtils() {
//            return new JobLauncherTestUtils();
//        }
//    }

    @Autowired
    @Qualifier("customerFileJob")
    private Job customerFileJob;

    @Test
    void customerFileJobTest() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
                .addString("datetime", LocalDateTime.now().toString())
                .toJobParameters();
        jobLauncherTestUtils.setJob(customerFileJob);

        // when
        JobExecution jobExecution =
                jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }
}
