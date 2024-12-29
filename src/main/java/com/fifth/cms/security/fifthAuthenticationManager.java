package com.fifth.cms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.fifth.cms.service.login.loginHomeServiceImpl;

@Component
public class fifthAuthenticationManager implements AuthenticationManager {

	/*
		AuthenticationManager
		
		authenticate(Authentication authentication) 메서드를 구현하여 사용자의 자격 증명을 확인하고 인증된 Authentication 객체를 반환합니다.
		
	*/

	@Autowired
	private loginHomeServiceImpl loginHomeServiceImpl;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		
		System.out.println("로그인 인증절차 thirdAuthenticationManager authenticate 접근");
		
		WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		return null;
	}
	
}
