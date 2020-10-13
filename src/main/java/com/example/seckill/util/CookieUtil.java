package com.example.seckill.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static String getCookie(String cookieName, HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void deleteCookie(String cookieName, HttpServletResponse response, String path) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath(path);
        response.addCookie(cookie);
    }
}
