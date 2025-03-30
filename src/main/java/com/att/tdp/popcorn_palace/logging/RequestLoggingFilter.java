package com.att.tdp.popcorn_palace.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, java.io.IOException {
        
        long startTime = System.currentTimeMillis();
        
        // Log the incoming request
        logger.info("REQUEST: {} {} from {}", 
            request.getMethod(),
            request.getRequestURI(),
            request.getRemoteAddr());

        try {
            // Continue with the request
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Request processing failed: {} {} - Exception: ", 
                request.getMethod(), 
                request.getRequestURI(), 
                e);
            throw e;
        } finally {
            // Log the response
            long duration = System.currentTimeMillis() - startTime;
            if (response.getStatus() >= 500) {
                logger.error("RESPONSE: {} {} - {} ({} ms)", 
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration);
            } else {
                logger.info("RESPONSE: {} {} - {} ({} ms)", 
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration);
            }
        }
    }
} 