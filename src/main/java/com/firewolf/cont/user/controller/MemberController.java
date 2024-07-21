package com.firewolf.cont.user.controller;

import com.firewolf.cont.user.dto.MyPageResponse;
import com.firewolf.cont.user.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("member")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "마이페이지", description = "size option만 넘겨주세요")
    @GetMapping("/myPage")
    public ResponseEntity<MyPageResponse> myPage(
            @SessionAttribute(name = "memberId") Long memberId,
            Pageable pageable
    ){
        return ResponseEntity.ok().body(memberService.myPage(memberId, pageable));
    }

}
