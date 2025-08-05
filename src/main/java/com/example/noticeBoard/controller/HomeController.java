package com.example.noticeBoard.controller;

import com.example.noticeBoard.entity.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 어노테이션을 통해 controller임을 명시한다
public class HomeController {
    @GetMapping("/") // /요청이 들어오면 아래의 함수를 실행한다.
    public String home(Model model, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        model.addAttribute("loginMember", loginMember);
        return "home";
    }
}
