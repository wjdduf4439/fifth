package com.fifth.cms.interceptor;

import java.util.HashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fifth.cms.model.login.AccessVO;
import com.fifth.cms.service.login.access.AccessService;
import com.fifth.cms.util.access.AccessInfo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component	
public class AdminInterCeptor implements HandlerInterceptor {
	
	private final AccessInfo accessInfo = new AccessInfo();
	private final AccessService accessService;

	public AdminInterCeptor(AccessService accessService) {
		this.accessService = accessService;
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		// 요청 처리 전에 실행되는 메서드
		// 여기서 refresh token 검증 로직을 추가할 수 있습니다.
		System.out.println(this.getClass().getName() + " : Pre Handle method is Calling");

		HashMap<String, String> accessInfoMap = accessInfo.getAccessInfo(req);

		// refresh token과 code으로 사용자 권한 검증
		AccessVO resultCheckAdmin = accessService.checkAdminOne(accessInfoMap);

		if(null == resultCheckAdmin || resultCheckAdmin.getAuthority() == null) {
			res = accessInfo.sendResponse(res, "인증 정보에 대한 관리자 권한이 없습니다.");
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
