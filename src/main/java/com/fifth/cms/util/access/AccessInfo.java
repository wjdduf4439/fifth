package com.fifth.cms.util.access;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AccessInfo {

	public HashMap<String, String> getAccessInfo(HttpServletRequest req) {

		HashMap<String, String> accessInfo = new HashMap<String, String>();

		accessInfo.put("accessCode"		, req.getHeader("accessCode"));
		accessInfo.put("accessToken"	, req.getHeader("accessToken"));
		accessInfo.put("refreshToken"	, req.getHeader("refreshToken"));
		accessInfo.put("userAgent"		, req.getHeader("User-Agent"));
		accessInfo.put("accessId"		, req.getHeader("accessId"));
		accessInfo.put("nick"			, req.getHeader("nick"));
		accessInfo.put("ip"				, req.getRemoteAddr());

		return accessInfo;
	}

	//인증실패시 response객체에 메시지 전달
	public HttpServletResponse sendResponse(HttpServletResponse res, String message) throws Exception {

        Map<String, String> responseMap = new HashMap<>();

		res.setStatus(HttpServletResponse.SC_OK); // 200 상태 코드 설정
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");


		responseMap.put("message", message);
		responseMap.put("result", "true");

		// ObjectMapper를 사용하여 Map을 JSON 문자열로 변환
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResponse = objectMapper.writeValueAsString(responseMap);

		res.getWriter().write(jsonResponse);

		return res;
	}

}
