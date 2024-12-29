package com.fifth.cms.service.admin;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.fifth.cms.mapper.admin.TemplateZeroDDLMapper;

@Service
public class TemplateZeroDDLService {
	
	private final TemplateZeroDDLMapper templateZeroMapper;
	
	public TemplateZeroDDLService(TemplateZeroDDLMapper templateZeroMapper) {
		this.templateZeroMapper = templateZeroMapper;
	}

	public Integer createTemplateZeroPostTable(HashMap<String, String> stringJson) {
		return templateZeroMapper.createTemplateZeroPostTable(stringJson);
	}	

	public Integer createTemplateZeroContextTable(HashMap<String, String> stringJson) {
		return templateZeroMapper.createTemplateZeroContextTable(stringJson);
	}

	public Integer createTemplateZeroFileTable(HashMap<String, String> stringJson) {
		return templateZeroMapper.createTemplateZeroFileTable(stringJson);
	}	

	public Integer createTemplateZeroReplyTable(HashMap<String, String> stringJson) {
		return templateZeroMapper.createTemplateZeroReplyTable(stringJson);
	}	

	public Integer dropTemplateZeroPostTable(HashMap<String, String> stringJson) {
		return templateZeroMapper.dropTemplateZeroPostTable(stringJson);
	}		

	public Integer dropTemplateZeroContextTable(HashMap<String, String> stringJson) {
		return templateZeroMapper.dropTemplateZeroContextTable(stringJson);
	}

	public Integer dropTemplateZeroFileTable(HashMap<String, String> stringJson) {
		return templateZeroMapper.dropTemplateZeroFileTable(stringJson);
	}			

	public Integer dropTemplateZeroReplyTable(HashMap<String, String> stringJson) {
		return templateZeroMapper.dropTemplateZeroReplyTable(stringJson);
	}			

}
