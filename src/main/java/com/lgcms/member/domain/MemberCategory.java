package com.lgcms.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCategory {
    @Id
    @Column(name = "member_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Category category;
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private Member member;
}
