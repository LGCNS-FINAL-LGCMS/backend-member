package com.lgcms.member.repository.projection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDbResponse {
    public record NicknameOwner(
        Long memberId,
        String nickname
    ) {
    }
}
