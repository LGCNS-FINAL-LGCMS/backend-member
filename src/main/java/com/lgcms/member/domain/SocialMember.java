package com.lgcms.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialMember {
    @Id
    @Column(name = "social_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Member member;
    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;
    private String sub;
}
