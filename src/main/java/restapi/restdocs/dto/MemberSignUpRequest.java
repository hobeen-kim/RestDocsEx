package restapi.restdocs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.restdocs.entity.Member;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpRequest {

    @NotBlank
    private String memberName;
    @NotBlank
    private String password;
    @Email
    private String email;

    public Member toEntity(MemberSignUpRequest memberSignUpRequest) {
        Member member = new Member();
        member.setMemberName(memberSignUpRequest.getMemberName());
        member.setPassword(memberSignUpRequest.getPassword());
        member.setEmail(memberSignUpRequest.getEmail());
        return member;
    }
}
