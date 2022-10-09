package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Hello;
import study.querydsl.entity.QHello;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional // 기본적으로 롤백시킴
class QuerydslApplicationTests {

    // 스프링 최신버전에서는 @Autowired를 사용하고, 자바 표준 스택에서는 @PersistenceContext 사용하면 된다. (상관없음)
    @Autowired
    EntityManager em;

    @Test
    void contextLoads() {
        Hello hello = new Hello();
        em.persist(hello);

        // querydsl 최신버전에서는 JPAQueryFactory 사용 권장
        // 과거에는 from()부터 시작하여 select()를 사용했지만, JPAQueryFactory를 사용하면 select()부터 시작하기에 더 명확하게 사용 가능
        JPAQueryFactory query = new JPAQueryFactory(em);
        QHello qHello = new QHello("h"); // 이렇게 생성해도 되지만 Q클래스에는 기본적으로 static final 인스턴스를 가지고 있다.
        QHello qHello1 = QHello.hello; // 따라서 위의 생성방식보단 이걸 사용하자.

        // 쿼리와 관련된 것은 Q 타입을 사용
        Hello result = query
                .selectFrom(qHello1)
                .fetchOne();

        assertThat(result).isEqualTo(hello);
        assertThat(result.getId()).isEqualTo(hello.getId());
    }
}
