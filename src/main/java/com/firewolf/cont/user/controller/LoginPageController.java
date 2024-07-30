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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("loginPage")
public class LoginPageController {

    private final MemberService memberService;
    private final KakaoService kakaoService;

    @Operation(summary = "로그인 페이지")
    @GetMapping("")
    public ResponseEntity<Map<String,String>> login() {
        HashMap<String, String> response = new HashMap<>();
        response.put("kakao_link",kakaoService.getKakaoLogin());
        return ResponseEntity.ok()
                .body(response);
    }

    @Operation(summary = "회원가입 시 이메일 중복 체크")
    @GetMapping("/save/checkEmail")
    public ResponseEntity<Map<String,Boolean>> checkEmail(@RequestParam("email") String email){
        boolean isExist = memberService.checkDuplicatedEmail(email);
        HashMap<String, Boolean> response = new HashMap<>();
        response.put("status",isExist);
        return ResponseEntity.ok()
                .body(response);
    }

    @Operation(summary = "로그인(카카오 x)", description = "로그인 성공 => /mainPage로 리다이렉션")
    @PostMapping("")
    public ResponseEntity<Map<String,String>> login(@RequestBody @Valid LoginRequestDto loginRequest,
                      HttpServletRequest servletRequest)  {
        memberService.login(loginRequest, servletRequest);
        HashMap<String, String> response = new HashMap<>();
        response.put("message","로그인 성공");
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "회원가입 페이지")
    @GetMapping("/save")
    public ResponseEntity<Void> save(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원가입(카카오 x)",description = "회원가입 성공 => /loginPage로 리다이렉션")
    @PostMapping("/save")
    public ResponseEntity<Map<String,String>> save(@RequestBody @Valid SaveRequestDto saveRequest) {
        memberService.save(saveRequest);
        HashMap<String, String> response = new HashMap<>();
        response.put("message","회원가입 성공");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

}