package org.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneId;

@WebFilter(urlPatterns = "/time")
public class TimezoneValidateFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String timezoneParam = req.getParameter("timezone");
        if (timezoneParam == null || timezoneParam.isEmpty()) {
            chain.doFilter(req, res);
            return;
        }

        try {
            ZoneId.of(timezoneParam.replaceAll(" ", "+"));
        } catch (DateTimeException e) {
            res.setContentType("text/html; charset=utf-8");
            res.setStatus(400);
            res.getWriter().write("Invalid timezone");
            res.getWriter().close();
            return;
        }
        chain.doFilter(req, res);
    }
}
