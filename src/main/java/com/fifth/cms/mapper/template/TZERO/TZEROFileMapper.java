package com.fifth.cms.mapper.template.TZERO;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fifth.cms.model.template.TemplateZeroFileVO;

@Mapper
public interface TZEROFileMapper {
	
	public TemplateZeroFileVO getFile(HashMap<String, String> valueMap);

	public List<TemplateZeroFileVO> getContextImageFileList(HashMap<String, String> stringJson);
	
	public Integer getUploadedFileCount(HashMap<String, String> stringJson);

	public String selectFileRecordListMax(HashMap<String, String> stringJson);

	public Integer insertFileRecord( HashMap<String, String> stringJson );

	public Integer insertContextImageFile(HashMap<String, String> stringJson);

	public Integer deleteFileRecord( HashMap<String, String> stringJson );

	public Integer deleteContextImageFile(HashMap<String, String> stringJson);

	public Integer deletePostedContextImageFile(HashMap<String, String> stringJson);
	
}
