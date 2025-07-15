package com.lgcms.member.service;

import com.lgcms.member.api.dto.MemberResponse.SignupResponse;
import com.lgcms.member.domain.Member;
import com.lgcms.member.domain.MemberRole;
import com.lgcms.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public SignupResponse signup(String sub, String email) {
        Optional<Member> optinoalMember = null;
        if ((optinoalMember = memberRepository.findMemberBySubject(sub)).isPresent()) {
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
}
