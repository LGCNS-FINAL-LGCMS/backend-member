package com.lgcms.member.repository;

import com.lgcms.member.domain.SocialMember;
import com.lgcms.member.domain.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialMemberRepository extends JpaRepository<SocialMember, Long> {
    Optional<SocialMember> findBySubAndSocialType(String sub, SocialType socialType);
}
