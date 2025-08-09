package com.lgcms.member.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private MemberRole role;
    @OneToMany(mappedBy = "member", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MemberCategory> memberCategory;
    public void updateCategories(List<MemberCategory> memberCategories) {
        this.memberCategory.clear();
        if(memberCategories != null) {
            this.memberCategory.addAll(memberCategories);
            memberCategories.forEach(memberCategory -> memberCategory.setMember(this));
        }
    }
}
