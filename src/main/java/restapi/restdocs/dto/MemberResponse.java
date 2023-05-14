package restapi.restdocs.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.restdocs.entity.Authority;
import restapi.restdocs.entity.Member;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private String memberName;
    private String password;
    private String email;
    private Authority authority;

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getMemberName(), member.getPassword(), member.getEmail(), member.getAuthority());
    }
}
