package com.fifth.cms.controller.admin;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fifth.cms.model.admin.SiteTypeVO;
import com.fifth.cms.model.admin.SkinTypeVO;
import com.fifth.cms.service.admin.TemplateOptionService;

import jakarta.servlet.http.HttpServletRequest;

//codehead 등록시 필요한 옵션 컨트롤러
@RequestMapping("/admin")
@Controller
public class AdminTemplateOptionController {

	private final TemplateOptionService templateOptionService;
	
	public AdminTemplateOptionController( TemplateOptionService templateOptionService ) {
		
		this.templateOptionService = templateOptionService;
		
	}

	@ResponseBody
	@RequestMapping(value = "/templateType/{processMark:list}", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> TestSiteTpyeList(ModelMap map, HttpServletRequest req, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark ) throws Exception {  
		
		//System.out.println("TestSiteTpyeList stringJson : " + stringJson.toString());
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result",  false );
		
		List<SiteTypeVO> resultList = templateOptionService.getSiteTpyeList(stringJson);
		
		resultMap.put("resultList",  resultList );
		
		resultMap.put("message",  "react project on" );
		resultMap.put("result",  true );
		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = "/skinType/{processMark:list}", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> getSkinTypeList(HttpServletRequest req, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark) throws Exception {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result",  false );

		List<SkinTypeVO> resultList = templateOptionService.getSkinTypeList(stringJson);

		resultMap.put("resultList",  resultList );
		
		resultMap.put("message",  "react project on" );
		resultMap.put("result",  true );
		return resultMap;
	}	

}




 