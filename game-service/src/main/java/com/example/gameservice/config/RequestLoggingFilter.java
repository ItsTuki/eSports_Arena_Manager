package com.example.gameservice.config;

import jakarta.servlet.*; import jakarta.servlet.http.*; import org.slf4j.*; import org.springframework.stereotype.Component; import java.io.IOException;
@Component
public class RequestLoggingFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request; chain.doFilter(request, response);
        log.info("http method={} path={} status={}", req.getMethod(), req.getRequestURI(), ((HttpServletResponse) response).getStatus());
    }
}
