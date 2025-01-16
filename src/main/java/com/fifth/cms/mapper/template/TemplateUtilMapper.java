package com.fifth.cms.mapper.template;
 
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TemplateUtilMapper {
	
	public Integer plusLike(HashMap<String, String> stringJson);
	public Integer plusDislike(HashMap<String, String> stringJson);
	public Integer plusReplyLike(HashMap<String, String> stringJson);
	public Integer plusReplyDislike(HashMap<String, String> stringJson);


}
