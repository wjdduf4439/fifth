package com.fifth.cms.controller.access;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fifth.cms.service.login.access.AccessService;
import com.fifth.cms.util.access.AccessInfo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping(value = "/api/common")
@Controller
public class AccessController {

	private final AccessService accessService;

	AccessInfo accessInfo = new AccessInfo();

	public AccessController(AccessService accessService) {
		this.accessService = accessService;
	}

	@ResponseBody
	@RequestMapping(value = "/accLogout.go", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> personalControllerMethod(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);

		HashMap<String, String> accessInfoMap = accessInfo.getAccessInfo(req);

		Integer result = accessService.updateBlankRefreshToken(accessInfoMap);

		if (result > 0) {
			resultMap.put("result", true);
			resultMap.put("message", "로그아웃 되었습니다.");
		}
		return resultMap;
	}

}
