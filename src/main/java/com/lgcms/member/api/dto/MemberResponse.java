package com.lgcms.member.api.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
}
