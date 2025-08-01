package com.lgcms.member.api.dto;

import com.lgcms.member.domain.Member;
import com.lgcms.member.domain.MemberRole;
import com.lgcms.member.domain.SocialMember;
import com.lgcms.member.domain.SocialType;
import com.lgcms.member.service.NicknameUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberRequest {
    public record SignupRequest(
        String sub,
        String email,
        SocialType socialType
    ) {
        public Member toEntity() {
            return Member.builder()
                .email(email)
                .nickname(NicknameUtil.generateNickname())
                .role(MemberRole.STUDENT)
                .build();
        }

        public SocialMember toEntity(Member member) {
            return SocialMember.builder()
                .member(member)
                .sub(sub)
                .socialType(socialType)
                .build();
        }
    }

    public record ChangeInfoRequest(
        String nickname
    ) {
    }

    public record NicknameCheckRequest(
        String nickname
    ) {
    }
}
