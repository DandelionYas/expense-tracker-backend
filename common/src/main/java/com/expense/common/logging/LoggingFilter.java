package com.expense.common.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.expense.common.utils.Constants.TRUCK_ID;

@Slf4j
@Component
@WebFilter(urlPatterns = "/*")
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        LocalDateTime now = LocalDateTime.now();
        String trackId = UUID.randomUUID().toString();
        request.setAttribute(TRUCK_ID, trackId);
        byte[] requestBody = requestWrapper.getContentAsByteArray();
        log.info("Request: {} {} {}, trackId: {}", request.getMethod(), request.getRequestURI(),
                request.getParameterMap().isEmpty() ? StringUtils.EMPTY : request.getParameterMap(), trackId);
        log.debug("Request Body: {} trackId: {}", new String(requestBody, StandardCharsets.UTF_8), trackId);

        filterChain.doFilter(requestWrapper, responseWrapper);

        LocalDateTime endTime = LocalDateTime.now();
        byte[] responseBody = responseWrapper.getContentAsByteArray();
        log.info("Response: {} {}, Status: {}, Duration: {}ms, trackId: {}", request.getMethod(), request.getRequestURI(),
                response.getStatus(), Duration.between(now, endTime).toMillis(), trackId);
        log.debug("Response Body: {} trackId: {}", new String(responseBody, StandardCharsets.UTF_8), trackId);
    }
}
