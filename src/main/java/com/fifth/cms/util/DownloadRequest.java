package com.fifth.cms.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class DownloadRequest extends HttpServletRequestWrapper{

	HashMap<String, String[]> params;
	private RequestProcess requestProcess;
	
	public DownloadRequest(HttpServletRequest request) throws JSONException, Exception {

		//먼저 기존 view에서 가져온 request 요청을 매핑한다
		super(request);
		this.requestProcess = new RequestProcess();
		this.params = new HashMap<String, String[]>();
		
		System.out.println("DownloadSysRequest 생성자 접근 > requesturl : " + request.getRequestURL().toString());
		params.put("requesturl", new String[]{request.getRequestURL().toString()} );

		// 일반 폼 파라미터 처리, upload 작업에는 기본적인것들만 포함하기로 한다.
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			String[] values = request.getParameterValues(paramName);
			if (values != null) {
				String[] filteredValues = new String[values.length];
				for (int i = 0; i < values.length; i++) {
					filteredValues[i] = requestProcess.getParameterXSSFilter(paramName, values[i]);
				}
				this.params.put(paramName, filteredValues);
			}
		}

		System.out.println("DownloadSysRequest 생성자 작업 종료");
	}
	
	public void printAllParameters() {
		System.out.println("printAllParameters 출력");
		for (Map.Entry<String, String[]> entry : params.entrySet()) {
			String key = entry.getKey();
			String values = Arrays.toString(entry.getValue()) ;
			System.out.println("\tKey: " + key + " | Value(s): " + values);
		}
		System.out.println("printAllParameters 출력종료");
	}
	
	public void printRequestParameters(HttpServletRequest request) {
		Enumeration<String> parameterNames = request.getParameterNames();
		
		System.out.println("printRequestParameters 출력");
		while (parameterNames.hasMoreElements()) {
			String key = parameterNames.nextElement();
			String value = request.getParameter(key);
			System.out.println("\tKey: " + key + ", Value: " + value);
		}
		System.out.println("printRequestParameters 출력종료");
	}

	//검색값 파라미터 필터
	// public String getParameterXSSFilter(String key, String input, String type) throws Exception {
		
	// 	boolean replace_check = false; 
		
	// 	String lowerCaseInput = "";
	// 	if( null != input && !"".equals(input)) lowerCaseInput = input.toLowerCase(); 
		
	// 	// if(!"json".equals(type)) {
	// 	// 	// json이 아닌 자료형의 블랙리스트 단어가 포함되어 있는지 확인
	//     //     for (String blacklistedWord : filterValues_OutJson) {
	        	
	//     //         if (lowerCaseInput.contains(blacklistedWord)) {
	//     //         	//System.out.println("\t5 - blacklistedWord 감지 문자열 : " + blacklistedWord);
	//     //     		lowerCaseInput = lowerCaseInput.replace(blacklistedWord, "" );
	//     //     		replace_check = true;
	//     //         }
	//     //     }
	// 	// }
		
	// 	// 블랙리스트 단어가 포함되어 있는지 확인
    //     for (String blacklistedWord : filterXSSValues) {
        	
    //     	//UTF-8로 URL 인코딩
    //         String encodedBlacklistedWord = URLEncoder.encode(blacklistedWord, StandardCharsets.UTF_8.name());
        	
    //         if (lowerCaseInput.contains(blacklistedWord)) {
    //         	//System.out.println("\t3 - blacklistedWord 감지 문자열 : " + blacklistedWord);
    //     		lowerCaseInput = lowerCaseInput.replace(blacklistedWord, "" );
    //     		replace_check = true;
    //         }
            
    //         if (lowerCaseInput.contains(encodedBlacklistedWord)) {
    //         	//System.out.println("\t4 - encodedBlacklistedWord 감지 문자열 : " + encodedBlacklistedWord);
    //     		lowerCaseInput = lowerCaseInput.replace(encodedBlacklistedWord, "" );
    //     		replace_check = true;
    //         }
    //     }
		
    //     System.out.println("\t\tdoFilterInternal input 결과 : " + key + " : " + lowerCaseInput + "\t replace_check : " + replace_check);
	// 	if(replace_check) lowerCaseInput = getParameterXSSFilter(key, lowerCaseInput, type);

	// 	return lowerCaseInput;
	// }
	
	
	// public String getParameterXSSFilter(String key, String input) throws Exception {
	// 	return getParameterXSSFilter(key, input, "");
	// }

	@Override
	public String getParameter(String name) {
		String[] values = params.get(name);
		return values != null && values.length > 0 ? values[0] : null;
	}

	public void setParameter(String name, String value){
		params.put(name, new String[]{value});
	}


	@SuppressWarnings("unchecked")
	public Map getParameterMap() {
		return Collections.unmodifiableMap(params);
	}

	@SuppressWarnings("unchecked")
	public Enumeration getParameterNames() {
		return Collections.enumeration(params.keySet());        
	}
	@Override
	public String[] getParameterValues(String name) {
		String[] values = params.get(name);
    	return values != null ? values : new String[0];
	}

	// HttpServletRequestWrapper의 메서드를 오버라이드
	@Override
	public String getMethod() {
		return super.getMethod();
	}

}
