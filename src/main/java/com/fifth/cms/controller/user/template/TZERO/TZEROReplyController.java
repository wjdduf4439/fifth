package com.fifth.cms.controller.user.template.TZERO;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fifth.cms.model.template.TemplateZeroReplyVO;
import com.fifth.cms.service.template.TZERO.TZEROReplyService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user/template/tzero/reply")
public class TZEROReplyController {
	
	private final TZEROReplyService tZEROreplyService;

	public TZEROReplyController(TZEROReplyService tZEROreplyService) {
		this.tZEROreplyService = tZEROreplyService;
	}

	@ResponseBody
	@RequestMapping(value = "/{processMark:list|count}", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> list(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark ) throws Exception {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);

		if("list".equals(processMark)) {
			
			List<TemplateZeroReplyVO> resultList = tZEROreplyService.getReplyList(stringJson);
			resultMap.put("resultList", resultList);

		}else if("count".equals(processMark)) {
			Integer resultCount = tZEROreplyService.selectReplyCount(stringJson);
			resultMap.put("resultCount", resultCount);
		}

		resultMap.put("result", true);

		return resultMap;

	}

}
