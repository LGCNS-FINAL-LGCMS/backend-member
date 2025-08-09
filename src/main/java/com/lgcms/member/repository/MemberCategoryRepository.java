package com.lgcms.member.repository;

import com.lgcms.member.domain.Member;
import com.lgcms.member.domain.MemberCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberCategoryRepository extends JpaRepository<MemberCategory, Long> {
    List<MemberCategory> findByMember(Member member);
}
