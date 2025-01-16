package com.fifth.cms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

public class JsonRequest extends HttpServletRequestWrapper{

	private MultipartHttpServletRequest multipartRequest = null;
	private RequestProcess requestProcess;
	HashMap<String, String[]> params;
	/*
	 "!", "(", ")", ":", 제외
	 소괄호는 url encoding 고려해서 넣기
	 */
	//private final String[] filterValues = { "`", "$", "%", "^", "*", "@", "+", "|", ";", "<", ">", "\\" };
	
	// private final String[] filterXSSValues = { "document", "window", "location", "history", "navigator", "cookie",
	// 		"localStorage", "sessionStorage", "innerHTML", "outerHTML", "href",
	// 		"onload", "onerror", "onmouseover", "onfocus", "onclick", "onkeydown",
	// 		"onkeyup", "onkeypress", "eval", "setTimeout", "setInterval", "alert",
	// 		"confirm\\(", "prompt", "function", "constructor", "object", "Array", 
	// 		"String", "RegExp", "execCommand", "createElement", "appendChild",
	// 		"replaceChild", "insertBefore", ".write", "writeln", "script", "select", "from", "where"
	// };


	@SuppressWarnings("unchecked")
	public JsonRequest(HttpServletRequest request) throws JSONException, Exception {

		//먼저 기존 view에서 가져온 request 요청을 매핑한다
		super(request);
		this.requestProcess = new RequestProcess();
		this.params = new HashMap<String, String[]>();
		
		System.out.println("NabSysRequest 생성자 접근 > requesturl : " + request.getRequestURL().toString());
		params.put("requesturl", new String[]{request.getRequestURL().toString()} );

		String line = "";           
		StringBuilder stringBuilder = new StringBuilder();
		String nowContentType = request.getContentType();

		if("application/json".equalsIgnoreCase(nowContentType)) {

			/*
				Content-Type이 JSON인 경우, 요청 본문을 읽어서 처리
				request.getReader을 사용하지 말것, JSON 요청은 request body에 데이터가 있어서 getInputStream()이나 getReader()로 읽어야 함
				JSON은 getParameterNames()나 getParameterValues()로 접근할 수 없음
			*/
			ServletInputStream inputStream = request.getInputStream();	 
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		
			while ((line = reader.readLine()) != null)  stringBuilder.append(line); 
			// 로그 출력
			System.out.println("requesturl : " + request.getRequestURL().toString() + " > contentType : " + request.getContentType() + " process \n\t > messageBody = {}.tostring : " + stringBuilder.toString());

			// JSON 문자열을 파싱하여 Map에 저장
			JSONObject jsonObject = new JSONObject(stringBuilder.toString());
			for (String key : jsonObject.keySet()) {
				
				
				Object value = jsonObject.get(key);
				System.out.println("\tjson 처리 작업 > Key : " + key + ", type : " + value.getClass().getName());
				//System.out.println("\t\tvalue is JSONObject? : " + (value instanceof JSONObject));
				//System.out.println("\t\tvalue is JSONArray? : " + (value instanceof JSONArray));
				//System.out.println("\t\tvalue is String? : " + (value instanceof String));
				//System.out.println("\t\tvalue is Integer? : " + (value instanceof Integer));
				//System.out.println("\t\tvalue is Boolean? : " + (value instanceof Boolean));

				String paramValueString = "";

				//어짜피 json 형식으로 보낸 문자열도 string 형식으로 처리되니까 string 형식으로 받기
				if(value instanceof JSONObject) {
					JSONObject jsonObj = (JSONObject) value;
					
					// JSONObject의 각 필드를 개별 파라미터로 저장
					for(String objKey : jsonObj.keySet()) {
						String paramKey = key + "." + objKey;  // contentVO.uid, contentVO.context 등의 형태로 저장
						Object objValue = jsonObj.get(objKey);
						
						if(objValue instanceof String) {
							this.params.put(paramKey, new String[]{requestProcess.getParameterXSSFilter(paramKey, (String)objValue)});
						} else if(objValue instanceof Integer) {
							this.params.put(paramKey, new String[]{String.valueOf(objValue)});
						} else {
							this.params.put(paramKey, new String[]{objValue.toString()});
						}
					}
				}else if (value instanceof String) {
					// System.out.println("\t json request NabsysRequest key mapping String : " + key + " : " + getParameterXSSFilter(key, (String) jsonObject.getString(key)));
					// System.out.println("\t json request NabsysRequest key mapping String to isJSONValid : " + key + " : " + requestProcess.isJSONValid(value.toString()));

					if(requestProcess.isJSONValid(value.toString())) {
						paramValueString = value.toString();
					}else {
						paramValueString = requestProcess.getParameterXSSFilter(key, (String) jsonObject.getString(key));
					}
					this.params.put(key, new String[]{paramValueString});
				}else if(value instanceof Integer) {
					//System.out.println("\t json request NabsysRequest key mapping Integer : " + key + " : " + (String.valueOf(value)));
					paramValueString = requestProcess.getParameterXSSFilter(key, (String.valueOf(value)));
					this.params.put(key, new String[]{paramValueString});
				}else if(value instanceof Boolean) {
					//System.out.println("\t json request NabsysRequest key mapping Boolean : " + key + " : " + (String.valueOf(value)));
					this.params.put(key, new String[]{value.toString()});
				}else {
					
					//System.out.println("\t json request NabsysRequest key mapping Other Value : " + key + " : " + value.getClass().getName() );
					
				}
				
			}
			
			
		} else if(nowContentType.contains("multipart/form-data")) {	
			if (request instanceof MultipartHttpServletRequest) {
				this.multipartRequest = (MultipartHttpServletRequest) request;
				
				// 일반 폼 파라미터 처리, upload 작업에는 기본적인것들만 포함하기로 한다.
				Enumeration<String> paramNames = this.multipartRequest.getParameterNames();
				while (paramNames.hasMoreElements()) {
					String paramName = paramNames.nextElement();
					String[] values = this.multipartRequest.getParameterValues(paramName);
					if (values != null) {
						String[] filteredValues = new String[values.length];
						for (int i = 0; i < values.length; i++) {
							filteredValues[i] = requestProcess.getParameterXSSFilter(paramName, values[i]);
						}
						this.params.put(paramName, filteredValues);
					}
				}
			}
		}else{
			// 허용되지 않는 요청 형식입니다 메세지를 담아서 거부시키기
			try {
				HttpServletResponse response = (HttpServletResponse) request.getAttribute("response");
				if (response != null) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "허용되지 않는 요청 형식입니다.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("NabSysRequest 생성자 작업 종료");
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
	// 	// 	// json이 아닌 자료형의 블랙리스트 단어가 포함되어 있는지 확인 - 지금은 비활성화
	//     //     for (String blacklistedWord : filterValues_OutJson) {
	        	
	//     //     	//UTF-8로 URL 인코딩
	//     //         String encodedBlacklistedWord = URLEncoder.encode(blacklistedWord, StandardCharsets.UTF_8.name());
	        	
	//     //         if (lowerCaseInput.contains(blacklistedWord)) {
	//     //         	//System.out.println("\t5 - blacklistedWord 감지 문자열 : " + blacklistedWord);
	//     //     		lowerCaseInput = lowerCaseInput.replace(blacklistedWord, "" );
	//     //     		replace_check = true;
	//     //         }
	            
	//     //         if (lowerCaseInput.contains(encodedBlacklistedWord)) {
	//     //         	//System.out.println("\t6 - encodedBlacklistedWord 감지 문자열 : " + encodedBlacklistedWord);
	//     //     		lowerCaseInput = lowerCaseInput.replace(encodedBlacklistedWord, "" );
	//     //     		replace_check = true;
	//     //         }
	//     //     }
	// 	// }
		
	// 	// 블랙리스트 특수문자 단어가 포함되어 있는지 확인 - 지금은 비활성화
	// 	/*
    //     for (String blacklistedWord : filterValues) {
        	
    //     	//UTF-8로 URL 인코딩
    //         String encodedBlacklistedWord = URLEncoder.encode(blacklistedWord, StandardCharsets.UTF_8.name());
        	
    //         if (lowerCaseInput.contains(blacklistedWord)) {
    //         	//System.out.println("\t1 - blacklistedWord 감지 문자열 : " + blacklistedWord);
    //     		lowerCaseInput = lowerCaseInput.replace(blacklistedWord, "" );
    //     		replace_check = true;
    //         }
            
    //         if (lowerCaseInput.contains(encodedBlacklistedWord)) {
    //         	//System.out.println("\t2 - encodedBlacklistedWord 감지 문자열 : " + encodedBlacklistedWord);
    //     		lowerCaseInput = lowerCaseInput.replace(encodedBlacklistedWord, "" );
    //     		replace_check = true;
    //         }
    //     }
	// 	*/
		
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

	// 	//code(codehead 제공 코드)인 key 값일 경우, 대문자료 표기
	// 	if( "code".equals(key) ) lowerCaseInput = lowerCaseInput.toUpperCase();
	// 	//del_chk(삭제여부 코드), visible(표시여부 코드)인 key 값일 경우, 대문자료 표기
	// 	if( "del_chk".equals(key) || "visible".equals(key) ) lowerCaseInput = lowerCaseInput.toUpperCase();
	// 	//templateType(templateType 템플릿 유형 코드), skinType(skinType 스킨 유형 코드)인 key 값일 경우, 대문자료 표기
	// 	if( "templateType".equals(key) || "skinType".equals(key)  ) lowerCaseInput = lowerCaseInput.toUpperCase();

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
