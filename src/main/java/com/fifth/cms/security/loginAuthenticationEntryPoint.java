package com.fifth.cms.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class loginAuthenticationEntryPoint implements AuthenticationEntryPoint   {
	
	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		response.addHeader("message", "ASSU, Welcome");
	    response.sendError(HttpStatus.UNAUTHORIZED.value());
    }
	
}
