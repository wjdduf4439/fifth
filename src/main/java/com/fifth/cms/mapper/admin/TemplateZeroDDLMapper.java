package com.fifth.cms.mapper.admin;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TemplateZeroDDLMapper {
	
	public Integer createTemplateZeroPostTable(HashMap<String, String> stringJson);
	public Integer createTemplateZeroContextTable(HashMap<String, String> stringJson);
	public Integer createTemplateZeroFileTable(HashMap<String, String> stringJson);
	public Integer createTemplateZeroReplyTable(HashMap<String, String> stringJson);

	public Integer dropTemplateZeroPostTable(HashMap<String, String> stringJson);
	public Integer dropTemplateZeroContextTable(HashMap<String, String> stringJson);
	public Integer dropTemplateZeroFileTable(HashMap<String, String> stringJson);
	public Integer dropTemplateZeroReplyTable(HashMap<String, String> stringJson);
}
