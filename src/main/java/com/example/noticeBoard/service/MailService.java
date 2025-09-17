package com.example.noticeBoard.service;

import com.example.noticeBoard.Dto.PostMailReq;
import com.example.noticeBoard.Dto.VerifyMailReq;

public interface MailService {

    void sendVerificationCode(PostMailReq postMailReq);

    boolean verifyCode(VerifyMailReq verifyMailReq);
}
