package com.firewolf.cont.user.service;

import com.firewolf.cont.exception.CustomErrorCode;
import com.firewolf.cont.exception.CustomException;
import com.firewolf.cont.user.dto.LoginDto;
import com.firewolf.cont.user.dto.LoginDto.LoginRequestDto;
import com.firewolf.cont.user.dto.LoginDto.LoginResponseDto;
import com.firewolf.cont.user.dto.SaveRequestDto;
import com.firewolf.cont.user.entity.Member;
import com.firewolf.cont.user.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.firewolf.cont.exception.CustomErrorCode.NO_MEMBER_CONFIGURED_400;
import static com.firewolf.cont.exception.CustomErrorCode.NO_MEMBER_CONFIGURED_500;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void save(SaveRequestDto saveRequest){
        memberRepository.save(Member.builder()
                .accountEmail(saveRequest.getEmail())
                .nickname(saveRequest.getNickname())
                .birthday(saveRequest.getBirthday())
                .password(saveRequest.getPassword())
                .isKakaoMember(false)
                .build());
    }

    public void login(LoginRequestDto loginRequest, HttpServletRequest servletRequest){
        memberRepository.findByAccountEmail(loginRequest.getEmail())
                .orElseThrow(() ->new CustomException(NO_MEMBER_CONFIGURED_400));
        Member member = memberRepository.findByPassword(loginRequest.getPassword())
                .orElseThrow(() -> new CustomException(NO_MEMBER_CONFIGURED_400));
        HttpSession session = servletRequest.getSession(true);
        session.setAttribute("memberId",member.getId());
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
}
