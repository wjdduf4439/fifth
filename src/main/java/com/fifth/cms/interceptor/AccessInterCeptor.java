package com.fifth.cms.interceptor;

import java.util.HashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fifth.cms.service.login.access.AccessService;
import com.fifth.cms.util.access.AccessInfo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AccessInterCeptor implements HandlerInterceptor {

	private final AccessInfo accessInfo = new AccessInfo();
	private final AccessService accessService;

	public AccessInterCeptor(AccessService accessService) {
		this.accessService = accessService;
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		// 요청 처리 전에 실행되는 메서드
		// 여기서 refresh token 검증 로직을 추가할 수 있습니다.
		System.out.println(this.getClass().getName() + " : Pre Handle method is Calling");

		HashMap<String, String> accessInfoMap = accessInfo.getAccessInfo(req);

		if(
			accessInfoMap == null ||
			accessInfoMap.get("ip") == null ||
			null == accessInfoMap.get("accessCode") ||
			null == accessInfoMap.get("accessToken") || 
			null == accessInfoMap.get("refreshToken") ||
			null == accessInfoMap.get("accessId") ||
			null == accessInfoMap.get("nick") ||
			null == accessInfoMap.get("role")
		) {
			res = accessInfo.sendResponse(res, "해당하는 사용자의 인증 정보가 없습니다.");
			return false;
		} else {
			System.out.println(this.getClass().getName() + " accessInfoMap : " + accessInfoMap.toString());
			accessInfoMap.put("role", "ROLE_" + accessInfoMap.get("role"));
		}

		// refresh token과 code으로 사용자 계정 검증
		Integer resultCheckAccess = accessService.checkAccess(accessInfoMap);
		System.out.println(this.getClass().getName() + " resultCheckAccess : " + resultCheckAccess);

		if(resultCheckAccess == 0) {
			res = accessInfo.sendResponse(res, "인증 정보가 맞지 않습니다.");
			return false;
		}


		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler, ModelAndView modelAndView) throws Exception {
		//System.out.println(this.getClass().getName() + " : Post Handle method is Calling");
	}

	public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception exception) throws Exception {
		// 요청 완료 후 실행되는 메서드
		//System.out.println(this.getClass().getName() + " : Request and Response is completed");
	}

}
