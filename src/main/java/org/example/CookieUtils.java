package org.example;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CookieUtils extends HttpServlet {
    public static void setLastTimezoneCookie(HttpServletResponse resp, String timezone) {
        String encodedTimezone = URLEncoder.encode(timezone, StandardCharsets.UTF_8);
        Cookie cookie = new Cookie("lastTimezone", encodedTimezone);
        cookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
        resp.addCookie(cookie);
    }

    public static String getLastTimezoneCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("lastTimezone")) {
                    return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                }
            }
        }
        return null;
    }
}