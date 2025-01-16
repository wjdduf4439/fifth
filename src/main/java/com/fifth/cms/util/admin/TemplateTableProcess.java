package com.fifth.cms.util.admin;

import java.util.HashMap;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Component;

import com.fifth.cms.service.admin.TemplateZeroDDLService;

@Component
public class TemplateTableProcess {
	
	private final TemplateZeroDDLService templateZeroService;

	public TemplateTableProcess(TemplateZeroDDLService templateZeroService) {
		this.templateZeroService = templateZeroService;
	}

	//템플릿 0 - 일반 게시판 생성
	public HashMap<String, Object> createTemplateZeroTable(HashMap<String, String> createTableJson, String processName) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);

		System.out.println("TemplateTableProcess createTemplateZeroTable : " + processName + " 전 정보 : " + createTableJson.toString());
			
			try {
				templateZeroService.createTemplateZeroPostTable(createTableJson);
				templateZeroService.createTemplateZeroContextTable(createTableJson);
				templateZeroService.createTemplateZeroFileTable(createTableJson);
				templateZeroService.createTemplateZeroReplyTable(createTableJson);
				templateZeroService.createTemplateZeroContentFileTable(createTableJson);
			} catch (NullPointerException e) {
				resultMap.put("message", processName + " 작업에 실패 > 예외 : " + e.getMessage()); return resultMap;
			} catch (IllegalArgumentException e) {
				resultMap.put("message", processName + " 작업에 실패 > 예외 : " + e.getMessage()); return resultMap;	
			} catch (PersistenceException e) {
				resultMap.put("message", processName + " 작업에 실패 > 예외 : " + e.getMessage()); return resultMap;	
			} catch (Exception e) {
				resultMap.put("message", processName + " 작업에 실패 > 예외 : " + e.getMessage()); return resultMap;
			}

		resultMap.put("result", true);
		resultMap.put("message", processName + " 작업에 성공");
		return resultMap;
	}

	//템플릿 0 - 일반 게시판 삭제
	public Integer dropTemplateZeroTable(HashMap<String, String> dropTableJson) {

		int result = 0;
		
		int processOne = templateZeroService.dropTemplateZeroPostTable(dropTableJson);
		int processTwo = templateZeroService.dropTemplateZeroContextTable(dropTableJson);
		int processThree = templateZeroService.dropTemplateZeroFileTable(dropTableJson);
		int processFour = templateZeroService.dropTemplateZeroReplyTable(dropTableJson);
		int processFive = templateZeroService.dropTemplateZeroContentFileTable(dropTableJson);

		if(processOne > 0 && processTwo > 0 && processThree > 0 && processFour > 0 && processFive > 0) result = 1;

		return result;
	}

}
