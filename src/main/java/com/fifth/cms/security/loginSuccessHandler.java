package com.fifth.cms.security;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fifth.cms.service.login.access.AccessService;
import com.fifth.cms.util.JwtUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class loginSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${server.servlet.context-path}")
    private String rootContextPath;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
            
            System.out.println("loginSuccessHandler 접근");
            /*
            일시적으로 세션에 아이디를 부여하는 절차, 로그인 성공시에만 되기 때문에, 일회성이 강하다
            */
            
            HttpSession session = request.getSession();
            
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            if (requestAttributes != null) {
                request = requestAttributes.getRequest();
                session = request.getSession();
                session.setAttribute("id", authentication.getPrincipal());
            }
            
            MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
            MediaType jsonMimeType = MediaType.APPLICATION_JSON;

            // JSONResult jsonResult = JSONResult.success(securityUser); 
            
			// JWT 토큰 생성
			String token = JwtUtil.generateAccessToken(authentication.getName(), authentication.getAuthorities().toString(), request.getRemoteAddr(), request.getHeader("User-Agent"));

            // 페이지 이동을 위한 csrf 토큰 재발급            
            HashMap<String, Object> stringJson = new HashMap<String, Object>();
            stringJson.put("greeting", authentication.getName() + "님 반갑습니다.");
            stringJson.put("result", true);
            stringJson.put("code", "200");
			stringJson.put("token", token); // JWT 토큰 추가
            
            // CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            // // 응답 헤더에 CSRF 토큰을 설정
            // if(null != csrfToken.getHeaderName()){
            //     // CSRF 토큰의 이름(헤더 이름)과 값을 응답 헤더에 설정 -> 클라이언트 측에서 이 헤더를 읽어 CSRF 토큰을 알 수 있게 됨.
            //     response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
            //     stringJson.put("csrfToken_HeaderName", csrfToken.getHeaderName());
            //     stringJson.put("csrfToken_getToken", csrfToken.getToken());
            // }
            
            if (jsonConverter.canWrite(stringJson.getClass(), jsonMimeType)) {
                jsonConverter.write(stringJson, jsonMimeType, new ServletServerHttpResponse(response));
            }
            
            // 리다이렉션 제거
            // response.sendRedirect(rootContextPath);
    }
}
