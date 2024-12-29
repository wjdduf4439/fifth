package com.fifth.cms.controller;

import java.util.HashMap;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
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

}
