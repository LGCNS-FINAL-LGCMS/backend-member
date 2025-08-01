package com.lgcms.member.service;

import com.lgcms.member.domain.Member;
import com.lgcms.member.domain.MemberRole;
import com.lgcms.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@DisplayName("MemberService 로 ")
class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("이미 가입한 사용자가 자신의 닉네임으로 변경하려 할떄 닉네임이 사용가능한지 알 수 있다.")
    void checkMyNickname() {
        // given
        String duplicateNickname = "duplicate";
        String notDuplicateNickname = "not-duplicate";
        Member oldMember = memberRepository.save(Member.builder()
            .role(MemberRole.STUDENT)
            .email("student@gamil.com")
            .nickname(duplicateNickname)
            .build()
        );
        Member newMember = memberRepository.save(Member.builder()
            .role(MemberRole.STUDENT)
            .email("newStudent@gamil.com")
            .build());

        // when
        Boolean checkMyNickname = memberService.checkUsedNickname(oldMember.getId(), duplicateNickname);

        // then
        assertThat(checkMyNickname).isTrue();
    }

    @Test
    @DisplayName("이미 가입한 사용자가 다른 사용자가 사용중인 닉네임이 이미 사용중인지 알 수 있다.")
    void checkNotDuplicatedNickname() {
        // given
        String duplicateNickname = "duplicate";
        String notDuplicateNickname = "not-duplicate";
        Member oldMember = memberRepository.save(Member.builder()
            .role(MemberRole.STUDENT)
            .email("student@gamil.com")
            .nickname(duplicateNickname)
            .build()
        );
        Member newMember = memberRepository.save(Member.builder()
            .role(MemberRole.STUDENT)
            .email("newStudent@gamil.com")
            .build());

        // when
        Boolean checkNotDuplicatedNickname = memberService.checkUsedNickname(oldMember.getId(), notDuplicateNickname);

        // then
        assertThat(checkNotDuplicatedNickname).isTrue();
    }

    @Test
    @DisplayName("다른 사용자가 사용중인 닉네임이 이미 사용중인지 알 수 있다.")
    void checkDuplicatedNickname() {
        // given
        String duplicateNickname = "duplicate";
        String notDuplicateNickname = "not-duplicate";
        Member oldMember = memberRepository.save(Member.builder()
            .role(MemberRole.STUDENT)
            .email("student@gamil.com")
            .nickname(duplicateNickname)
            .build()
        );
        Member newMember = memberRepository.save(Member.builder()
            .role(MemberRole.STUDENT)
            .email("newStudent@gamil.com")
            .build());

        // when
        Boolean checkDuplicatedNickname = memberService.checkUsedNickname(newMember.getId(), duplicateNickname);

        // then
        assertThat(checkDuplicatedNickname).isFalse();
    }

    @Test
    @DisplayName("다른 사용자가 사용중이지 않은 닉네임이 이미 사용중인지 알 수 있다.")
    void checkNotDuplicatedNewNickname() {
        // given
        String duplicateNickname = "duplicate";
        String notDuplicateNickname = "not-duplicate";
        Member oldMember = memberRepository.save(Member.builder()
            .role(MemberRole.STUDENT)
            .email("student@gamil.com")
            .nickname(duplicateNickname)
            .build()
        );
        Member newMember = memberRepository.save(Member.builder()
            .role(MemberRole.STUDENT)
            .email("newStudent@gamil.com")
            .build());

        // when
        Boolean checkNotDuplicatedNewNickname = memberService.checkUsedNickname(newMember.getId(), notDuplicateNickname);

        // then
        assertThat(checkNotDuplicatedNewNickname).isTrue();
    }
}