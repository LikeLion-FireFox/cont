package com.firewolf.cont.user.controller;

import com.firewolf.cont.user.dto.LoginDto.LoginResponseDto;
import com.firewolf.cont.user.service.KakaoService;
import com.firewolf.cont.user.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("mainPage")
@Slf4j
public class MainPageController {

    private final KakaoService kakaoService;
    private final MemberService memberService;

    /*이 쪽으로 redirect => 카카오 서버로부터 로그인 정보 가져오고, 일부 정보 반환
    request.getparam 으로 카카오 서버로부터 redirect 시에 발급되는 인가 코드 => 이를 세션 속성에 넣기
    */
    @GetMapping("")
    public ResponseEntity<LoginResponseDto> mainPage(
            HttpServletRequest request,
            HttpServletResponse servletResponse,
            @SessionAttribute(name = "memberId", required = false) Long memberId
    ) throws Exception {
        if(request.getParameter("code")!=null)
            return ResponseEntity.ok().body(kakaoService.getKakaoInfo(request.getParameter("code"),request));
        else if(request.getParameter("code")==null && memberId == null) { //로그인 되지 않은 회원
            servletResponse.sendRedirect("/loginPage");
            return null;
        }
        else
            return ResponseEntity.ok().body(memberService.getMemberInfo(memberId));
    }

    @PostMapping("/logout")
    public void logout(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse,
            @SessionAttribute(name = "memberId") Long memberId,
            @SessionAttribute(name = "kakaoToken", required = false) String kakaoToken
    ) throws IOException {
        HttpSession session = servletRequest.getSession(false);
        log.info("session = {}",session.getAttributeNames());
        if(memberService.isKakaoUser(memberId)) {
            kakaoService.logout(kakaoToken);
            session.removeAttribute("kakaoToken");
        }
        session.removeAttribute("memberId"); // common (kakao user, nonkakao user)
        servletResponse.sendRedirect("/loginPage");
    }
}