package com.firewolf.cont.contract.controller;

import com.firewolf.cont.contract.dto.ContractRequest;
import com.firewolf.cont.contract.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("chat")
@RequiredArgsConstructor
public class ChatController {

    private final ContractService contractService;

    @Operation(summary = "계약서 검토 페이지 (검토 전)")
    @GetMapping("/chatPage")
    public ResponseEntity<Void> chat(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "계약서 검토",
            description = " case 1)계약서 검토 완료 => /chat/chatResultPage로 리다이렉션\n" +
                    "case 2) 계약서 형식이 아닌 파일 => 예외 발생(message = 계약서 형식이 아닌 파일, status = BAD_REQUEST)")
    @PostMapping("")
    public void chat(
            @SessionAttribute("memberId") Long memberId,
            @RequestBody ContractRequest contractRequest,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) throws IOException {
        String chatResponse = contractService.chatAndSave(memberId, contractRequest);
        HttpSession chatResponseSession = servletRequest.getSession();
        chatResponseSession.setAttribute("chatResponse",chatResponse);
        servletResponse.sendRedirect("/chat/chatResultPage");
    }

    @Operation(summary = "계약서 검토 결과")
    @GetMapping("/chatResultPage")
    public ResponseEntity<String> chatResultPage(
            HttpServletRequest servletRequest
    ){
        HttpSession session = servletRequest.getSession();
        String chatResponse = (String) session.getAttribute("chatResponse");
        session.removeAttribute("chatResponse");
        return ResponseEntity.ok().body(chatResponse);
    }
}