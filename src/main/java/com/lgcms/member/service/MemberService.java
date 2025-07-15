package com.lgcms.member.service;

import com.lgcms.member.api.dto.MemberResponse.MemberInfoResponse;
import com.lgcms.member.api.dto.MemberResponse.SignupResponse;
import com.lgcms.member.common.dto.exception.BaseException;
import com.lgcms.member.domain.Member;
import com.lgcms.member.domain.MemberRole;
import com.lgcms.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.lgcms.member.common.dto.exception.MemberError.NO_MEMBER_PRESENT;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public SignupResponse signup(String sub, String email) {
        Optional<Member> optinoalMember = null;
        if ((optinoalMember = memberRepository.findMemberBySub(sub)).isPresent()) {
            return SignupResponse.toDto(true, optinoalMember.get().getId().toString());
        }
        Member member = Member.builder()
            .email(email)
            .sub(sub)
            .nickname(NicknameUtil.generateNickname())
            .role(MemberRole.STUDENT)
            .build();
        memberRepository.save(member);
        return SignupResponse.toDto(false, member.getId().toString());
    }

    public MemberInfoResponse getMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BaseException(NO_MEMBER_PRESENT));
        return MemberInfoResponse.toDto(member);
    }
}
