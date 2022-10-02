# 실전! Querydsl
[인프런 강의](https://www.inflearn.com/course/Querydsl-실전)<br>
강사 : [김영한](https://www.inflearn.com/users/@yh)

<br>

QueryDsl의 기초부터 실무 활용까지 학습할 수 있는 강의이다.
- JPA를 사용할 때 동적 쿼리와 복잡한 쿼리 문제 해결 가능
- 쿼리를 문자가 아닌 자바 코드로 작성 가능
- 문법 오류를 컴파일 시점에서 발견
- JPQL new 명령어와는 비교가 안될 정도로 깔끔한 DTO 조회 지원

<br>

### 예시
```java
public class QuerydslDemoTest {
    @Autowired JPAQueryFactory queryFactory;
    @Autowired EntityManager em;

    // 문법 오류가 있지만 컴파일 단계에서 확인할 수 없음
    // 실행을 해봐야 알기 때문에 매우 위험함
    @Test
    public void jpql() {
        String username = "kim";
        String query = "select m from Member m" +
                        "where m.username = :username";
        List<Member> result = em.createQuery(query, Member.class)
                .setParameter("username", username)
                .getResultList();
    }
    
    // 모든 오류를 컴파일 시점에서 잡아줌
    // 코드 자동완성의 도움도 받을 수 있음
    // 동적쿼리의 문제도 편하게 작성 가능
    // 일부분을 메소드 추출하여 다른 곳에서도 공통으로 사용 가능
    @Test
    public void querydsl() {
        String username = "kim";
        List<Member> result = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq(username))
                .fetch();
    }
}
```

<br>

### 내가 사용한 컴퓨터 환경
  - Spring Boot 2.7.4
  - Java 8
  - IntelliJ IDEA 2022.2.2 (Ultimate Edition)
  - macOS 12.6

<br>

# QueryDsl 설치법
1. `build.gradle` 에 다음과 같이 querydsl를 추가하기 위한 설정 코드를 넣는다.

```gradle
// querydsl version 정보 추가
buildscript {
    ext {
        queryDslVersion = "5.0.0"
    }
}

plugins {
    id 'org.springframework.boot' version '2.7.4'
    id 'io.spring.dependency-management' version '1.0.14.RELEASE'
    //querydsl plugins 추가
    id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"
    id 'java'
}

group = 'study'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '1.8'

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    //querydsl dependencies 추가
    implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"
    annotationProcessor "com.querydsl:querydsl-apt:${queryDslVersion}"

    compileOnly 'org.projectlombok:lombok'
    runtimeOnly 'com.h2database:h2'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

tasks.named('test') {
    useJUnitPlatform()
}

//== querydsl 설정 추가 ==//

// querydsl에서 사용할 경로
def querydslDir = "$buildDir/generated/querydsl"

// JPA 사용 여부와 사용할 경로 지정
querydsl {
    jpa = true
    querydslSourcesDir = querydslDir
}

// build 시 사용할 sourceSet 추가
sourceSets {
    main.java.srcDir querydslDir
}

// querydsl이 compileClassPath를 상속하도록 설정
configurations {
    querydsl.extendsFrom compileClasspath
}

// querydsl 컴파일시 사용할 옵션 설정
compileQuerydsl {
    options.annotationProcessorPath = configurations.querydsl
}

//== querydsl 설정 끝 ==//
```

2. 우측에 Gradle을 클릭해서 리로드하거나 `build.gradle` 파일 우측 상단에 비슷하게 생긴 작은 버튼을 눌려 라이브러리를 설치한다.
<img width="465" alt="image" src="https://user-images.githubusercontent.com/80039556/193440316-7c2b80f4-4c95-430d-9234-8ab23eccd58c.png">

3. 다시 우측의 Gradle을 선택한 후 Tasks -> other 폴더안에 `compileQuerydsl` 을 클릭하여 컴파일을 실행한다.
<img width="1258" alt="image" src="https://user-images.githubusercontent.com/80039556/193440341-fedbbdea-3802-4fa8-be89-fe2c83aa480c.png">

4. 내 프로젝트 폴더 안에 build -> generated 폴더안에 Q 클래스가 생성됐는지 확인한다.
<img width="342" alt="image" src="https://user-images.githubusercontent.com/80039556/193440374-c6a821cc-dafc-4f13-8ac7-256789455710.png">

> 이 때 주의할 점은 src 폴더안의 generated 폴더가 아니라, build 폴더안에 generated 폴더안이라는 것을 명심하자.

5. 만약 build 폴더가 안보인다면 프로젝트를 보여주는 공간의 설정을 눌려서 제외된 파일 표시를 클릭하자.
<img width="651" alt="image" src="https://user-images.githubusercontent.com/80039556/193440419-7c8f3b40-f4f0-4772-a21b-d9728a12b4de.png">
