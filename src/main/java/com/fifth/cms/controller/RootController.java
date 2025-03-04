package com.fifth.cms.controller;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fifth.cms.util.access.RegistProcess;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api")
public class RootController {

	public RootController() {
		System.out.println("RootController 생성자 호출");
	}	
	
	@ResponseBody
    @RequestMapping(value = "/", method = {RequestMethod.POST, RequestMethod.GET})
    public HashMap<String, Object> personalControllerMethod() {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result",  false );

		resultMap.put("result",  true );

        resultMap.put("message",  "통신 완료" );
		return resultMap;
    }

	@ResponseBody
    @RequestMapping(value = "/checkAccessCode", method = {RequestMethod.POST, RequestMethod.GET})
    public HashMap<String, Object> checkAccessCode(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result",  false );

		String accessId = stringJson.get("accessId");
		String uid = stringJson.get("uid");

		if(accessId != null && uid != null) {
			RegistProcess registProcess = new RegistProcess();
			String hashedCode = registProcess.shaHashing(accessId, uid);
			
            resultMap.put("hashed code : ", hashedCode);
            resultMap.put("result", true);
            resultMap.put("message",  "통신 완료" );
		}
        
		return resultMap;
    }


}
