package com.example.noticeBoard.service.Impl;


import com.example.noticeBoard.Dto.PostMailReq;
import com.example.noticeBoard.Dto.VerifyMailReq;
import com.example.noticeBoard.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;

    private final Map<String, String> verificationStore = new ConcurrentHashMap<>();

    @Override
    public void sendVerificationCode(PostMailReq postMailReq) {
        String email = postMailReq.getEmail();

        String code = String.format("%06d", new Random().nextInt(999999));

        verificationStore.put(email, code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("게시판 이메일 인증 코드");
        message.setText("인증번호는 " + code + " 입니다.");
        message.setFrom("nawonjin12@naver.com");
        mailSender.send(message);
    }


    @Override
    public boolean verifyCode(VerifyMailReq verifyMailReq) {
        String storedCode = verificationStore.get(verifyMailReq.getEmail());
        return storedCode != null && storedCode.equals(verifyMailReq.getVerifyCode());
    }

}
