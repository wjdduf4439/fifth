package com.fifth.cms.controller.layout;

import java.util.HashMap;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fifth.cms.model.admin.MenuVO;
import com.fifth.cms.service.admin.TopMenuService;
import com.fifth.cms.util.admin.MenuUtil;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/topmenu/menu")
@Controller
public class TopMenuController {
	

	private final TopMenuService topMenuService;
	private final MenuUtil menuUtil;
	private final Environment environment;
	
	public TopMenuController( Environment environment, TopMenuService topMenuService, MenuUtil menuUtil ) {
		
		this.environment = environment;
		this.topMenuService = topMenuService;
		this.menuUtil = menuUtil;
	}

	@ResponseBody
	@RequestMapping(value = "/{processMark:list}", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> list(ModelMap map, HttpServletRequest req, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark ) throws Exception {  
		
		//System.out.println("TestCrudMenu stringJson : " + stringJson.toString());
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result",  false );
		
		if("".equals(processMark) || "list".equals(processMark) ) {
			
			List<MenuVO> resultList = topMenuService.getView(stringJson);
			
			resultMap.put("resultList",  resultList );
			
		}
		
		resultMap.put("message",  "react project on" );
		resultMap.put("result",  true );
		return resultMap;
	}

}
