package com.lgcms.member.api.open;

import com.lgcms.member.api.dto.MemberRequest.MemberIds;
import com.lgcms.member.api.dto.MemberResponse.MemberInfoResponse;
import com.lgcms.member.common.dto.BaseResponse;
import com.lgcms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/admin/member")
@RestController
@RequiredArgsConstructor
public class OpenAdminMemberController {
    private final MemberService memberService;

    @GetMapping("/lecturer/desirer")
    public ResponseEntity<BaseResponse<List<MemberInfoResponse>>> getLecturerDesirer() {
        return ResponseEntity.ok(BaseResponse.ok(memberService.getLecturerDesirer()));
    }

    @PostMapping("/lecturer/confirm")
    public ResponseEntity<BaseResponse<List<MemberInfoResponse>>> confirmLecturer(
        @RequestBody MemberIds request
    ) {
        return ResponseEntity.ok(BaseResponse.ok(memberService.confirmLecturer(request.memberIds())));
    }
}
