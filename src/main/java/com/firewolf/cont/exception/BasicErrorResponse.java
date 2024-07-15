package com.firewolf.cont.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Getter @Setter
@Builder
public class BasicErrorResponse {

    private Map<String,String> defaultMessages;
    private String errorMessage;
    private HttpStatus status;
    private LocalDateTime timeStamp;

}
