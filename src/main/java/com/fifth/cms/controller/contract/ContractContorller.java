package com.fifth.cms.controller.contract;

import java.util.HashMap;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fifth.cms.service.contract.ContractService;
import com.fifth.cms.util.CodeProcess;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/api/contract")
@Controller
public class ContractContorller {
	
	private final Environment environment;
	private final ContractService contractService;

	public ContractContorller(ContractService contractService, Environment environment) {
		this.contractService = contractService;
		this.environment = environment;
	}

	@ResponseBody
	@RequestMapping(value = "/{processMark:insert}", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> process(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark ) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);

		String maxCode = contractService.selectContractMaxCode();

		// 코드 생성
		CodeProcess codeProcess = new CodeProcess(environment);
		String code = codeProcess.createCode(maxCode, "CONT", "");

		stringJson.put("code", code);

		Integer result = contractService.insertContract(stringJson);

		if (result > 0) {
			resultMap.put("result", true);
			resultMap.put("message", "연락을 보냈습니다.");
		}

		return resultMap;

	}
	
}
