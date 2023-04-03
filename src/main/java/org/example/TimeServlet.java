package org.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        String timezoneParam = req.getParameter("timezone");
        ZoneId zoneId = timezoneParam != null && !timezoneParam.isEmpty()
                ? ZoneId.of(timezoneParam.replaceAll(" ", "+")) : ZoneId.of("UTC");
        LocalDateTime now = LocalDateTime.now(zoneId);
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        resp.getWriter().write(formattedDateTime + " " + zoneId);
        resp.getWriter().close();
    }
}
