package com.lgcms.member.api.dto;

import com.lgcms.member.domain.Category;
import com.lgcms.member.domain.Member;
import com.lgcms.member.domain.MemberRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {
    public record SignupResponse(
        Boolean alreadyExist,
        String memberId
    ) {
        public static SignupResponse toDto(Boolean alreadyExist, String memberId) {
            return new SignupResponse(alreadyExist, memberId);
        }
    }

    public record MemberInfoResponse(
        Long memberId,
        String email,
        String nickname,
        MemberRole role
    ) {
        public static MemberInfoResponse toDto(Member member) {
            return new MemberInfoResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getRole()
            );
        }
    }

    public record NicknameCheckResponse(
        Boolean isUsed
    ) {
        public static NicknameCheckResponse toEntity(Boolean isUsed) {
            return new NicknameCheckResponse(isUsed);
        }
    }

    public record CategoryListResponse(
        List<Category> categories
    ) {
        public static CategoryListResponse toDto(List<Category> categories) {
            return new CategoryListResponse(categories);
        }
    }
}
