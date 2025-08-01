package com.lgcms.member.api.open;

import com.lgcms.member.api.dto.MemberRequest.ChangeInfoRequest;
import com.lgcms.member.api.dto.MemberRequest.NicknameCheckRequest;
import com.lgcms.member.api.dto.MemberResponse.MemberInfoResponse;
import com.lgcms.member.api.dto.MemberResponse.NicknameCheckResponse;
import com.lgcms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/member")
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

    @PatchMapping("/change/info")
    public ResponseEntity<MemberInfoResponse> changeInfo(
        @RequestHeader("X-USER-ID") Long memberId,
        @RequestBody ChangeInfoRequest request
    ) {
        return ResponseEntity.ok(memberService.changeInfo(memberId, request.nickname()));
    }

    @PostMapping("/check/nickname")
    public ResponseEntity<NicknameCheckResponse> checkDuplicateNickname(
        @RequestHeader("X-USER-ID") Long memberId,
        @RequestBody NicknameCheckRequest request
    ) {
        return ResponseEntity.ok(NicknameCheckResponse.toEntity(memberService.checkUsedNickname(memberId, request.nickname())));
    }
}
