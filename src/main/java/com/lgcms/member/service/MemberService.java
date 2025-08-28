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
import com.lgcms.member.event.dto.MemberInfoDto.MemberQuited;
import com.lgcms.member.event.dto.MemberInfoDto.NicknameModified;
import com.lgcms.member.event.dto.NotificationEventDto;
import com.lgcms.member.event.producer.MemberInfoEventProducer;
import com.lgcms.member.event.producer.NotificationEventProducer;
import com.lgcms.member.repository.CategoryRedisRepository;
import com.lgcms.member.repository.MemberRepository;
import com.lgcms.member.repository.SocialMemberRepository;
import com.lgcms.member.repository.projection.MemberDbResponse.NicknameOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.lgcms.member.common.dto.exception.MemberError.DUPLICATE_NICKNAME;
import static com.lgcms.member.common.dto.exception.MemberError.NO_MEMBER_PRESENT;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SocialMemberRepository socialMemberRepository;
    private final CategoryRedisRepository categoryRedisRepository;
    private final MemberInfoEventProducer memberInfoEventProducer;
    private final NotificationEventProducer notificationEventProducer;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        Optional<SocialMember> optinoalSocialMember = null;
        if ((optinoalSocialMember = socialMemberRepository.findBySubAndSocialType(request.sub(), request.socialType())).isPresent()) {
            return SignupResponse.toDto(true, MemberInfoResponse.toDto(optinoalSocialMember.get().getMember()));
        }
        Member member = memberRepository.save(request.toEntity());
        socialMemberRepository.save(request.toEntity(member));
        return SignupResponse.toDto(false, MemberInfoResponse.toDto(member));
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse getMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BaseException(NO_MEMBER_PRESENT));
        return MemberInfoResponse.toDto(member);
    }

    @Transactional
    public MemberInfoResponse changeInfo(Long memberId, String nickname, List<Long> categoryIds, Boolean desireLecturer) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BaseException(NO_MEMBER_PRESENT));
        if (categoryIds == null) {
            categoryIds = new ArrayList<>();
        }
        changeMemberCategories(categoryIds, member);
        if (nickname != null) {
            if (checkUsedNickname(memberId, nickname))
                throw new BaseException(DUPLICATE_NICKNAME);
            else {
                member.setNickname(nickname);
                memberInfoEventProducer.memberModified(NicknameModified.toDto(member));
            }
        }
        member.changeDesireLecturer(desireLecturer);
        return MemberInfoResponse.toDto(member);
    }

    private List<MemberCategory> changeMemberCategories(List<Long> categoryIds, Member member) {
        List<Category> categoriesById = categoryRedisRepository.getCategoriesById(categoryIds);
        List<MemberCategory> memberCategories = categoriesById.stream().map(categoryById ->
            MemberCategory.builder().category(categoryById).member(member).build()
        ).toList();
        member.updateCategories(memberCategories);
        return memberCategories;
    }

    @Transactional
    public Boolean signout(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BaseException(NO_MEMBER_PRESENT));
        socialMemberRepository.deleteSocialMemberByMember(member);
        memberRepository.delete(member);
        memberInfoEventProducer.memberQuitedType(MemberQuited.toDto(member));
        return true;
    }

    public Boolean checkUsedNickname(Long memberId, String nickname) {
        NicknameOwner response = memberRepository.findExistNickname(nickname);
        if (response == null || response.memberId().equals(memberId)) {
            return false;
        }
        return true;
    }

    public CategoryListResponse getCategoryList() {
        return CategoryListResponse.toDto(categoryRedisRepository.getAllCategories());
    }

    public List<MemberInfoResponse> getLecturerDesirer() {
        List<Member> lecturerDesirers = memberRepository.findLecturerDesirer();
        return lecturerDesirers.stream().map(MemberInfoResponse::toDto).toList();
    }

    @Transactional
    public List<MemberInfoResponse> confirmLecturer(List<Long> memberIds) {
        List<Member> lecturerDesirers = memberRepository.findAllById(memberIds);
        lecturerDesirers.forEach(Member::changeToLecturer);
        lecturerDesirers.forEach(lecturerDesirer -> {
            notificationEventProducer.roleModified(NotificationEventDto.RoleModified.toDto(lecturerDesirer));
        });
        return lecturerDesirers.stream().map(MemberInfoResponse::toDto).toList();
    }
}
