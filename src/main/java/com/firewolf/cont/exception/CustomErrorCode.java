package com.firewolf.cont.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RequiredArgsConstructor
@Getter
public enum CustomErrorCode {

    //contract
    EXCEEDED_CONTENT_LENGTH_400("계약서 길이 초과, 최대 단어 수는 약 1500개",BAD_REQUEST),
    CONTRACT_FORMAT_ERROR_400("계약서 형식이 아닌 파일",BAD_REQUEST),
    JSON_PARSE_EXCEPTION_500("JSON 객체 변환 오류",INTERNAL_SERVER_ERROR),

    //kakao
    KAKAO_API_CALL_FAILED("카카오 로그인 API 오류",INTERNAL_SERVER_ERROR),
    //member
    NO_MEMBER_CONFIGURED_400("회원 식별 불가", BAD_REQUEST),
    NO_MEMBER_CONFIGURED_500("회원 식별 불가", INTERNAL_SERVER_ERROR),
    DUPLICATED_EMAIL_400("중복된 이메일 계정",BAD_REQUEST),
    DUPLICATED_NICKNAME_400("중복된 닉네임",BAD_REQUEST),
    DUPLICATED_PASSWORD_400("중복된 비밀 번호",BAD_REQUEST);

    private final String errorMessage;
    private final HttpStatus status;

}
