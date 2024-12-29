package com.fifth.cms.mapper.template.TZERO;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.fifth.cms.model.template.TemplateZeroFileVO;

@Mapper
public interface TZEROFileMapper {
	
	public TemplateZeroFileVO getFile(HashMap<String, String> valueMap);
	public Integer getUploadedFileCount(HashMap<String, String> stringJson);

	public String selectFileRecordListMax(HashMap<String, String> stringJson);

	public Integer insertFileRecord( HashMap<String, String> stringJson );

	public Integer deleteFileRecord( HashMap<String, String> stringJson );
}
