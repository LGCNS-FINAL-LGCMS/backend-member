package com.lgcms.member.repository;

import com.lgcms.member.domain.Member;
import com.lgcms.member.repository.projection.MemberDbResponse.NicknameOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("""
        SELECT
            m.id AS memberId,
            m.nickname AS nickname
        FROM Member m
        WHERE m.nickname = :nickname
        """)
    NicknameOwner findExistNickname(@Param("nickname") String nickname);
}
