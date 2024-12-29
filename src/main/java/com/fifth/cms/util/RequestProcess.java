package com.fifth.cms.util;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestProcess {

	//private final String[] filterValues = { "`", "$", "%", "^", "*", "@", "+", "|", ";", "<", ">", "\\" };
	
	private final String[] filterXSSValues = { "document", "window", "location", "history", "navigator", "cookie",
			"localStorage", "sessionStorage", "innerHTML", "outerHTML", "src", "href",
			"onload", "onerror", "onmouseover", "onfocus", "onclick", "onkeydown",
			"onkeyup", "onkeypress", "eval", "setTimeout", "setInterval", "alert",
			"confirm\\(", "prompt", "function", "constructor", "object", "Array", 
			"String", "RegExp", "execCommand", "createElement", "appendChild",
			"replaceChild", "insertBefore", ".write", "writeln", "script", "select", "from", "where"
	};

	private static final String CENSOR_HEAD_STRING = "%-";
	private static final String CENSOR_TAIL_STRING = "-%";
	
	public RequestProcess(){

	}

	//검색값 파라미터 필터
	public String getParameterXSSFilter(String key, String input, String type) throws Exception {
		
		boolean replace_check = false; 
		
		String processingInput = "";
		String newInput = "";
		if( null != input && !"".equals(input)) {
			processingInput = input;
			newInput = input;
		} else if( null == input || "".equals(input)) return input;
		
		// 블랙리스트 단어가 포함되어 있는지 확인
        for (String blacklistedWord : filterXSSValues) {

			int startIndex = processingInput.indexOf(blacklistedWord);
			int censoredIndex = processingInput.indexOf(CENSOR_HEAD_STRING + blacklistedWord + CENSOR_TAIL_STRING);
			
			if (startIndex != -1 && censoredIndex == -1) {
				// int endIndex = startIndex + blacklistedWord.length() - 1;
				//newInput = processingInput.substring(0, startIndex) + CENSOR_HEAD_STRING + processingInput.substring(startIndex, endIndex + 1) + CENSOR_TAIL_STRING + processingInput.substring(endIndex + 1);
				newInput = processingInput.replace(blacklistedWord, CENSOR_HEAD_STRING + blacklistedWord + CENSOR_TAIL_STRING);
				replace_check = true;
			}

        }
		
        System.out.println("\t\t RequestProcess doFilterInternal input 결과 : " + key + " : " + newInput + "\t replace_check : " + replace_check);
		if(replace_check) newInput = getParameterXSSFilter(key, newInput, type);

		return newInput;
	}
	
	public String getParameterXSSFilter(String key, String input) throws Exception {
		return getParameterXSSFilter(key, input, "");
	}

	// JSON 문자열 유효성 검사 메소드 추가
	public boolean isJSONValid(String stringValue) {
		try {
			new JSONObject(stringValue);
			return true;
		} catch (Exception e) {
			try {
				new JSONArray(stringValue);
				return true;
			} catch (Exception e2) {
				return false;
			}
		}
	}

}
