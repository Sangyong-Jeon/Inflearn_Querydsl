package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory; // 동시성 문제는 설계단계에서 해결되서 이렇게 필드로 빼서 사용해도 됨

    @BeforeEach // 테스트 전에 실행하는 것
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void startJPQL() {
        // member1 찾기
        String qlString = "select m from Member m " +
                "where m.username = :username";

        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1") // 파라미터 바인딩
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl() {
//        QMember m = new QMember("m"); // 매개변수에 별칭을 넣는건데 이거 안쓸거라 중요하지 않음. 추후 스태틱 객체를 쓸 것임.

        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1")) // 파라미터 바인딩을 자동처리함. 문자열 더하기였으면 sql인젝션 공격 받을 수 있는데 queryDsl은 자동으로 preparedstatement 파라미터 바인딩처럼 사용함. DB 성능상에서 더 좋음
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
}
