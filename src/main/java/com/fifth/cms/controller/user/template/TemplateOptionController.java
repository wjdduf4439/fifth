package com.fifth.cms.controller.user.template;

import java.util.HashMap;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fifth.cms.model.admin.CodeHeadVO;
import com.fifth.cms.service.admin.CodeHeadService;
import com.fifth.cms.util.CodeProcess;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@RequestMapping("/api/template")
@Controller
public class TemplateOptionController {

	private final Environment environment;
	private final CodeHeadService codeHeadService;

	public TemplateOptionController(Environment environment, CodeHeadService codeHeadService) {
		this.environment = environment;
		this.codeHeadService = codeHeadService;
	}
	
	@ResponseBody
	@RequestMapping(value="/codeHead/{processMark:one}", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> getCodeHeadInfo(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark) {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);

		CodeProcess codeProcess = new CodeProcess(environment);

		CodeHeadVO codeHeadVO = codeHeadService.selectCodeHeadOneforCode(stringJson);
		codeHeadVO.setOptionContent(codeProcess.optionPathRead(stringJson, codeHeadVO.getCode()));

		if("Y".equals(codeHeadVO.getDel_chk())) {
			resultMap.put("message", "비활성화된 게시판입니다.");
			return resultMap;
		}

		resultMap.put("resultList", codeHeadVO);
		resultMap.put("result", true);

		return resultMap;
	}

}
