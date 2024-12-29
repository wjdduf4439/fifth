package com.fifth.cms.service.admin;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fifth.cms.mapper.admin.CodeHeadMapper;
import com.fifth.cms.model.admin.CodeHeadVO;

@Service
public class CodeHeadService {
	
	private final CodeHeadMapper codeHeadMapper;

	public CodeHeadService(CodeHeadMapper codeHeadMapper) {
		this.codeHeadMapper = codeHeadMapper;
	}
	
	public List<CodeHeadVO> selectCodeHeadList(HashMap<String, String> stringJson) {

		HashMap<String, Object> ObjectJson = new HashMap<String, Object>();
		ObjectJson.put("startPoint", Integer.parseInt(stringJson.get("startPoint")) );
		ObjectJson.put("limit", Integer.parseInt(stringJson.get("limit")) );

		return codeHeadMapper.selectCodeHeadList(ObjectJson);
	}

	public CodeHeadVO selectCodeHeadOne(HashMap<String, String> stringJson) {
		return codeHeadMapper.selectCodeHeadOne(stringJson);
	}

	public CodeHeadVO selectCodeHeadOneforCode(HashMap<String, String> stringJson) {
		return codeHeadMapper.selectCodeHeadOneforCode(stringJson);
	}

	public String selectCodeHeadMaxCode(HashMap<String, String> stringJson) {
		return codeHeadMapper.selectCodeHeadMaxCode(stringJson);
	}

	public Integer selectCodeHeadCount(HashMap<String, String> stringJson) {
		return codeHeadMapper.selectCodeHeadCount(stringJson);	
	}

	public Integer insertCodeHead(HashMap<String, String> stringJson) {
		return 	codeHeadMapper.insertCodeHead(stringJson);
	}

	public Integer updateCodeHead(HashMap<String, String> stringJson) {
		return codeHeadMapper.updateCodeHead(stringJson);
	}		

	public Integer disableCodeHead(HashMap<String, String> stringJson) {
		return codeHeadMapper.disableCodeHead(stringJson);
	}

	public Integer restoreCodeHead(HashMap<String, String> stringJson) {
		return codeHeadMapper.restoreCodeHead(stringJson);
	}

	public Integer deleteCodeHead(HashMap<String, String> stringJson) {
		return codeHeadMapper.deleteCodeHead(stringJson);
	}

}
