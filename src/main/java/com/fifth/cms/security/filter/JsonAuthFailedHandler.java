package com.fifth.cms.security.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/*

	로그인 실패 핸들러의 경우 onAuthenticationFailure 메소드로 인증 시도가 실패할 때 호출됩니다. 메소드의 인자에서 AuthenticationException 예외의 종류로는 아래와 같습니다.

*/
@Component
public class JsonAuthFailedHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

		System.out.println("JsonAuthFailedHandler 접근");

        Map<String, Object> data = new HashMap<>();
        data.put("result", false);

        if (exception instanceof BadCredentialsException) {
			response.setStatus(HttpServletResponse.SC_OK);
            data.put("message", "정보가 없거나 인증되지 않은 사용자입니다.");
        } else {
            data.put("message", "인증 실패");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(data));
    }
}
