package com.firewolf.cont.user.controller;

import com.firewolf.cont.user.dto.LoginDto.LoginResponseDto;
import com.firewolf.cont.user.service.KakaoService;
import com.firewolf.cont.user.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("mainPage")
@Slf4j
public class MainPageController {

    private final KakaoService kakaoService;
    private final MemberService memberService;

    /*
    request.getparam 으로 카카오 서버로부터 redirect 시에 발급되는 인가 코드 => 이를 세션 속성에 넣음 (카카오 로그 아웃)
    */
    @Operation(summary = "카카오 로그인(or +회원 가입) post 요청",
            description = "querystring code 필수, 로그인 성공 후 프론트에서 mainPage로 redirect 필요")
    @PostMapping("/kakao")
    public ResponseEntity<Map<String,String>> loginKakao(
            @RequestParam("code") String code,
            HttpServletRequest request
    ) {
        AtomicBoolean isNew = new AtomicBoolean(false);
        kakaoService.addKakaoInfo(code,request,isNew);
        HashMap<String, String> response = new HashMap<>();
        response.put("message","카카오 로그인 성공");
        if(isNew.get())
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "메인 페이지",description = "이미 로그인 된(쿠키가 존재하는) 회원")
    @GetMapping("")
    public ResponseEntity<LoginResponseDto> mainPage(
            @SessionAttribute(name = "memberId") Long memberId
    ) {
            return ResponseEntity.ok().body(memberService.getMemberInfo(memberId));
    }

    @Operation(summary = "로그 아웃",description = "세션 정보 삭제")
    @PostMapping("/logout")
    public ResponseEntity<Map<String,String>> logout(
            HttpServletRequest servletRequest,
            @SessionAttribute(name = "memberId") Long memberId,
            @SessionAttribute(name = "kakaoToken", required = false) String kakaoToken
    ) {
        HashMap<String, String> response = new HashMap<>();
        HttpSession session = servletRequest.getSession(false);
        if(memberService.isKakaoUser(memberId)) {
            kakaoService.logout(kakaoToken);
            session.removeAttribute("kakaoToken");
        }
        session.removeAttribute("memberId"); // common (kakao user, nonkakao user)
        response.put("message","로그 아웃 성공");
        return ResponseEntity.ok().body(response);
    }
}