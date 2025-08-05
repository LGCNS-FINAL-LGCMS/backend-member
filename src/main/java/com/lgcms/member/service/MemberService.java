package com.lgcms.member.service;

import com.lgcms.member.api.dto.MemberRequest.SignupRequest;
import com.lgcms.member.api.dto.MemberResponse.CategoryListResponse;
import com.lgcms.member.api.dto.MemberResponse.MemberInfoResponse;
import com.lgcms.member.api.dto.MemberResponse.SignupResponse;
import com.lgcms.member.common.dto.exception.BaseException;
import com.lgcms.member.domain.Category;
import com.lgcms.member.domain.Member;
import com.lgcms.member.domain.MemberCategory;
import com.lgcms.member.domain.SocialMember;
import com.lgcms.member.repository.CategoryRedisRepository;
import com.lgcms.member.repository.MemberCategoryRepository;
import com.lgcms.member.repository.MemberRepository;
import com.lgcms.member.repository.SocialMemberRepository;
import com.lgcms.member.repository.projection.MemberDbResponse.NicknameOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.lgcms.member.common.dto.exception.MemberError.NO_MEMBER_PRESENT;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SocialMemberRepository socialMemberRepository;
    private final CategoryRedisRepository categoryRedisRepository;
    private final MemberCategoryRepository memberCategoryRepository;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        Optional<SocialMember> optinoalSocialMember = null;
        if ((optinoalSocialMember = socialMemberRepository.findBySubAndSocialType(request.sub(), request.socialType())).isPresent()) {
            return SignupResponse.toDto(true, optinoalSocialMember.get().getMember().getId().toString());
        }
        Member member = memberRepository.save(request.toEntity());
        socialMemberRepository.save(request.toEntity(member));
        return SignupResponse.toDto(false, member.getId().toString());
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse getMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BaseException(NO_MEMBER_PRESENT));
        List<MemberCategory> memberCategories = memberCategoryRepository.findByMember(member);
        return MemberInfoResponse.toDto(member, memberCategories);
    }

    @Transactional
    public MemberInfoResponse changeInfo(Long memberId, String nickname, List<Category> categories) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BaseException(NO_MEMBER_PRESENT));
        List<MemberCategory> memberCategories = memberCategoryRepository.findByMember(member);
        if (!categories.isEmpty()) {
            List<Long> categoryIds = categories.stream().map(Category::id).toList();
            List<Category> categoriesById = categoryRedisRepository.getCategoriesById(categoryIds);
            memberCategories = categoriesById.stream().map(categoryById ->
                MemberCategory.builder().category(categoryById).member(member).build()
            ).toList();
            memberCategoryRepository.saveAll(memberCategories);
        }
        if (nickname != null) {
            checkUsedNickname(memberId, nickname);
            member.setNickname(nickname);
        }
        return MemberInfoResponse.toDto(member, memberCategories);
    }

    @Transactional
    public Boolean signout(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BaseException(NO_MEMBER_PRESENT));
        socialMemberRepository.deleteSocialMemberByMember(member);
        memberRepository.delete(member);
        return true;
    }

    public Boolean checkUsedNickname(Long memberId, String nickname) {
        NicknameOwner response = memberRepository.findExistNickname(nickname);
        if (response == null || response.memberId().equals(memberId)) {
            return true;
        }
        return false;
    }

    public CategoryListResponse getCategoryList() {
        return CategoryListResponse.toDto(categoryRedisRepository.getAllCategories());
    }
}
