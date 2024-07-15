package com.firewolf.cont.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final CustomErrorCode errorCode;

    public CustomException(CustomErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
