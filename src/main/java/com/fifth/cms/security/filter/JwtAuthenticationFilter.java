package com.fifth.cms.security.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fifth.cms.model.login.AccessVO;
import com.fifth.cms.service.login.access.AccessService;
import com.fifth.cms.util.JwtUtil;
import com.fifth.cms.util.access.AccessInfo;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final AccessInfo accessInfo = new AccessInfo();
	private final AccessService accessService;

	public JwtAuthenticationFilter(AccessService accessService) {
		this.accessService = accessService;
	}

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		
		// 관리자 권한 필요 페이지일 경우 관리자 권한 검증 유도
		if (requestURI.contains("/admin/")) {
			
		} else {
			chain.doFilter(request, response);
			return;
		}

		// access token 만료 검증 후, 만료시 재발급
		HashMap<String, String> accessInfoMap = accessInfo.getAccessInfo(request);
		boolean isTokenValid = JwtUtil.isJwtTokenExpired(accessInfoMap.get("accessToken"));
		if (!isTokenValid) {

			System.out.println(this.getClass().getName() + " Access token이 만료되었습니다.");
			// res = accessInfo.sendResponse(res, "Access token이 만료되었습니다.");
			// return false;

			AccessVO resultVO = accessService.selectAccessOneforUid(accessInfoMap.get("accessCode"));
			try {
				String token = JwtUtil.generateAccessToken(resultVO.getId(), resultVO.getAuthorities().toString(), request.getRemoteAddr(), request.getHeader("User-Agent"));
				System.out.println(this.getClass().getName() + " token : " + token);
				accessInfoMap.put("accessToken", token);

				//재발급 후 헤더에 전달
				response.setHeader("accessToken", accessInfoMap.get("accessToken"));
			} catch (NullPointerException e) {
				response = unauthenticatedAccessToken(response, accessInfoMap, "접속을 인증하는 도중 문제가 발생했습니다 - 4437");
				return;
			}

		} else {
			response.setHeader("accessToken", accessInfoMap.get("accessToken"));
		}

        System.out.println("JwtAuthenticationFilter 접근 - " + request.getRequestURL() + " - " + request.getMethod());
        String header = accessInfoMap.get("accessToken");
        String token = null;
        String username = null;
        String ip = null;
        String userAgent = null;
        String authorities_string = null;
        List<SimpleGrantedAuthority>  authorities = null;

        System.out.println("header : " + header);

		//Bearer 검증 시에는 Authorization 헤더에 있는 문자열을 사용
        if (header != null && request.getHeader("Authorization").startsWith("Bearer ")) {
            token = header;
            try {
                Claims claims = JwtUtil.validateToken(token);
                username = claims.getSubject();
                ip = claims.get("ip", String.class);
                userAgent = claims.get("userAgent", String.class);
                authorities_string = claims.get("authorities", String.class);

                System.out.println("username : " + username);
                System.out.println("ip : " + ip);
                System.out.println("userAgent : " + userAgent);
                System.out.println("authorities_string : " + authorities_string);

				// authorities_string을 SimpleGrantedAuthority 리스트로 변환
                ObjectMapper objectMapper = new ObjectMapper();
                authorities_string = authorities_string.replace("[", "[\"").replace("]", "\"]");
                List<String> authoritiesList = objectMapper.readValue(authorities_string, new TypeReference<List<String>>() {});
                authorities = authoritiesList.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
					
				System.out.println("authorities : " + authorities);


            } catch (Exception e) {
                System.out.println(this.getClass().getName() + " : Invalid JWT Token");
				e.printStackTrace();
            }
        }

        if (ip == null || !DigestUtils.sha384Hex(request.getRemoteAddr()).equals(ip)) {
            // filter 응답에 메시지 담기
            response = unauthenticatedAccessToken(response, accessInfoMap, "접속을 인증하는 도중 문제가 발생했습니다 - 4439");
            return;
        }

		// 다른 기기간 로그인 관련 옵션인데, 모바일/pc 도중 한쪽에 로그인을 하고 다른쪽에 웹으로 접속하면 인증절차가 안되는 문제점이 있음
		// 이 부분 관련해서는 주목해보고 다른 방식을 생각해봐야함
		// if(userAgent == null || !userAgent.equals(request.getHeader("User-Agent"))) {
		// 	response = unauthenticatedAccessToken(response, accessInfoMap, "접속을 인증하는 도중 문제가 발생했습니다 - 4440");
		// 	return;
		// }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        chain.doFilter(request, response);
    }


	public HttpServletResponse unauthenticatedAccessToken(HttpServletResponse response, HashMap<String, String> accessInfoMap, String message) throws IOException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("message", message);
		ObjectMapper objectMapper = new ObjectMapper();
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
		response.getWriter().flush();

		return response;
	}
}