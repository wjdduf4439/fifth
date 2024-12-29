package com.fifth.cms.filter; 

import java.io.IOException;

import org.json.JSONException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import com.fifth.cms.util.DownloadRequest;
import com.fifth.cms.util.JsonRequest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
OncePerRequestFilter는 Spring Framework에서 제공하는 필터의 일종으로, 주로 웹 애플리케이션에서 HTTP 요청을 처리하는 데 사용됩니다. 이 필터는 이름에서 알 수 있듯이 각 HTTP 요청마다 한 번만 실행되는 것을 보장합니다.
*/
public class XssFilter extends OncePerRequestFilter {

	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String nowContentType = request.getContentType() != null ? request.getContentType() : "";

		//System.out.println("XssFilter 접근");
		JsonRequest jsonRequest = null;
		DownloadRequest downloadRequest = null;

		if("application/json".equalsIgnoreCase(nowContentType) || nowContentType.contains("multipart/form-data")){
			try {
				
				// multipart 요청인 경우 추가 작업 실행, 이 작업을 하지 않으면 파일 정보가 이관되지 않음
				HttpServletRequest processedRequest = request;
				if (request.getContentType() != null && request.getContentType().contains("multipart/form-data")) {
					StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
					// 원시 데이터를 파싱하여 사용하기 쉬운 형태로 변환
					processedRequest = resolver.resolveMultipart(request);
				}

				jsonRequest = new JsonRequest(processedRequest);
			
				jsonRequest.printRequestParameters(processedRequest);
				jsonRequest.printAllParameters();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println(this.getClass().getName() + " 예외 발생 : " + e.getMessage());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(this.getClass().getName() + " 예외 발생 : " + e.getMessage());
			}
			
			//System.out.println("XssFilter 접근 종료");
			// Proceed with the modified request
			filterChain.doFilter(jsonRequest, response);

		}else{

			try {
				downloadRequest = new DownloadRequest(request);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(this.getClass().getName() + " 예외 발생 : " + e.getMessage());
			}
			
			//다운로드 로직일 경우
			filterChain.doFilter(downloadRequest, response);
		}

		
	}
	
	
	
}




 