package com.firewolf.cont.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firewolf.cont.exception.CustomException;
import com.firewolf.cont.user.entity.Member;
import com.firewolf.cont.user.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.firewolf.cont.exception.CustomErrorCode.*;
import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class KakaoService {

    private final MemberRepository memberRepository;

    @Value("${kakao.client_id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.redirect_uri}")
    private String KAKAO_REDIRECT_URI;

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    public String getKakaoLogin() {
        return KAKAO_AUTH_URI + "/oauth/authorize"
                + "?client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URI
                + "&response_type=code";
    }

    @Transactional
    public void addKakaoInfo(String code, HttpServletRequest servletRequest, AtomicBoolean isNew) {
        if (code == null) throw new CustomException(NO_KAKAO_CODE_CONFIGURED);

        String accessToken = "";
//        String refreshToken = "";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", KAKAO_CLIENT_ID);
            params.add("code", code);
            params.add("redirect_uri", KAKAO_REDIRECT_URI);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            /**
             * redirect 시 kakao server 로부터 발급받은 code를 활용해서
             * kakao server 로부터 토큰(response) 요청 후 발급받기
             */
            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_AUTH_URI + "/oauth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());

            accessToken  = (String) jsonObj.get("access_token");
//            refreshToken = (String) jsonObj.get("refresh_token");
        } catch (Exception e) {
            log.info("kakao login api call exception",e);
            throw new CustomException(KAKAO_API_CALL_FAILED);
        }

        setUserInfoWithToken(accessToken,servletRequest,isNew);
    }

    private void setUserInfoWithToken(String accessToken, HttpServletRequest servletRequest,AtomicBoolean isNew) {
        //HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        //Kakao Server 로부터 오는 Response 데이터 파싱
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = null;
        try {
            jsonObj = (JSONObject) jsonParser.parse(response.getBody());
        }catch (Exception e){
            log.info("json parse exception",e);
            throw new CustomException(JSON_PARSE_EXCEPTION_500);
        }
        JSONObject account = (JSONObject) jsonObj.get("kakao_account");
        JSONObject profile = (JSONObject) account.get("profile");

        long id = (long) jsonObj.get("id");
        String email = String.valueOf(account.get("email"));
        String nickname = String.valueOf(profile.get("nickname"));

        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        sb.append((account.get("birthyear"))).append((account.get("birthday")));

        HttpSession session = servletRequest.getSession(true);
        Optional<Member> optionalMember = memberRepository.findByKakaoId(id);
        if(optionalMember.isEmpty()) {
            isNew.set(true);
        }
        Member member = optionalMember.orElseGet(() ->
                memberRepository.save(Member.builder()
                        .kakaoId(id)
                        .isKakaoMember(TRUE)
                        .nickname(nickname)
                        .birthday(LocalDate.parse(sb, formatter))
                        .accountEmail(email)
                        .build())
        );

        session.setAttribute("kakaoToken",accessToken);
        session.setAttribute("memberId", member.getId());
        log.info("session attributes = {}",session.getAttributeNames());
//        return LoginResponseDto.builder()
//                .id(member.getId())
//                .nickname(nickname)
//                .build();
    }


    public void logout(String accessToken)  {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoLogoutRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/logout",
                HttpMethod.POST,
                kakaoLogoutRequest,
                String.class
        );
    }
}