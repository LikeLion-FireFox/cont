package com.firewolf.cont.user.controller;

import com.firewolf.cont.user.dto.LoginDto.LoginRequestDto;
import com.firewolf.cont.user.dto.SaveRequestDto;
import com.firewolf.cont.user.service.MemberService;
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

    @GetMapping("")
    public ResponseEntity<Void> login() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/save/checkEmail")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email){
        boolean isExist = memberService.checkDuplicatedEmail(email);
        return ResponseEntity.ok()
                .body(isExist);
    }

    @PostMapping("")
    public void login(@RequestBody @Valid LoginRequestDto loginRequest,
                      HttpServletRequest servletRequest,
                      HttpServletResponse servletResponse,
                      RedirectAttributes redirectAttributes) throws IOException {
        memberService.login(loginRequest, servletRequest);
        servletResponse.sendRedirect("/mainPage");
    }

    @GetMapping("/save")
    public ResponseEntity<Void> save(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/save")
    public void save(
            @RequestBody @Valid SaveRequestDto saveRequest,
            HttpServletResponse servletResponse
            ) throws IOException {
        memberService.save(saveRequest);
        servletResponse.sendRedirect("/loginPage");
    }

}