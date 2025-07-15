package com.lgcms.member.api.open;

import com.lgcms.member.api.dto.MemberResponse.MemberInfoResponse;
import com.lgcms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/open/members")
@RestController
@RequiredArgsConstructor
public class OpenMemberController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberInfoResponse> getMyInfo(
        @RequestHeader("X-USER-ID") Long memberId
    ) {
        return ResponseEntity.ok(memberService.getMyInfo(memberId));
    }
}
