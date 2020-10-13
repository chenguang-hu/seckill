package com.example.seckill.controller;

import com.example.seckill.util.TokenManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/notice")
public class ServerNoticeController {

    @RequestMapping("/")
    public void process(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 请求都是/notice/*
        String uri = request.getRequestURI();
        String cmd = uri.substring(uri.lastIndexOf("/") + 1);

        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");

        switch (cmd) {
            case "timeout": {
                String vt = request.getParameter("vt");
                int tokenTimeout = Integer.parseInt(request.getParameter("tokenTimeout"));
                Date expires = TokenManager.timeout(vt, tokenTimeout);
                response.getWriter().write(expires == null ? "" : String.valueOf(expires.getTime()));
                break;
            }
            case "logout": {
                String vt = request.getParameter("vt");
                TokenManager.invalidate(vt);
                response.getWriter().write("true");
                break;
            }
            case "shutdown": {
                TokenManager.destroy();
                response.getWriter().write("true");
                break;
            }
        }
    }
}
