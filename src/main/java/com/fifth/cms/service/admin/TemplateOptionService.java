package com.fifth.cms.service.admin;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fifth.cms.mapper.admin.TemplateOptionMapper;
import com.fifth.cms.model.admin.SiteTypeVO;
import com.fifth.cms.model.admin.SkinTypeVO;


@Service
public class TemplateOptionService {

	private final TemplateOptionMapper templateOptionMapper;
	
	public TemplateOptionService( TemplateOptionMapper templateOptionMapper ) {
		
		this.templateOptionMapper = templateOptionMapper;
		
	}
	
	public List<SiteTypeVO> getSiteTpyeList(HashMap<String, String> stringJson) throws Exception {
		// TODO Auto-generated method stub
		return templateOptionMapper.getSiteTpyeList( stringJson );
	}

	public List<SkinTypeVO> getSkinTypeList(HashMap<String, String> stringJson) throws Exception {
		return templateOptionMapper.getSkinTypeList( stringJson );
	}
		
}




 