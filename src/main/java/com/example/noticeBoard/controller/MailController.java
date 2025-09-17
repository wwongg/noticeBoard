package com.example.noticeBoard.controller;


import com.example.noticeBoard.Dto.PostMailReq;
import com.example.noticeBoard.Dto.VerifyMailReq;
import com.example.noticeBoard.service.Impl.MailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/joinMember")
@RequiredArgsConstructor
public class MailController {

    private final MailServiceImpl mailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody PostMailReq postMailReq) {
        mailService.sendVerificationCode(postMailReq);
        return ResponseEntity.ok("인증번호 발송 완료");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyMail(@RequestBody VerifyMailReq verifyMailReq) {
        boolean result = mailService.verifyCode(verifyMailReq);
        if(result) {
            return ResponseEntity.ok("인증 성공");
        } else {
            return ResponseEntity.badRequest().body("인증 실패");
        }
    }
}
