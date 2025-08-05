package com.lgcms.member.api.open;

import com.lgcms.member.api.dto.MemberRequest.ChangeInfoRequest;
import com.lgcms.member.api.dto.MemberRequest.NicknameCheckRequest;
import com.lgcms.member.api.dto.MemberResponse.MemberInfoResponse;
import com.lgcms.member.api.dto.MemberResponse.NicknameCheckResponse;
import com.lgcms.member.common.dto.BaseResponse;
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
    public ResponseEntity<BaseResponse<MemberInfoResponse>> getMyInfo(
        @RequestHeader("X-USER-ID") Long memberId
    ) {
        return ResponseEntity.ok(BaseResponse.ok(memberService.getMyInfo(memberId)));
    }

    @PatchMapping("/change/info")
    public ResponseEntity<BaseResponse<MemberInfoResponse>> changeInfo(
        @RequestHeader("X-USER-ID") Long memberId,
        @RequestBody ChangeInfoRequest request
    ) {
        return ResponseEntity.ok(BaseResponse.ok(memberService.changeInfo(memberId, request.nickname())));
    }

    @PostMapping("/check/nickname")
    public ResponseEntity<BaseResponse<NicknameCheckResponse>> checkDuplicateNickname(
        @RequestHeader("X-USER-ID") Long memberId,
        @RequestBody NicknameCheckRequest request
    ) {
        return ResponseEntity.ok(BaseResponse.ok(NicknameCheckResponse.toEntity(memberService.checkUsedNickname(memberId, request.nickname()))));
    }
}
