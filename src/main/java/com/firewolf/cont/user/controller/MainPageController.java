package com.firewolf.cont.user.controller;

import com.firewolf.cont.user.dto.LoginDto.LoginResponseDto;
import com.firewolf.cont.user.service.KakaoService;
import com.firewolf.cont.user.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("mainPage")
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
        else if(request.getParameter("code")==null && memberId == null) {
            servletResponse.sendRedirect("/loginPage");
            return null;
        }
        else
            return ResponseEntity.ok().body(memberService.getMemberInfo(memberId));
    }
}