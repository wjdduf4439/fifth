package com.fifth.cms.security.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fifth.cms.service.login.loginHomeServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class fifthLoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    
    		
	@Autowired
	private loginHomeServiceImpl loginHomeServiceImpl;
	
	public fifthLoginAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
			AuthenticationException {
		Authentication authentication = super.attemptAuthentication(request, response);
		User user = (User) authentication.getPrincipal();
		/*
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		loginHomeVO userDetails =
		        (loginHomeVO) loginHomeServiceImpl.loadUserByUsername(username);
		
		if (user.getUsername().startsWith("test")) {
			// 테스트 유저인 경우 어드민과 유저 권한 모두 부여
			return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
		}
		*/
		
		System.out.println(" thirdLoginAuthenticationFilter 접근 ");
		System.out.println(" authentication.getName() : " + authentication.getName());
		/*
		String accessInfo_id = "logout";
		if(  null != authentication.getName() && !"".equals(authentication.getName())  ) {
			accessInfo_id = authentication.getName();
			request.setAttribute("accessInfo_id", accessInfo_id);
		}
		*/
		
		return authentication;
	}
    
}
