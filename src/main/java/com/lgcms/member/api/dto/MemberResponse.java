package com.lgcms.member.api.dto;

import com.lgcms.member.domain.Category;
import com.lgcms.member.domain.Member;
import com.lgcms.member.domain.MemberCategory;
import com.lgcms.member.domain.MemberRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {
    public record SignupResponse(
        Boolean alreadyExist,
        String memberId,
        String role
    ) {
        public static SignupResponse toDto(Boolean alreadyExist, String memberId, String role) {
            return new SignupResponse(alreadyExist, memberId, role);
        }
    }

    public record MemberInfoResponse(
        Long memberId,
        String email,
        String nickname,
        MemberRole role,
        Boolean desireLecturer,
        List<Category> categories
    ) {
        public static MemberInfoResponse toDto(Member member) {
            return new MemberInfoResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getRole(),
                member.getDesireLecturer(),
                member.getMemberCategory().stream().map(MemberCategory::getCategory).toList()
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
