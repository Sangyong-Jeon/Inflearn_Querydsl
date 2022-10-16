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

# 목차
- [QueryDsl 설치법](https://github.com/Sangyong-Jeon/Inflearn_Querydsl/wiki/QueryDsl-설치법)
- [JPQL vs Querydsl](https://github.com/Sangyong-Jeon/Inflearn_Querydsl/wiki/JPQL-vs-QueryDsl)
