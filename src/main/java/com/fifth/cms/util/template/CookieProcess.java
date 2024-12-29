package com.fifth.cms.util.template;

import java.util.HashMap;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieProcess {

	public boolean setTemplateCookie(HttpServletRequest req, HttpServletResponse res, HashMap<String, String> stringJson) throws Exception{

		@SuppressWarnings("unchecked")
		HashMap<String, String> cookieProcessMap = (HashMap<String, String>) stringJson.clone();

		String codeHeadId = cookieProcessMap.get("codeHead");
		String postId = cookieProcessMap.get("uid");
		String cookieName = codeHeadId + "_post_view_" + postId;
			
		// 쿠키 확인, 현재 요청(request)에 포함된 모든 쿠키들을 배열로 가져옵니다, 쿠키가 하나도 없다면 null을 반환
		Cookie[] cookies = req.getCookies();
		boolean doProgress = true;
			
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				//쿠키 이름 조회 후 일치하는 쿠키가 있다면 이미 해당 게시물을 조회했다는 의미이므로 hasViewed를 true로 설정
				if (cookieName.equals(cookie.getName())) {
					doProgress = false;
					break;
				}
			}
		}

		return doProgress;
	}

	public HttpServletResponse setCookie(HttpServletResponse res, String cookieName) {
		Cookie cookie = new Cookie(cookieName, "true");
		cookie.setMaxAge(60 * 60 * 24); // 24시간
		cookie.setPath("/");
		res.addCookie(cookie);

		return res;
	}
}
