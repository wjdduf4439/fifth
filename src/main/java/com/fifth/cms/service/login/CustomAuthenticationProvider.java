package com.fifth.cms.service.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.fifth.cms.model.login.AccessVO;
import com.fifth.cms.model.login.FifthAuthenticationToken;

/*

	AuthenticationProvider 인터페이스:
	
	AuthenticationProvider는 실제 인증 로직을 구현하는 인터페이스입니다.
	authenticate(Authentication authentication) 메서드를 구현하여 사용자의 자격 증명을 확인하고 인증된 Authentication 객체를 반환합니다.
	예를 들어, 사용자의 인증 정보를 데이터베이스에서 가져와 비밀번호를 검증하거나 사용자가 존재하는지 확인하는 등의 작업을 수행할 수 있습니다.
	Spring Security에서 여러 AuthenticationProvider 구현체를 등록하여 다양한 인증 방법을 지원할 수 있습니다.


	간단히 말해서, AuthenticationProvider는 사용자의 인증을 처리하고, UserDetailsService는 사용자 정보를 로드하는 역할을 합니다. 
		일반적으로, AuthenticationProvider는 사용자가 입력한 자격 증명을 확인하고, 
		UserDetailsService는 해당 사용자의 세부 정보를 가져옵니다. 
	두 인터페이스를 함께 사용하여 전체적인 인증 프로세스를 완성할 수 있습니다.


*/

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	/* public class customAuthenticationProvider { */

	@Autowired
	private loginHomeServiceImpl loginHomeServiceImpl;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		/*
		 * 현재 요청에 연결된 Authentication을 얻으려면 SecurityContextHolder.getContext().
		 * getAuthentication()
		 * 으로 얻는다.
		 * SecurityContextHolder.getContext()는 현재 요청에 연결된 SecurityContext를 반환한다.
		 * Authentication.getAuthorities()으로 현재 로그인 한 사용자에게 부여된 권한(GrantedAuthority
		 * 컬렉션)을
		 * 얻을 수 있다.
		 * Authentication.getDetails()는 기본적으로 WebAuthenticationDetails을 반환한다.
		 * 이 클래스에서 IP 주소 및 세션 ID를 얻을 수 있다.
		 * Authentication.getPrincipal ()에서 로그인 사용자 UserDetails를 얻을 수 있다.
		 * Authentication.getCredentials()는 사용자 인증에 이용하는 정보(일반적이라면 로그인 비밀번호)를 반환한다.
		 */
		// WebAuthenticationDetails 객체 가져오기
		WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();

		// UserDetails의 password와 Authentication 객체의 password를 비교한다.
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		// details 객체가 null인지 확인
		if (details != null) {
			System.out.println("[details]");
			System.out.println(
					"  IP Address > Session ID : " + details.getRemoteAddress() + " > " + details.getSessionId());
		} else {
			System.out.println("Details object is null");
		}

		System.out.println("[authentication] : " + authentication.toString());
		System.out.println("[principal username] : " + authentication.getPrincipal());
		System.out.println("[credentials password] : " + authentication.getCredentials().toString());

		// Authentication 객체에서 username과 password를 꺼낸다.
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		// UserDetailsService에서 UserDetails를 꺼낸다, loginHomeServiceImpl에서 등록된 회원의 정보를
		// 가져온다
		AccessVO userDetails = (AccessVO) loginHomeServiceImpl.loadUserByUsername(username);

		System.out
				.println("password <-> userDetails.getPassword() : " + password + " <-> " + userDetails.getPassword());
		System.out.println("최종 인증 결과 : passwordEncoder.matches(password, userDetails.getPassword()) : "
				+ passwordEncoder.matches(password, userDetails.getPassword()));

		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다.");
		}

		// username, password 일치 시 토큰을 생성한다.
		System.out.println("[return] : " + new FifthAuthenticationToken(username, password, userDetails.getAuthorities(), userDetails.getNick(), userDetails.getUid()).toString());
		// return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
		return new FifthAuthenticationToken(username, password, userDetails.getAuthorities(), userDetails.getNick(), userDetails.getUid());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return true;
	}

}
