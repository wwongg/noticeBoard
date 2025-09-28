package com.example.noticeBoard.controller;

import com.example.noticeBoard.Dto.LoginDto;
import com.example.noticeBoard.Dto.MemberDto;
import com.example.noticeBoard.entity.Member;
import com.example.noticeBoard.service.Impl.MemberServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

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

    @GetMapping("/joinMember")
    public String createMember(Model model) {
        model.addAttribute("member", new MemberDto());
        return "member/joinMember";
    }

    @PostMapping("/joinMember")
    public String createMemberPost(@Valid @ModelAttribute("member") MemberDto memberDto, BindingResult
            bindingResult,Model model) {

        if (bindingResult.hasErrors()) {
            return "member/joinMember";
        }

        if(!memberDto.getPassword().equals(memberDto.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "error.memberDto", "비밀번호가 일치하지 않습니다.");
            return "member/joinMember";
        }

        try {
            memberService.saveDto(memberDto);
        } catch (Exception e) {
            model.addAttribute("error", "회원가입 처리 중 오류 발생");
            return "member/joinMember";
        }

        return "redirect:/";

    }



    @GetMapping("/member/resetPassword")
    public String getResetPassword(@RequestParam("email") String email, Model model) {

        model.addAttribute("email", email);
        return "member/resetPassword";

    }

    @PostMapping("/member/resetPassword")
    public String postResetPassword(@RequestParam("email") String email, @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword,
                                    Model model) {

        if(!newPassword.equals(confirmPassword)) {
            model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
            return "member/resetPassword";
        }

        try {
            memberService.resetPassword(email, newPassword);
            model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
            return "redirect:/";
        } catch (Exception e) {
            log.error("비밀번호 변경 실패", e);
            model.addAttribute("message", "비밀번호 변경 중 오류가 발생했습니다.");
        }

        return "member/resetPassword";
    }



}
