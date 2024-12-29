package com.fifth.cms.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class loginFailedHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// 실패로직 핸들링

		System.out.println("onAuthenticationFailure 접근");
        exception.printStackTrace();

        writePrintErrorResponse(response, exception);
	}

	 private void writePrintErrorResponse(HttpServletResponse response, AuthenticationException exception) {
	        try {
	            ObjectMapper objectMapper = new ObjectMapper();

	            Map<String, Object> responseMap = new HashMap<>();

	            String message = getExceptionMessage(exception);

	            responseMap.put("status", 401);

	            responseMap.put("message", message);

	            response.getOutputStream().println(objectMapper.writeValueAsString(responseMap));

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    private String getExceptionMessage(AuthenticationException exception) {
	        if (exception instanceof BadCredentialsException) {
				/*return "비밀번호불일치";*/
	        	return "BadCredentialsException";
	        }else if(exception instanceof UsernameNotFoundException) {
				/*return "계정없음";*/
	        	return "UsernameNotFoundException";
	        }else if(exception instanceof AccountExpiredException) {
				/*return "계정만료";*/
	        	return "AccountExpiredException";
	        }else if(exception instanceof CredentialsExpiredException) {
				/*return "비밀번호만료";*/
	        	return "CredentialsExpiredException";
	        }else if(exception instanceof DisabledException) {
				/*return "계정비활성화";*/
	        	return "DisabledException";
	        }else if(exception instanceof LockedException) {
				/*return "계정잠김";*/
	            return "LockedException";
	        } else {
				/*return "확인된 에러가 없습니다.";*/
	            return "other Exception";
	        }
	    }

}
