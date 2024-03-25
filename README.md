## 프로젝트 명 : Delivery Hanghae

### User : http://49.50.161.142:8080/v1
### Owner : http://49.50.161.142:8080/v2/login-page

### 노션 : https://www.notion.so/ramizzang/b13c7d2c88274489b6bfa877cd6d1d65
### 그라운드 룰 : https://www.notion.so/ramizzang/eb1b5203b0e84217beb43f1665d4e807
### Git 전략 : https://www.notion.so/ramizzang/Git-5d5d7e547bf941c0b2d2cc17930843eb

## 목차
1. [팀원](#팀원)
2. [요구사항](#요구사항)
3. [Flow Chart](#Flow-Chart)
4. [ERD](#ERD)
5. [API 설계](#API-설계)
6. [기술 Map](#기술-Map)
7. [프로젝트 실행 방법](#프로젝트-실행-방법)
8. [프로그램 실행 예시](#프로그램-실행-예시)
9. [추가하고 싶은 기능](#추가하고-싶은-기능)

## 팀원
* 김민기 https://github.com/miiiingi/DHH
* 민가람 https://github.com/ramizzang
* 오진선 https://github.com/Rosa-Eau
* 송유하 https://github.com/uha9218

## 요구사항
1. 사장님 기능
   * 사장님 계정으로 로그인, 회원가입 할 수 있다. -> 메일 인증
   * 사장님은 업장 정보를 등록, 수정, 삭제할 수 있다. -> 실제 사업자 번호인지 검사
   * 사장님은 업장을 한개만 소유하고 있다.
   * 사장님은 업자의 메뉴 정보를 등록, 수정, 삭제할 수 있다.
   * 사장님은 고객의 주문 정보를 확인 할 수 있다.
   * 사장님은 고객 주문을 배달완료 처리할 수 있다.
   * 배달완료 처리된 주문의 가격만큼 사장님의 포인트가 증가한다.

2. 고객 기능
   * 고객 계정으로 로그인, 회원가입 할 수 있다. -> 메일 인증
   * 고객은 가입 시 딜리버리항해에서 사용되는 1만 포인트가 증정된다
   * 고객은 전 업장 목록을 볼 수 있다.
   * 고객은 업장을 선택하여 해당 업장의 메뉴 목록을 볼 수 있다.
   * 고객은 키워드로 업장을 검색할 수 있다.
   * 고객은 메뉴 하나를 선택하여 해당 메뉴를 주문할 수 있다.
   * 주문시 가격은 포인트에서 차감되며 포인트 필요시 원하는 만큼 결제하여 충전할 수 있다.
   * 주문시 잔여 포인트보다 메뉴 가격이 비싸면 포인트 충전 페이지로 이동한다.

## Flow Chart
![image](https://github.com/miiiingi/DHH/assets/77494780/ffddf763-d137-45d7-9995-27190928b10b)
![image](https://github.com/miiiingi/DHH/assets/77494780/87a9b16c-51c2-4295-8f79-bfcf47fa4ef5)

## ERD
![image](https://github.com/miiiingi/DHH/assets/77494780/df297f38-f50a-40ce-8d02-6169e9933dab)

## API 설계
http://49.50.161.142:8080/swagger-ui/index.html

## 기술 Map
![image](https://github.com/miiiingi/DHH/assets/77494780/705feba8-6c57-42e7-99ae-923b0f89351d)

## 프로젝트 실행 방법
1. 해당 프로젝트를 zip파일로 다운로드한다.
2. zip파일 압축을 풀고 사용하는 가상환경에 파일을 등록한다.
3. DHH\src\main\resources 에 application.properties 파일을 추가한다.
```java
//데이터 베이스 연동 정보
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
//redis, security
jwt.secret.key=
spring.data.redis.host=redis
spring.data.redis.port=6379
spring.jwt.token.access-expiration-time=3600000
spring.jwt.token.refresh-expiration-time=259200000
//메일인증
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.starttls.enable=true
//s3
cloud.aws.region.static=ap-northeast-2
cloud.aws.stack.auto-=false
cloud.aws.credentials.accessKey=
cloud.aws.credentials.secretKey=
cloud.aws.s3.bucket=dhh-service-bucket
//프로메테우스-그라파냐
management.endpoints.web.exposure.include=*
management.endpoint.prometheus.enabled=true
//스웨거
springdoc.swagger-ui.path=/api-test
springdoc.swagger-ui.groups-order=DESC
springdoc.swagger-ui.tag-sorter=alpha
springdoc.show-login-endpoint=true
//아임 포트 결제
iamport.apiKey=
iamport.secretKey=
iamport.impKey=
```


## 프로그램 실행 예시 : https://youtu.be/ZRGbnZgXghE?si=K7UaNbnMbiT0zG7r 
* 사장님
  1. 회원가입 
![image](https://github.com/miiiingi/DHH/assets/77494780/7c0e5393-24bd-4cf9-8907-3640877ff010)
      * 회원가입은 메일 인증이 필수입니다. 인증 번호를 확인하여 일치해야 가입이 가능합니다.
      * 가입을 위해서는 패스워드와 패스워드 확인이 일치해야 합니다.
      * 사장님과 유저 이메일이 일치할 경우 가입이 불가능합니다.

   2. 로그인
![image](https://github.com/miiiingi/DHH/assets/77494780/07a0ca75-4b64-4461-8051-e74231f55671)
      * 사장님과 유저의 테이블이 분리되어 있으며, 각각 별도로 로그인이 이루어집니다.
      * 로그인은 이메일과 비밀번호 확인 후에 일치해야 합니다.

  3. 가게 등록, 수정
![image](https://github.com/miiiingi/DHH/assets/77494780/16cd8fd0-dfc9-4bef-aaf7-040ea0716d0d)
      * 가게 등록 시에는 실제 사업자 번호가 확인되어야 합니다.
      * 이미지 등록 전에는 확장자 검사가 필요합니다.
      * 모든 입력 칸이 채워져 있어야 가게 등록이 가능합니다.

  4. 메뉴 등록, 수정, 삭제
![image](https://github.com/miiiingi/DHH/assets/77494780/c50efb31-75f9-4675-a547-d5fdf4cd3f0f)
      * 메뉴 등록 시에도 이미지 등록 전에는 확장자 검사가 필요합니다.
      * 모든 입력 칸이 채워져 있어야 메뉴 등록이 가능합니다.
 
  5. 주문 내역 확인
![image](https://github.com/miiiingi/DHH/assets/77494780/d00a1762-0d8f-4d5f-9020-c97eb5b24ff4)
      * 사장님은 유저의 주문 내역을 확인할 수 있습니다.
      * 주문이 배달 완료된 후에는 배달 완료 버튼을 클릭하여 메뉴 금액을 포인트로 받을 수 있습니다.

* 유저
   1. 회원가입
      * 사장님과 동일한 절차를 따릅니다.
      
   2. 로그인
      * 사장님과 동일한 방식으로 로그인합니다.
      
   3. 메인
![image](https://github.com/miiiingi/DHH/assets/77494780/ff7e3120-a687-4111-bf2e-21a80f44ffd5)
      * 키워드로 검색하여 가게를 찾을 수 있습니다.
      * 가게를 선택하여 상세 정보를 확인할 수 있습니다.

   4. 가게 상세 조회
![image](https://github.com/miiiingi/DHH/assets/77494780/055565ae-7211-4b67-9f0e-d62933c8821a)
      * 가게 정보와 메뉴를 확인할 수 있습니다.
      * 주문하기 버튼을 클릭하여 주문할 수 있습니다.
 
   5. 결제
![image](https://github.com/miiiingi/DHH/assets/77494780/13426ff2-64eb-4f40-8b16-72467193d931)
      * 주문 가격, 보유 포인트, 잔여 포인트 정보를 조회할 수 있습니다. 잔액이 부족한 경우 '잔고가 부족합니다' 메시지가 표시됩니다.
      * 잔액 부족 시 결제 버튼을 클릭하면 포인트 충전 페이지로 이동하며, 충전 가능 시 결제가 완료됩니다.

   6. 충전
![image](https://github.com/miiiingi/DHH/assets/77494780/481b028e-ce07-476f-bdb3-e009351b7bd0)
      * 충전할 금액을 입력하고 충전하기 버튼을 클릭하면 결제 창이 표시됩니다.
      * 충전이 완료되면 결제 완료 팝업이 표시되며, 메인 화면으로 돌아갑니다. 충전 중 오류가 발생한 경우 결제 실패 팝업이 표시됩니다.

## 추가하고 싶은 기능 
1. 로그아웃 refresh token 삭제
2. redis cashing
3. 무중단 배포, nginx, load balancing
4. 테스트 코드 작성, git action
5. JMeter 시큐리티 연동 
