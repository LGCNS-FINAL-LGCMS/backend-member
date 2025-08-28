package com.lgcms.member.event.dto;

import com.lgcms.member.domain.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberInfoDto {
    public record NicknameModified(
        Long memberId
    ) {
        static public NicknameModified toDto(Member member) {
            return new NicknameModified(member.getId());
        }
    }

    public record MemberQuited(
        Long memberId,
        String nickname
    ) {
        public static MemberQuited toDto(Member member) {
            return new MemberQuited(member.getId(), member.getNickname());
        }
    }
}
