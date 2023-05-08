package restapi.restdocs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restapi.restdocs.dto.*;
import restapi.restdocs.service.MemberService;

import java.net.URI;

@RequestMapping("/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<PostResponse> signUp(@RequestBody MemberSignUpRequest memberSignUpRequest) {
        memberService.signUp(memberSignUpRequest);
        return ResponseEntity.created(URI.create("/auth/login")).build();
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(@RequestBody MemberLoginRequest memberLoginRequest) {

        memberService.login(memberLoginRequest);

        return ResponseEntity.ok(memberService.login(memberLoginRequest));
    }

    @DeleteMapping("/{memberName}")
    public ResponseEntity<Void> deleteMember(@PathVariable String memberName) {
        memberService.delete(memberName);
        return ResponseEntity.noContent().build();
    }

}
