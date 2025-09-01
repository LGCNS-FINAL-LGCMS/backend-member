package com.lgcms.member.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Builder.Default
    private List<MemberCategory> memberCategory = new ArrayList<>();
    @Builder.Default
    private Boolean desireLecturer = Boolean.FALSE;
    private LocalDateTime desireLecturerDate;

    public void updateCategories(List<MemberCategory> memberCategories) {
        this.memberCategory.clear();
        if (memberCategories != null) {
            this.memberCategory.addAll(memberCategories);
            memberCategories.forEach(memberCategory -> memberCategory.setMember(this));
        }
    }

    public void changeToLecturer() {
        this.role = MemberRole.LECTURER;
    }

    public void changeDesireLecturer(Boolean desireLecturer) {
        if (this.role.equals(MemberRole.LECTURER)) {
            this.desireLecturer = Boolean.TRUE;
            return;
        }
        if (desireLecturer == Boolean.TRUE && this.desireLecturer != desireLecturer) {
            this.desireLecturerDate = LocalDateTime.now();
        }
        this.desireLecturer = desireLecturer;
    }
}
