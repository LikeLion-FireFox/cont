package com.firewolf.cont.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class BasicExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity<BasicErrorResponse> handleCustomException(
            CustomException ex, HttpServletRequest request
    ){
        log.error("ex = {}",ex.getErrorCode().getErrorMessage());
        BasicErrorResponse response = BasicErrorResponse.builder()
                .errorMessage(ex.getErrorCode().getErrorMessage())
                .status(ex.getErrorCode().getStatus())
                .timeStamp(LocalDateTime.now())
                .build();
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler
    public ResponseEntity<BasicErrorResponse> handleHttpMessageConvertException(
            HttpMessageConversionException ex, HttpServletRequest request
    ){
        log.error("ex = ",ex);
        BasicErrorResponse response = BasicErrorResponse.builder()
                .errorMessage(ex.getLocalizedMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timeStamp(LocalDateTime.now())
                .build();
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    //valid
    @ExceptionHandler
    public ResponseEntity<BasicErrorResponse> handleMethodArgumentException(
            MethodArgumentNotValidException ex, HttpServletRequest request
    ){
        log.error("ex = {}",ex.getMessage());
        BasicErrorResponse response = BasicErrorResponse.builder()
                .defaultMessages(new HashMap<>())
                .errorMessage("Validation failed")
                .status(HttpStatus.BAD_REQUEST)
                .timeStamp(LocalDateTime.now())
                .build();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            response.getDefaultMessages().put(fieldError.getField(),fieldError.getDefaultMessage());
        }
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler({
            NoHandlerFoundException.class,
            NoResourceFoundException.class
    })
    public ResponseEntity<BasicErrorResponse> handleNoResource(
            Exception ex
    ){
        log.error("ex = {}",ex.getMessage());
//        log.error("detail message = ",ex);
        BasicErrorResponse response = BasicErrorResponse.builder()
                .errorMessage(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .timeStamp(LocalDateTime.now())
                .build();
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler
    public ResponseEntity<BasicErrorResponse> handleException(
            Exception ex, HttpServletRequest request
    ){
        log.error("ex = {}",ex.getMessage());
        log.error("detail message = ",ex);
        BasicErrorResponse response = BasicErrorResponse.builder()
                .errorMessage(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timeStamp(LocalDateTime.now())
                .build();
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }


}
