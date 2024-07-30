package com.firewolf.cont.contract.controller;

import com.firewolf.cont.contract.dto.ContractRequest;
import com.firewolf.cont.contract.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
            description = "계약서 형식이 아닌 파일 => 예외 발생(message = 계약서 형식이 아닌 파일, status = BAD_REQUEST)")
    @PostMapping("")
    public ResponseEntity<Map<String,String>> chat(
            @SessionAttribute("memberId") Long memberId,
            @RequestBody ContractRequest contractRequest,
            HttpServletRequest servletRequest
    ) {
        JSONObject chatResponse = contractService.chatAndSave(memberId, contractRequest);
        HttpSession chatResponseSession = servletRequest.getSession();
        chatResponseSession.setAttribute("chatResponse",chatResponse);
        HashMap<String, String> response = new HashMap<>();
        response.put("message","계약서 저장 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "계약서 검토 결과")
    @GetMapping("/chatResultPage")
    public ResponseEntity<JSONObject> chatResultPage(
            HttpServletRequest servletRequest
    ){
        HttpSession session = servletRequest.getSession();
        JSONObject chatResponse = (JSONObject) session.getAttribute("chatResponse");
        session.removeAttribute("chatResponse");
        return ResponseEntity.ok().body(chatResponse);
    }
}