package com.example.noticeBoard.handler;


import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class ErrorResult {
    private String code;
    private String message;
}
