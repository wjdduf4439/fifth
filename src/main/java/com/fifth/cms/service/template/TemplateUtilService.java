package com.fifth.cms.service.template;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.fifth.cms.mapper.template.TemplateUtilMapper;

@Service
public class TemplateUtilService {

	private final TemplateUtilMapper templateUtilMapper;
	
	public TemplateUtilService(TemplateUtilMapper templateUtilMapper) {
		this.templateUtilMapper = templateUtilMapper;
	}

	public Integer updateLikeOptionPath(HashMap<String, String> stringJson) {
		return templateUtilMapper.updateLikeOptionPath(stringJson);
	}

	public Integer plusLike(HashMap<String, String> stringJson) {
		return templateUtilMapper.plusLike(stringJson);
	}

	public Integer plusDislike(HashMap<String, String> stringJson) {
		return templateUtilMapper.plusDislike(stringJson);
	}

	public Integer plusReplyLike(HashMap<String, String> stringJson) {
		return templateUtilMapper.plusReplyLike(stringJson);
	}

	public Integer plusReplyDislike(HashMap<String, String> stringJson) {
		return templateUtilMapper.plusReplyDislike(stringJson);
	}

}
