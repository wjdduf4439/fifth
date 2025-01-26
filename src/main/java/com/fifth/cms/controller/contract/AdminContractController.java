package com.fifth.cms.controller.contract;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fifth.cms.model.contract.ContractVO;
import com.fifth.cms.service.contract.ContractService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/api/admin/contract")
@Controller
public class AdminContractController {

	
	private final ContractService contractService;

	public AdminContractController(ContractService contractService) {
		this.contractService = contractService;
	}
	
	@ResponseBody
	@RequestMapping(value = "/{processMark:list|count}", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> list(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);

		if("list".equals(processMark)) {
			List<ContractVO> contractList = contractService.selectContractList(stringJson);
			resultMap.put("resultList", contractList);
		}else if("count".equals(processMark)) {
			Integer count = contractService.selectContractCount(stringJson);
			resultMap.put("resultCount", count);
		}

		resultMap.put("result", true);
		

		return resultMap;
	}

}
