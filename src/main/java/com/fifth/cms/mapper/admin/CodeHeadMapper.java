package com.fifth.cms.mapper.admin;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fifth.cms.model.admin.CodeHeadVO;

@Mapper
public interface CodeHeadMapper {

	public List<CodeHeadVO> selectCodeHeadList(HashMap<String, Object> ObjectJson);

	public CodeHeadVO selectCodeHeadOne(HashMap<String, String> stringJson);

	public CodeHeadVO selectCodeHeadOneforCode(HashMap<String, String> stringJson);

	public String selectCodeHeadMaxCode(HashMap<String, String> stringJson);

	public Integer selectCodeHeadCount(HashMap<String, String> stringJson);

	public Integer insertCodeHead(HashMap<String, String> stringJson);

	public Integer updateCodeHead(HashMap<String, String> stringJson);

	public Integer disableCodeHead(HashMap<String, String> stringJson);

	public Integer restoreCodeHead(HashMap<String, String> stringJson);

	public Integer deleteCodeHead(HashMap<String, String> stringJson);

}
