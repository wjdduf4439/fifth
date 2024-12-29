package com.fifth.cms.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/*

	로그인 실패 핸들러의 경우 onAuthenticationFailure 메소드로 인증 시도가 실패할 때 호출됩니다. 메소드의 인자에서 AuthenticationException 예외의 종류로는 아래와 같습니다.

*/
@Component
public class loginDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("No Authorities : " + accessDeniedException);
		System.out.println("Request Uri : { " + request.getRequestURI() + " } ");
		
	}


}
