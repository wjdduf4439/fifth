package com.fifth.cms.security.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fifth.cms.model.login.AccessVO;
import com.fifth.cms.model.login.FifthAuthenticationToken;
import com.fifth.cms.service.login.access.AccessService;
import com.fifth.cms.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JsonAuthenticationFilter는 JSON 형식의 로그인 요청을 처리하는 필터입니다.
 * AbstractAuthenticationProcessingFilter를 확장하여 특정 URL 패턴에 대한 인증을 처리합니다.
 */
@Component
public class JsonAuthentication extends AbstractAuthenticationProcessingFilter {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final BCryptPasswordEncoder passwordEncoder;
	private final AccessService accessService;

	/**
	 * JsonAuthenticationFilter 생성자.
	 * "/loginHome.go" 경로로 POST 요청이 들어올 때 이 필터가 작동합니다.
	 * 
	 * @param authenticationManager 인증을 처리할 AuthenticationManager 객체
	 * @param passwordEncoder       비밀번호 인코더
	 *                              @Qualifier("authenticationManagerBean") 어노테이션을
	 *                              사용하여 특정 AuthenticationManager 빈을 주입합니다.
	 *                              여러 개의 동일한 타입의 빈이 존재할 때, @Qualifier를 사용하여 주입할 빈을
	 *                              명시적으로 지정할 수 있습니다.
	 */
	public JsonAuthentication(@Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager, BCryptPasswordEncoder passwordEncoder, AccessService accessService) {
		super(new AntPathRequestMatcher("/api/accLogin.go", "POST"));
		setAuthenticationManager(authenticationManager);
		this.passwordEncoder = passwordEncoder;
		this.accessService = accessService;
	}

	/**
	 * 인증 시도 메소드.
	 * 요청에서 JSON 형식의 사용자 이름과 비밀번호를 읽어와 인증을 시도합니다.
	 * 
	 * @param request  HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @return Authentication 객체
	 * @throws AuthenticationException 인증 예외
	 * @throws IOException             입출력 예외
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {

		// 요청 본문에서 JSON 데이터를 읽어와 Map으로 변환
		Map<String, String> credentials = objectMapper.readValue(request.getInputStream(), Map.class);
		String username = credentials.get("id");
		String password = credentials.get("pw");

		System.out.println("JsonAuthenticationFilter username : " + username);
		System.out.println("JsonAuthenticationFilter password : " + password);
		System.out.println("JsonAuthenticationFilter crypt password : " + passwordEncoder.encode(password));

		// 인증 요청 객체 생성, 사용자 이름과 비밀번호를 토큰에 담아 전달
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

		// details 객체 설정, 현재 요청에 대한 로그인 정보의 url, 세션 정보 등을 설정
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

		// 인증 관리자에게 인증 요청을 전달하여 인증 처리를 위임
		request.setAttribute("credentials", credentials); // 추가된 부분
		return getAuthenticationManager().authenticate(authRequest);
	}

	/**
	 * 인증 성공 시 호출되는 메소드.
	 * Spring Security의 기본 동작은 인증이 성공하면 기본적으로 리다이렉션을 수행하는 것입니다.
	 * 이를 방지하기 위해 응답 상태를 200으로 설정하고 메시지를 출력합니다.
	 * 
	 * @param request    HttpServletRequest 객체
	 * @param response   HttpServletResponse 객체
	 * @param chain      FilterChain 객체
	 * @param authResult Authentication 객체
	 * @throws IOException      입출력 예외
	 * @throws ServletException 서블릿 예외
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		// 리다이렉션 방지
		response.setStatus(HttpServletResponse.SC_OK);

		// attemptAuthentication에서 설정한 credentials를 가져옴
		FifthAuthenticationToken fifthAuthenticationToken = (FifthAuthenticationToken) authResult;
		Map<String, String> credentials = (Map<String, String>) request.getAttribute("credentials");

		System.out.println("role : " + fifthAuthenticationToken.getAuthorities().toString());

		// JWT 접속 토큰 생성
		String accessToken = JwtUtil.generateAccessToken(fifthAuthenticationToken.getName(), fifthAuthenticationToken.getAuthorities().toString(), request.getRemoteAddr(), request.getHeader("User-Agent"));

		// JWT 리프레시 토큰 생성
		String refreshToken = JwtUtil.generateRefreshToken(fifthAuthenticationToken.getName());

		AccessVO accessVO = new AccessVO();
		accessVO.setUid(fifthAuthenticationToken.getUid());
		accessVO.setRefreshToken(refreshToken);
		accessService.updateRefreshToken(accessVO);

		// JSON 객체 생성
		Map<String, String> jsonResponse = new HashMap<>();
		jsonResponse.put("message", "Authentication successful");
		jsonResponse.put("code", fifthAuthenticationToken.getUid().toString());
		jsonResponse.put("id", credentials.get("id"));
		jsonResponse.put("nick", fifthAuthenticationToken.getNick());
		jsonResponse.put("result", "true");
		jsonResponse.put("accessToken", accessToken);
		jsonResponse.put("refreshToken", refreshToken);

		// JSON 객체를 문자열로 변환
		String jsonResponseString = objectMapper.writeValueAsString(jsonResponse);

		// 응답에 JSON 문자열 작성
		response.getWriter().write(jsonResponseString);
		response.getWriter().flush();
	}

	/**
	 * 인증 실패 시 호출되는 메소드.
	 *
	 * @param request  HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @param failed   AuthenticationException 객체
	 * @throws IOException      입출력 예외
	 * @throws ServletException 서블릿 예외
	 */
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		super.unsuccessfulAuthentication(request, response, failed);
	}
}
