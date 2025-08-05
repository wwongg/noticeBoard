package com.example.noticeBoard.controller;

import com.example.noticeBoard.Dto.LoginDto;
import com.example.noticeBoard.entity.Member;
import com.example.noticeBoard.service.Impl.MemberServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberServiceImpl memberService;


    @GetMapping("/login")
    public String getLogin(HttpServletRequest request, Model model) {

        String referer = request.getHeader("Referer");
        request.getSession().setAttribute("referer", referer);

        request.getSession().setAttribute("prevPage", referer);
        log.info("uri={}", referer);
        model.addAttribute("login", new LoginDto());
        return "member/login";
    }


    @PostMapping("/login")
    public String postLogin(@ModelAttribute("login") LoginDto loginDto,
                            HttpServletRequest request, HttpSession session, Model model) {

        boolean login = memberService.login(loginDto);  // 로그인 정보가 일치할 때 true

        if(login) {
            String username = loginDto.getUsername();
            Member member = memberService.findByUsername(username);
            session.setAttribute("loginMember", member);    // 세션에 멤버 변수 저장

            String prevPage = (String) request.getSession().getAttribute("prevPage");
            String rePrePage = (String) request.getSession().getAttribute("referer");
            request.getSession().removeAttribute("prevPage");

            return "redirect:" + (prevPage.equals("/") ? rePrePage : prevPage);
        }

        model.addAttribute("error", "비밀번호 또는 아이디가 올바르지 않습니다.");
        return "member/login";
    }


    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loginMember");
        return "redirect:/";
    }


}
