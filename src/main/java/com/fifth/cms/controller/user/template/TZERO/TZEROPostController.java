package com.fifth.cms.controller.user.template.TZERO;

import java.util.HashMap;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fifth.cms.model.template.TemplateZeroVO;
import com.fifth.cms.service.template.TZERO.TZEROPostService;
import com.fifth.cms.util.template.CookieProcess;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user/template/tzero/post")
public class TZEROPostController {

	private final Environment environment;
	private final TZEROPostService tZEROPostService;

	public TZEROPostController(Environment environment, TZEROPostService tZEROpostService) {
		this.environment = environment;
		this.tZEROPostService = tZEROpostService;
	}
	
	@ResponseBody
	@RequestMapping(value = "/{processMark:list|one|count|viewLikeDislike|notice}", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> list(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);

		if("list".equals(processMark)) {
			
			List<TemplateZeroVO> resultList = tZEROPostService.getPostList(stringJson);
			resultMap.put("resultList", resultList);

		}else if("one".equals(processMark)) {

			TemplateZeroVO codeHeadVO = tZEROPostService.getPost(stringJson);
			resultMap.put("resultList", codeHeadVO);

		}else if("viewLikeDislike".equals(processMark)) {

			TemplateZeroVO codeHeadVO = tZEROPostService.getViewLikeDislikePost(stringJson);
			resultMap.put("resultList", codeHeadVO);

		}else if("count".equals(processMark)) {

			Integer count = tZEROPostService.selectPostCount(stringJson);
			resultMap.put("resultCount", count);

		}else if("notice".equals(processMark)) {

			List<TemplateZeroVO> resultList = tZEROPostService.getNoticePostList(stringJson);
			resultMap.put("resultList", resultList);

		}

		resultMap.put("result", true);
		

		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = "/{processMark:viewNum}", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> process(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark ) throws Exception {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);

		Integer result = 0;
		// System.out.println("TZEROPostController process 작업 전 정보 : " + stringJson.toString());

		CookieProcess cookieProcess = new CookieProcess();
		boolean doProgress = cookieProcess.setTemplateCookie(req, res, stringJson);

		if (doProgress && "viewNum".equals(processMark)) {
			result = tZEROPostService.plusViewNum(stringJson);	
		}
		if (result > 0) {
			// 쿠키 생성 (24시간 유효)
			res = cookieProcess.setCookie(res, stringJson.get("codeHead") + "_post_view_" + stringJson.get("uid"));
			resultMap.put("result", true);
		}

		return resultMap;

	}

}
