package com.example.noticeBoard.Dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyMailReq {
    private String email;
    private String verifyCode;
}
