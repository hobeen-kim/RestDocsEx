package restapi.restdocs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import restapi.restdocs.dto.MemberLoginRequest;
import restapi.restdocs.dto.MemberResponse;
import restapi.restdocs.dto.MemberSignUpRequest;
import restapi.restdocs.entity.Member;
import restapi.restdocs.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse signUp(MemberSignUpRequest memberSignUpRequest) {
        Member member = memberSignUpRequest.toEntity(memberSignUpRequest);
        memberRepository.save(member);
        return MemberResponse.of(member);
    }

    public MemberResponse login(MemberLoginRequest memberLoginRequest) {
        Member member = memberRepository.findByMemberName(memberLoginRequest.getMemberName()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다. memberName=" + memberLoginRequest.getMemberName()));
        if (!member.getPassword().equals(memberLoginRequest.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        } else {

        return MemberResponse.of(member);
    }
    }

    public void delete(String memberName) {
        Member member = memberRepository.findByMemberName(memberName).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다. memberName=" + memberName));
        memberRepository.delete(member);
    }
}
