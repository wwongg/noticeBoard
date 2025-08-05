package com.example.noticeBoard.handler.advice;


import com.example.noticeBoard.Exception.BadRequestException;
import com.example.noticeBoard.Exception.Login_RestException;
import com.example.noticeBoard.Exception.NotFindPage_RestException;
import com.example.noticeBoard.handler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "messageboard.controller")
public class RestApiControllerAdvice {

    @ExceptionHandler(NotFindPage_RestException.class)
    public ResponseEntity<ErrorResult> notFindPageRest(NotFindPage_RestException e) {
        log.error("[404에러]", e);
        ErrorResult errorResult = new ErrorResult("Update-EX", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResult);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> badRequest(BadRequestException e) {
        log.error("[401에러]", e);
        ErrorResult errorResult = new ErrorResult("BadRequest-EX", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> login_restHandler(Login_RestException e) {
        log.error("[login 에러] ex", e);
        ErrorResult errorResult = new ErrorResult("Login-rest-Ex", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResult);
    }
}
