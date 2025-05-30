# Redis Docker 환경설정 가이드

## 1. 사전 준비사항
- Docker Desktop 설치 필요
- 터미널(Linux/Mac) 또는 PowerShell(Windows) 접근 가능
- https://www.docker.com/products/docker-desktop/

## 2. Docker를 이용한 Redis 설치

### 2.1 기본 Redis 컨테이너 실행
```bash
# Redis 이미지 다운로드
docker pull redis:latest

# Redis 컨테이너 실행
docker run --name my-redis -d -p 6379:6379 redis:latest

# 컨테이너 실행 상태 확인
docker ps
```

### 2.2 Docker Compose 설정
1. 프로젝트 디렉토리 생성 및 이동
```bash
mkdir redis-practice
cd redis-practice
```

2. docker-compose.yml 파일 생성
```bash
touch docker-compose.yml
```

3. docker-compose.yml 내용 작성
```yaml
version: '3.8'
services:
  redis:
    image: redis:latest
    container_name: redis-practice
    ports:
      - "6379:6379"
    volumes:
      - ./redis-config:/usr/local/etc/redis
    command: redis-server /usr/local/etc/redis/redis.conf
```

### 2.3 Redis 설정 파일 구성
1. redis.conf 파일 생성
```bash
mkdir redis-config
cd redis-config
touch redis.conf
```

2. redis.conf 기본 설정
```conf
# 메모리 설정
maxmemory 256mb
maxmemory-policy allkeys-lru

# 영속성 설정
appendonly yes
appendfilename "appendonly.aof"

# 네트워크 설정
bind 0.0.0.0
protected-mode yes
port 6379

# 로깅 설정
loglevel notice
logfile ""
```

## 3. Redis 실행 및 확인

### 3.1 Docker Compose로 Redis 실행
```bash
cd ..  # redis-practice 디렉토리로 이동
docker-compose up -d
```

### 3.2 Redis 상태 확인
```bash
# 컨테이너 상태 확인
docker ps

# Redis 로그 확인
docker logs redis-practice

# Redis CLI 접속 테스트
docker exec -it redis-practice redis-cli
```

### 3.3 기본 동작 테스트
Redis CLI에 접속한 후:
```bash
# PING 테스트
PING

# 간단한 키-값 저장 테스트
SET test "Hello Redis"
GET test
```

## 4. 문제 해결

### 4.1 일반적인 문제 및 해결방법
1. 포트 충돌
```bash
# 포트 사용 확인 (Linux/Mac)
lsof -i :6379
# Windows
netstat -ano | findstr :6379

# 다른 포트로 실행하기
# docker-compose.yml 수정
ports:
  - "6380:6379"  # 호스트의 6380포트를 컨테이너의 6379포트에 매핑
```

2. 권한 문제
```bash
# 볼륨 디렉토리 권한 설정
chmod 777 redis-config
```

### 4.2 컨테이너 관리 명령어
```bash
# 컨테이너 중지
docker-compose stop

# 컨테이너 시작
docker-compose start

# 컨테이너 재시작
docker-compose restart

# 컨테이너 삭제 (데이터 삭제 주의)
docker-compose down
```