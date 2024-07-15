package com.firewolf.cont.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RequiredArgsConstructor
@Getter
public enum CustomErrorCode {

    //kakao
    KAKAO_API_CALL_FAILED("카카오 로그인 API 오류",INTERNAL_SERVER_ERROR),
    //member
    NO_MEMBER_CONFIGURED_400("멤버 식별 불가", BAD_REQUEST),
    NO_MEMBER_CONFIGURED_500("멤버 식별 불가", INTERNAL_SERVER_ERROR),
    DUPLICATED_LOGIN_ID_400("중복된 로그인 ID",BAD_REQUEST),
    DUPLICATED_PASSWORD_400("중복된 비밀 번호",BAD_REQUEST);

    private final String errorMessage;
    private final HttpStatus status;
}
