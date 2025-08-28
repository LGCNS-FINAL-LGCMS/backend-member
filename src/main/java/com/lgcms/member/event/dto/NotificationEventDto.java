package com.lgcms.member.event.dto;

import com.lgcms.member.domain.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationEventDto {
    public record RoleModified(
        Long memberId,
        String nickname,
        String memberRoleName
    ) {
        static public RoleModified toDto(Member member) {
            return new RoleModified(member.getId(), member.getNickname(), member.getRole().getMemberRoleName());
        }
    }
}
