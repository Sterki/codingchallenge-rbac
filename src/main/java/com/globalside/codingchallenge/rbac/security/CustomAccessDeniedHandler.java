package com.globalside.codingchallenge.rbac.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globalside.codingchallenge.rbac.model.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exception
    ) throws IOException {

        HttpStatus status = HttpStatus.FORBIDDEN;

        ErrorResponseDto errorResponse = new ErrorResponseDto(
            status.value(),
            "You do not have permission to perform this operation."
        );

        response.setStatus(status.value());
        response.setContentType("application/json");

        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}