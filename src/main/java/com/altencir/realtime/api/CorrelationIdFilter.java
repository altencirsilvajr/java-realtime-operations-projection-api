package com.altencir.realtime.api;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
class CorrelationIdFilter extends OncePerRequestFilter {
    static final String ATTRIBUTE = "correlationId";
    private static final String HEADER = "X-Correlation-ID";

    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        var correlationId = request.getHeader(HEADER);
        if (correlationId == null || correlationId.isBlank()) correlationId = UUID.randomUUID().toString();
        request.setAttribute(ATTRIBUTE, correlationId); response.setHeader(HEADER, correlationId); MDC.put(ATTRIBUTE, correlationId);
        try { chain.doFilter(request, response); } finally { MDC.remove(ATTRIBUTE); }
    }
}
