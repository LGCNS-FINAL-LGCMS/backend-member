package com.lgcms.member.api.backend;

import com.lgcms.member.api.dto.MemberRequest.SignupRequest;
import com.lgcms.member.api.dto.MemberResponse.SignupResponse;
import com.lgcms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/internal/member")
@RestController
@RequiredArgsConstructor
public class BackendMemberController {
    private final MemberService memberService;

    /**
     * 요청한 subject의 사용자가 존재한다면 member id를 반환하고<BR/>
     * 존재하지 않는다면 새로운 사용자를 생성하고 member id를 반환
     */
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(memberService.signup(request));
    }

    @DeleteMapping("/signout")
    public ResponseEntity<Boolean> signout(@RequestParam Long memberId) {
        return ResponseEntity.ok(memberService.signout(memberId));
    }
}
