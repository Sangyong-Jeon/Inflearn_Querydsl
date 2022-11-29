package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.querydsl.entity.Member;

@Data
@NoArgsConstructor
public class MemberDto {

    private String username;
    private int age;

    @QueryProjection // 이거 붙이고 compileQuerydsl하면 해당 DTO도 Q파일로 생성됨
    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public MemberDto(Member member) {
        this.username = member.getUsername();
        this.age = member.getAge();
    }
}
