package com.lgcms.member.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record Category(
    Long id,
    String name
) {
}
