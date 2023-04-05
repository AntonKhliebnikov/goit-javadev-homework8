package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@WebServlet(urlPatterns = "/time")
public class TimeServlet extends HttpServlet {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        super.init();
        engine = new TemplateEngine();

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");

        engine.setTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String timezone = req.getParameter("timezone");

        if (timezone == null) {
            timezone = CookieUtils.getLastTimezoneCookie(req);
            if (timezone == null) {
                timezone = "UTC";
            }
        }

        ZoneId zoneId = !timezone.isEmpty()
                ? ZoneId.of(timezone.replaceAll(" ", "+")) : ZoneId.of("UTC");
        LocalDateTime now = LocalDateTime.now(zoneId);

        String formattedDateTime = now.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));

        CookieUtils.setLastTimezoneCookie(resp, timezone);

        Context context = new Context();
        context.setVariable("timezone", zoneId);
        context.setVariable("time", formattedDateTime);

        String html = engine.process("time.html", context);

        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().write(html);
        resp.getWriter().close();
    }
}
