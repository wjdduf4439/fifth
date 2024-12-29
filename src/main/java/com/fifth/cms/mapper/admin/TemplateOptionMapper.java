package com.fifth.cms.mapper.admin;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fifth.cms.model.admin.SiteTypeVO;
import com.fifth.cms.model.admin.SkinTypeVO;

@Mapper
public interface TemplateOptionMapper {


	public List<SiteTypeVO> getSiteTpyeList(HashMap<String, String> stringJson);

	public List<SkinTypeVO> getSkinTypeList(HashMap<String, String> stringJson);

}




