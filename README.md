## 프로젝트 명 : Delivery Hanghae

## 목차
1. [팀원](#팀원)
2. [요구사항](#요구사항)
3. [Flow Chart](#Flow-Chart)
4. [ERD](#ERD)
5. [API 설계](#API-설계)
6. [프로젝트 실행 방법](#프로젝트-실행-방법)
7. [프로그램 실행 예시](#프로그램-실행-예시)
8. [추가하고 싶은 기능](#추가하고-싶은-기능)

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

## ERD

## API 설계
http://49.50.161.142:8080/swagger-ui/index.html

## 프로젝트 실행 방법
1. 해당 프로젝트를 zip파일로 다운로드한다.
2. zip파일 압축을 풀고 사용하는 가상환경에 파일을 등록한다.
3. DHH\src\main\java\study\deliveryhanghae\global\config 에 AppConfig 클래스 파일을 생성한다.
   * iamport 가입 후 결제 연동 -> 가맹점 식별 코드 확인
```java
import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    String apiKey = "REST API Key 입력";
    String secretKey="REST API Secret 입력";

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, secretKey);
    }
}
```
4. DHH\src\main\resources 에 application.properties 파일을 추가한다.
```java
//데이터 베이스 연동 정보
spring.datasource.url=jdbc:mysql://localhost:3306/DB명
spring.datasource.username=
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update

spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true

//회원가입 메일 정보
spring.mail.host=smtp.gmail.com
spring.mail.port=
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.starttls.enable=true

spring.main.allow-bean-definition-overriding=true

//파일 등록 정보
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=20MB

//레디스 정보
spring.data.redis.host=localhost
spring.data.redis.port=6379

//JWT 정보
spring.jwt.token.access-expiration-time=3600000
spring.jwt.token.refresh-expiration-time=259200000
jwt.secret.key=

//AWS 정보
cloud.aws.region.static=ap-northeast-2
cloud.aws.stack.auto-=false
cloud.aws.credentials.accessKey=
cloud.aws.credentials.secretKey=
cloud.aws.s3.bucket=dhh-service-bucket

```
5. charge.html 파일 imp번호(가맹점 식별코드) 변경
![image](https://github.com/miiiingi/DHH/assets/77494780/18a8416f-ac56-40a4-87fe-a67c90ee8c58)

## 프로그램 실행 예시

## 추가하고 싶은 기능 
