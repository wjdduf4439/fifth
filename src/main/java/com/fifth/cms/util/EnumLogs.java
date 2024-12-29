package com.fifth.cms.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public class EnumLogs {

	private Map<String, String> dto = new HashMap<String, String>();
	
	public EnumLogs(HttpServletRequest request, String place) {
		
		//헤더 정보 출력
		showHeaderlog(request);
		
		//Enumeration 클래스가 Enumeration형의 데이터를 request로 받아들여 자동으로 변환을 해준다.
		System.out.println( place + " : /////// ---- enum class log ------------------------------------------------");
		
		 Enumeration params = request.getParameterNames(); 
		 while (params.hasMoreElements()){ 
			 String name = (String)params.nextElement();
			 System.out.println(name + " : " +request.getParameter(name));
		}
		 
		 
		 System.out.println( place + " :  [ END ] ---- enum class log ------------------------------------------------");
		 
	}
	
	public EnumLogs(HttpServletRequest request) {
		new EnumLogs(request, "");
	}
	
	
	public EnumLogs(HttpServletRequest request, Map<String, String> mdto) {
		
		//헤더 정보 출력
		showHeaderlog(request);
		
		//Enumeration 클래스가 Enumeration형의 데이터를 request로 받아들여 자동으로 변환을 해준다.
		
		System.out.println("/////// ---- enum class log with dto ------------------------------------------------");
		
		 Enumeration params = request.getParameterNames(); 
		 while (params.hasMoreElements()){ 
			 String name = (String)params.nextElement();
			 System.out.println(name + " : " + request.getParameter(name));
			 mdto.put(name, request.getParameter(name));
		}
		 
		this.dto = mdto;
		 
		System.out.println("[ END ] ---- enum class log with dto ------------------------------------------------");
		 
	}
	
	public void showHeaderlog(HttpServletRequest request) {
		
		System.out.println("/////// ---- enum header log ------------------------------------------------");
		Enumeration headers = request.getHeaderNames();
		 
		while(headers.hasMoreElements()) {
		    String headerName = (String)headers.nextElement();
		    String value = request.getHeader(headerName);
		    System.out.println("headerName:"+headerName+","+value);
		}
		
		System.out.println("[ END ] ---- enum header log ------------------------------------------------");
		
	}
	
	
}
