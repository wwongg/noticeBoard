package com.example.noticeBoard.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    private static final String LOG_ID = "LogId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                            Object handler) throws Exception{
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString().substring(0, 5);

        request.setAttribute(LOG_ID, uuid);

        log.info("요청 [{}] [{}] [{}] [{}]",uuid,requestURI, request.getDispatcherType(),handler);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);

        log.info("응답 [{}] [{}] [{}] [{}]",logId,requestURI,request.getDispatcherType(),handler);

        if (ex!=null){
            log.error("에러발생 error!!",ex);
        }
    }
}
