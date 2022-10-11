package study.querydsl.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // protected 기본생성자 생성
@ToString(of = {"id", "name"})
public class Team {

    @Id @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team") // 연관관계 주인 설정 (반대쪽이 주인이라 외래키값 업데이트 안함)
    List<Member> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}