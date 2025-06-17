package com.mooky.pet_diary.global.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mooky.pet_diary.global.ApiResponse;
import com.mooky.pet_diary.global.exception.ApiException;
import com.mooky.pet_diary.global.exception.AuthException;

import io.jsonwebtoken.lang.Collections;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * filters userId from jwt token and sets userId as SpringContextHolder's
 * Authentication
 * 
 * @CurrentUser can be used in any controller method parameter to derive (Long) userId
 * @see AuthException
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {

    private final JwtService jwtService;

    /**
     * @return null if not valid authorization header
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer") || authHeader.length() < 7) {
            return null;
        }
        return authHeader.substring(7);
    }

    /**
     * sets the userId as SecurityContextHolder's authentication, which is later
     * used to find out @CurrentUser
     * 
     * @see CurrentUser
     */
    private void authenticateUser(String token) {
        Long userId = this.jwtService.getUserIdFromToken(token, true);
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId,
                    null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    private void setErrorResponse(HttpServletResponse response, Exception ex)
            throws JsonProcessingException, IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        ApiResponse apiResponse;
        if (ex instanceof ApiException) {
            apiResponse = ApiResponse.error((ApiException) ex);
        } else {
            // TODO error code
            apiResponse = ApiResponse.error(ex.getClass().getSimpleName(), "", ex.getMessage());
        }

        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
        log.warn("[{}] error={}, errorCode={}, errorMessage={}",
                ex.getClass().getSimpleName(), apiResponse.getError(), apiResponse.getErrorCode(), apiResponse.getErrorMessage());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String token = this.extractTokenFromRequest(httpRequest);
        if (token == null) {
            this.setErrorResponse(httpResponse, AuthException.missingJwtToken());
            return;
        }

        try {
            this.authenticateUser(token);
            chain.doFilter(httpRequest, httpResponse);
        } catch (Exception ex) {
            this.setErrorResponse(httpResponse, ex);
        }
    }

}
