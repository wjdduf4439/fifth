package com.fifth.cms.controller.admin;

import java.util.HashMap;
import java.util.List;

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
import com.fifth.cms.util.admin.TemplateTableProcess;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/api/admin/codeHead")
@Controller
public class CodeHeadController {

	private final CodeHeadService codeHeadService;
	private final Environment environment;
	private final TemplateTableProcess templateTableProcess;


	public CodeHeadController(CodeHeadService codeHeadService, Environment environment, TemplateTableProcess templateTableProcess) {
		this.codeHeadService = codeHeadService;
		this.environment = environment;
		this.templateTableProcess = templateTableProcess;
	}		


	@ResponseBody
	@RequestMapping(value = "/{processMark:list|one|count}", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> list(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);

		if("list".equals(processMark)) {
			List<CodeHeadVO> codeHeadList = codeHeadList = codeHeadService.selectCodeHeadList(stringJson);
			resultMap.put("resultList", codeHeadList);
		}else if("one".equals(processMark)) {
			CodeProcess codeProcess = new CodeProcess(environment);
			CodeHeadVO codeHeadVO = codeHeadService.selectCodeHeadOne(stringJson);

			codeHeadVO.setOptionContent(codeProcess.optionPathRead(stringJson, codeHeadVO.getCode()));

			resultMap.put("resultList", codeHeadVO);
		}else if("count".equals(processMark)) {
			Integer count = codeHeadService.selectCodeHeadCount(stringJson);
			resultMap.put("resultCount", count);
		}

		resultMap.put("result", true);
		

		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = "/{processMark:insert|update|disable|restore|delete}", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> process(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark ) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);
		String processName = "";
		if("insert".equals(processMark)) {
			processName = "입력";
		}else if("update".equals(processMark)) {
			processName = "수정";
		}else if("delete".equals(processMark) && "N".equals(stringJson.get("del_chk"))) {
			processName = "비활성화";
		}else if("delete".equals(processMark) && "Y".equals(stringJson.get("del_chk"))) {
			processName = "삭제";
		}else if("restore".equals(processMark)) {
			processName = "복구";
		}

		Integer result = 0;
		System.out.println("CodeHeadController process : " + processName + " 전 정보 : " + stringJson.toString());

		if("insert".equals(processMark)) {

			CodeProcess codeProcess = new CodeProcess(environment);

			stringJson.put("optionPath", codeProcess.optionPathJsonCreate(stringJson));

			if("TZERO".equals(stringJson.get("templateType")))  templateTableProcess.createTemplateZeroTable(stringJson, processName); 

			result = codeHeadService.insertCodeHead(stringJson);
		}else if("update".equals(processMark)) {

			CodeProcess codeProcess = new CodeProcess(environment);
			codeProcess.optionPathJsonUpdate(stringJson, stringJson.get("optionContent"));

			result = codeHeadService.updateCodeHead(stringJson);
		}else if("restore".equals(processMark)) {
			result = codeHeadService.restoreCodeHead(stringJson);
		}else if("delete".equals(processMark)) {

			CodeProcess codeProcess = new CodeProcess(environment);

			if("N".equals(stringJson.get("del_chk"))) result = codeHeadService.disableCodeHead(stringJson);
			else if("Y".equals(stringJson.get("del_chk"))) {

				//codeProcess.optionPathJsonDelete(stringJson);
				codeProcess.storagePathDropStatusUpdate(stringJson);

				templateTableProcess.dropTemplateZeroTable(stringJson);
				result = codeHeadService.deleteCodeHead(stringJson);
			}
		} 

		if (result > 0) {
			resultMap.put("result", true);
			resultMap.put("message", processName + " 작업에 성공하였습니다.");
		}

		return resultMap;

	}

	
}
