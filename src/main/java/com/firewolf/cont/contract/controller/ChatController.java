package com.firewolf.cont.contract.controller;

import com.firewolf.cont.contract.dto.ContractRequest;
import com.firewolf.cont.contract.service.ContractService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static java.net.URLEncoder.encode;

@RestController
@RequestMapping("chat")
@RequiredArgsConstructor
public class ChatController {

    private final ContractService contractService;

    @GetMapping("/chatPage")
    public ResponseEntity<Void> chat(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("")
    public void chat(
            @SessionAttribute("memberId") Long memberId,
            @ModelAttribute ContractRequest contractRequest,
            HttpServletResponse servletResponse
    ) throws IOException {
        String chatResponse = contractService.chatAndSave(memberId, contractRequest);
        servletResponse.sendRedirect("/chat/chatResultPage?chatResponse="+ encode(chatResponse, "UTF-8"));
    }

    @GetMapping("/chatResultPage")
    public ResponseEntity<String> chatResultPage(
            @RequestParam("chatResponse") String chatResponse
    ){
        System.out.println("chatResponse = " + chatResponse);
        return ResponseEntity.ok().body(chatResponse);
    }
}