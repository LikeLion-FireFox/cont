package com.firewolf.cont.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("memberId") == null) {
            log.info("미인증 사용자 요청");

            // JSON 형식의 응답 반환
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Unauthorized. Please login.");
            responseBody.put("redirectUrl","http://localhost:5173/Login");

            response.getWriter().write(objectMapper.writeValueAsString(responseBody));

            return false;
        }
        return true;
    }
}