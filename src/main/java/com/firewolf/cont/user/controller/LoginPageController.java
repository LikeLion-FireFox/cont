package com.firewolf.cont.user.controller;

import com.firewolf.cont.user.dto.LoginDto.LoginRequestDto;
import com.firewolf.cont.user.dto.SaveRequestDto;
import com.firewolf.cont.user.service.KakaoService;
import com.firewolf.cont.user.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("loginPage")
public class LoginPageController {

    private final MemberService memberService;
    private final KakaoService kakaoService;

    @Operation(summary = "로그인 페이지")
    @GetMapping("")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok()
                .body(kakaoService.getKakaoLogin());
    }

    @Operation(summary = "회원가입 시 이메일 중복 체크")
    @GetMapping("/save/checkEmail")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email){
        boolean isExist = memberService.checkDuplicatedEmail(email);
        return ResponseEntity.ok()
                .body(isExist);
    }

    @Operation(summary = "로그인(카카오 x)", description = "로그인 성공 => /mainPage로 리다이렉션")
    @PostMapping("")
    public void login(@RequestBody @Valid LoginRequestDto loginRequest,
                      HttpServletRequest servletRequest,
                      HttpServletResponse servletResponse,
                      RedirectAttributes redirectAttributes) throws IOException {
        memberService.login(loginRequest, servletRequest);
        servletResponse.sendRedirect("/mainPage");
    }

    @Operation(summary = "회원가입 페이지")
    @GetMapping("/save")
    public ResponseEntity<Void> save(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원가입(카카오 x)",description = "회원가입 성공 => /loginPage로 리다이렉션")
    @PostMapping("/save")
    public void save(
            @RequestBody @Valid SaveRequestDto saveRequest,
            HttpServletResponse servletResponse
            ) throws IOException {
        memberService.save(saveRequest);
        servletResponse.sendRedirect("/loginPage");
    }

}