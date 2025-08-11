package com.lgcms.member.service;

import com.lgcms.member.domain.Member;
import com.lgcms.member.domain.MemberCategory;
import com.lgcms.member.domain.MemberRole;
import com.lgcms.member.repository.MemberCategoryRepository;
import com.lgcms.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
@DisplayName("MemberService 로 ")
class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberCategoryRepository memberCategoryRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final String categoryKeyPrefix = "CATEGORY:";

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
        Set<String> keys = redisTemplate.keys(categoryKeyPrefix + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
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

    @Test
    @DisplayName("사용자 관심사 카테고리를 수정할 수 있다")
    void changeInfo() {
        // given
        Member member = memberRepository.save(Member.builder()
            .role(MemberRole.STUDENT)
            .email("student@gmail.com")
            .nickname("student")
            .build());
        redisTemplate.opsForValue().set(categoryKeyPrefix + "1", "스프링");
        redisTemplate.opsForValue().set(categoryKeyPrefix + "2", "리액트");
        redisTemplate.opsForValue().set(categoryKeyPrefix + "3", "노드js");

        List<Long> categoryIds = List.of(1L, 2L);

        // when
        memberService.changeInfo(member.getId(), null, categoryIds, false);

        // then
        List<MemberCategory> memberCategories = memberCategoryRepository.findByMember(member);
        assertThat(memberCategories).hasSize(2)
            .extracting("category.id", "category.name", "member.id")
            .containsExactlyInAnyOrder(
                tuple(1L, "스프링", member.getId()),
                tuple(2L, "리액트", member.getId())
            );
    }

    @Test
    @DisplayName("사용자 관심사 카테고리를 수정하면 기존의 정보를 제거하고 새로운 정보를 추가한다")
    void changeInfo2() {
        // given
        Member member = memberRepository.save(Member.builder()
            .role(MemberRole.STUDENT)
            .email("student@gmail.com")
            .nickname("student")
            .build());
        redisTemplate.opsForValue().set(categoryKeyPrefix + "1", "스프링");
        redisTemplate.opsForValue().set(categoryKeyPrefix + "2", "리액트");
        redisTemplate.opsForValue().set(categoryKeyPrefix + "3", "노드js");

        memberService.changeInfo(member.getId(), null, List.of(1L, 2L), false);

        List<Long> categoryIds = List.of(2L, 3L);

        // when
        memberService.changeInfo(member.getId(), null, categoryIds, false);

        // then
        List<MemberCategory> memberCategories = memberCategoryRepository.findByMember(member);
        assertThat(memberCategories).hasSize(2)
            .extracting("category.id", "category.name", "member.id")
            .containsExactlyInAnyOrder(
                tuple(2L, "리액트", member.getId()),
                tuple(3L, "노드js", member.getId())
            );
    }
}