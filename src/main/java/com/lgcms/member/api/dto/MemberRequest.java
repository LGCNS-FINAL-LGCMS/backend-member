package com.lgcms.member.api.dto;

import com.lgcms.member.domain.Member;
import com.lgcms.member.domain.MemberRole;
import com.lgcms.member.domain.SocialMember;
import com.lgcms.member.domain.SocialType;
import com.lgcms.member.service.NicknameUtil;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

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
        String nickname,
        @NotNull(message = "카테고리를 선택하지 않아도 빈 리스트를 입력해주세요.")
        List<Long> categoryIds
    ) {
    }

    public record NicknameCheckRequest(
        String nickname
    ) {
    }
}
