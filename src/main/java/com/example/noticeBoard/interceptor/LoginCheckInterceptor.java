package com.example.noticeBoard.interceptor;

import com.example.noticeBoard.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws IOException {
        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession(false);

        Member loginMember = (Member) session.getAttribute("loginMember");
        if(session == null || loginMember == null || loginMember.getUsername() == null) {
            log.info("미사용자 요청");
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }

        return true;
    }
}
