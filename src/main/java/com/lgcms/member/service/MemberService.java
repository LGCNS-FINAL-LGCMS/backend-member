package com.lgcms.member.service;

import com.lgcms.member.api.dto.MemberRequest.SignupRequest;
import com.lgcms.member.api.dto.MemberResponse.MemberInfoResponse;
import com.lgcms.member.api.dto.MemberResponse.SignupResponse;
import com.lgcms.member.common.dto.exception.BaseException;
import com.lgcms.member.domain.Member;
import com.lgcms.member.domain.SocialMember;
import com.lgcms.member.repository.MemberRepository;
import com.lgcms.member.repository.SocialMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.lgcms.member.common.dto.exception.MemberError.NO_MEMBER_PRESENT;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SocialMemberRepository socialMemberRepository;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        Optional<SocialMember> optinoalSocialMember = null;
        if ((optinoalSocialMember = socialMemberRepository.findBySubAndSocialType(request.sub(), request.socialType())).isPresent()) {
            return SignupResponse.toDto(true, optinoalSocialMember.get().getMember().getId().toString());
        }
        Member member = memberRepository.save(request.toEntity());
        socialMemberRepository.save(request.toEntity(member));
        return SignupResponse.toDto(false, member.getId().toString());
    }

    public MemberInfoResponse getMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BaseException(NO_MEMBER_PRESENT));
        return MemberInfoResponse.toDto(member);
    }

    @Transactional
    public MemberInfoResponse changeInfo(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BaseException(NO_MEMBER_PRESENT));
        member.setNickname(nickname);
        return MemberInfoResponse.toDto(member);
    }
}
