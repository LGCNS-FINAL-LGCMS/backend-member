package com.lgcms.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {
    ADMIN("관리자"),
    LECTURER("강사"),
    STUDENT("학생"),
    ;

    private final String memberRoleName;
}
