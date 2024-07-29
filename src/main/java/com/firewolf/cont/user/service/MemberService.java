package com.firewolf.cont.user.service;

import com.firewolf.cont.contract.entity.Contract;
import com.firewolf.cont.contract.repository.ContractRepository;
import com.firewolf.cont.exception.CustomException;
import com.firewolf.cont.user.dto.LoginDto.LoginRequestDto;
import com.firewolf.cont.user.dto.LoginDto.LoginResponseDto;
import com.firewolf.cont.user.dto.MyPageResponse;
import com.firewolf.cont.user.dto.MyPageResponse.MyPageContract;
import com.firewolf.cont.user.dto.SaveRequestDto;
import com.firewolf.cont.user.entity.Member;
import com.firewolf.cont.user.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.firewolf.cont.exception.CustomErrorCode.*;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ContractRepository contractRepository;

    @Transactional
    public void save(SaveRequestDto saveRequest){
        memberRepository.findByNickname(saveRequest.getNickname())
                        .ifPresent(member -> {throw new CustomException(DUPLICATED_NICKNAME_400);});
        memberRepository.findByAccountEmail(saveRequest.getEmail())
                        .ifPresent(member -> {throw new CustomException(DUPLICATED_EMAIL_400);});
        memberRepository.save(Member.builder()
                .accountEmail(saveRequest.getEmail())
                .nickname(saveRequest.getNickname())
                .birthday(saveRequest.getBirthday())
                .password(saveRequest.getPassword())
                .isKakaoMember(false)
                .build());
    }

    @Transactional
    public void login(LoginRequestDto loginRequest, HttpServletRequest servletRequest){
        Member member = memberRepository.findByAccountEmail(loginRequest.getEmail())
                .orElseThrow(() -> new CustomException(NO_MEMBER_CONFIGURED_400));
        HttpSession session = servletRequest.getSession(true);
        session.setAttribute("memberId",member.getId());
        log.info("Login successful, session memberId: {}", session.getAttribute("memberId"));
    }

    public MyPageResponse myPage(Long memberId, Pageable pageable){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(NO_MEMBER_CONFIGURED_500));
        Page<MyPageContract> myPageContracts = contractRepository.findByMemberOrderByCreatedDateDesc(member, pageable)
                .map(MyPageContract::toDto);
        return MyPageResponse.toDto(member,myPageContracts);
    }

    public LoginResponseDto getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(NO_MEMBER_CONFIGURED_500));
        return LoginResponseDto.toDto(member);
    }

    public boolean checkDuplicatedEmail(String email){
        System.out.println("email = " + email);
        return memberRepository.findByAccountEmail(email).isPresent();
    }

    public boolean isKakaoUser(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(NO_MEMBER_CONFIGURED_500));
        return member.getIsKakaoMember();
    }
}