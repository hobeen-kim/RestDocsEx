package restapi.restdocs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.restdocs.entity.Member;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpRequest {

    private String memberName;
    private String password;
    private String email;

    public Member toEntity(MemberSignUpRequest memberSignUpRequest) {
        Member member = new Member();
        member.setMemberName(memberSignUpRequest.getMemberName());
        member.setPassword(memberSignUpRequest.getPassword());
        member.setEmail(memberSignUpRequest.getEmail());
        return member;
    }
}
