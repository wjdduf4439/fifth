package com.fifth.cms.mapper.template.TZERO;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fifth.cms.model.template.TemplateZeroReplyVO;

@Mapper
public interface TZEROReplyMapper {
	
	public List<TemplateZeroReplyVO> getReplyList(HashMap<String, Object> ObjectJson);
	
	public Integer selectReplyCount(HashMap<String, String> stringJson);
	
	public Integer insertReply(HashMap<String, String> stringJson);

	public Integer updateReply(HashMap<String, String> stringJson);

	public Integer updateReplyMomRepCode(HashMap<String, String> stringJson);

	public Integer deleteReply(HashMap<String, String> stringJson);
}
