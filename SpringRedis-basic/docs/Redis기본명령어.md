# Redis 기본 명령어 가이드

## 1. Redis CLI 접속
```bash
# Docker 컨테이너의 Redis CLI 접속
docker exec -it redis-practice redis-cli
```

## 2. 데이터 타입별 기본 명령어

### 2.1 String 타입
```bash
# 기본 문자열 저장 및 조회
SET user:1 "John Doe"
GET user:1

# 만료시간 설정
SET temp:1 "temporary" EX 60  # 60초 후 만료
TTL temp:1  # 남은 시간 확인

# 숫자형 문자열 증감
SET counter 1
INCR counter
INCRBY counter 5
DECR counter
DECRBY counter 2

# 여러 키-값 동시 처리
MSET key1 "value1" key2 "value2" key3 "value3"
MGET key1 key2 key3
```

### 2.2 List 타입
```bash
# 리스트 조작
LPUSH mylist "first"
RPUSH mylist "last"
LRANGE mylist 0 -1  # 전체 리스트 조회

# 리스트 요소 제거
LPOP mylist
RPOP mylist

# 리스트 길이
LLEN mylist

# 특정 인덱스 요소 조회
LINDEX mylist 0

# 리스트 요소 수정
LSET mylist 0 "modified"
```

### 2.3 Set 타입
```bash
# Set 요소 추가
SADD myset "element1"
SADD myset "element2" "element3"

# Set 요소 조회
SMEMBERS myset

# 요소 존재 여부 확인
SISMEMBER myset "element1"

# 요소 개수 확인
SCARD myset

# 요소 제거
SREM myset "element1"

# Set 연산
SADD set1 "a" "b" "c"
SADD set2 "c" "d" "e"
SUNION set1 set2    # 합집합
SINTER set1 set2    # 교집합
SDIFF set1 set2     # 차집합
```

### 2.4 Hash 타입
```bash
# Hash 데이터 저장
HSET user:100 name "Alice" age "20" email "alice@example.com"

# 특정 필드 조회
HGET user:100 name

# 모든 필드-값 조회
HGETALL user:100

# 특정 필드들의 값만 조회
HMGET user:100 name age

# 필드 존재 여부 확인
HEXISTS user:100 email

# 필드 삭제
HDEL user:100 email

# 모든 필드 조회
HKEYS user:100

# 모든 값 조회
HVALS user:100
```

### 2.5 Sorted Set 타입
```bash
# 점수와 함께 요소 추가
ZADD leaderboard 100 "player1"
ZADD leaderboard 200 "player2"
ZADD leaderboard 150 "player3"

# 점수 범위로 요소 조회
ZRANGE leaderboard 0 -1 WITHSCORES

# 역순 조회
ZREVRANGE leaderboard 0 -1 WITHSCORES

# 점수 범위로 요소 조회
ZRANGEBYSCORE leaderboard 100 200

# 특정 요소의 순위 조회
ZRANK leaderboard "player1"
```

## 3. 키 관리 명령어

### 3.1 키 검색 및 정보
```bash
# 패턴으로 키 검색
KEYS user:*

# 키 존재 여부 확인
EXISTS user:1

# 키 타입 확인
TYPE user:1

# 키 만료시간 설정
EXPIRE user:1 3600  # 1시간 후 만료
```

### 3.2 데이터베이스 관리
```bash
# 현재 DB의 키 개수 확인
DBSIZE

# 모든 키 삭제
FLUSHDB  # 현재 DB만
FLUSHALL # 모든 DB

# DB 선택 (0-15)
SELECT 1
```

## 4. 실습 예제

### 4.1 사용자 세션 관리
```bash
# 사용자 세션 생성
SET session:user123 "logged_in" EX 3600

# 세션 확인
GET session:user123
TTL session:user123
```

### 4.2 간단한 방문자 카운터
```bash
# 방문자 수 증가
INCR visitors

# 특정 페이지별 방문자 수
HINCRBY page:visits "/home" 1
HINCRBY page:visits "/about" 1

# 조회
HGETALL page:visits
```

### 4.3 실시간 랭킹 시스템
```bash
# 점수 등록
ZADD rankings 100 "user1"
ZADD rankings 200 "user2"
ZADD rankings 150 "user3"

# 상위 3명 조회
ZREVRANGE rankings 0 2 WITHSCORES
```

## 5. 주의사항 및 팁
1. 키 네이밍 컨벤션
    - 관련 데이터는 콜론(:)으로 구분
    - 예: user:1000:profile

2. 데이터 만료 관리
    - 임시 데이터는 항상 EXPIRE 설정
    - TTL로 주기적으로 확인

3. 메모리 관리
    - INFO memory로 사용량 모니터링
    - 큰 데이터는 분할하여 저장