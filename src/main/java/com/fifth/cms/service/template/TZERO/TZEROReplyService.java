package com.fifth.cms.service.template.TZERO;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fifth.cms.mapper.template.TZERO.TZEROReplyMapper;
import com.fifth.cms.model.template.TemplateZeroReplyVO;

@Service
public class TZEROReplyService {
	
	private final TZEROReplyMapper tZEROreplyMapper;

	public TZEROReplyService(TZEROReplyMapper tZEROreplyMapper) {
		this.tZEROreplyMapper = tZEROreplyMapper;
	}


	public List<TemplateZeroReplyVO> getReplyList(HashMap<String, String> stringJson) {
		
		HashMap<String, Object> ObjectJson = new HashMap<String, Object>();
		ObjectJson.put("startPoint", Integer.parseInt(stringJson.get("startPoint")) );
		ObjectJson.put("uid", Integer.parseInt(stringJson.get("uid")) );
		ObjectJson.put("limit", Integer.parseInt(stringJson.get("limit")) );
		ObjectJson.put("codeHead", stringJson.get("codeHead") );

		return tZEROreplyMapper.getReplyList(ObjectJson);
	}

	public Integer selectReplyCount(HashMap<String, String> stringJson) {
		return tZEROreplyMapper.selectReplyCount(stringJson);
	}

	public Integer insertReply(HashMap<String, String> stringJson) {
		return tZEROreplyMapper.insertReply(stringJson);
	}

	public Integer updateReply(HashMap<String, String> stringJson) {
		return tZEROreplyMapper.updateReply(stringJson);
	}

	public Integer updateReplyMomRepCode(HashMap<String, String> stringJson) {
		return tZEROreplyMapper.updateReplyMomRepCode(stringJson);
	}

	public Integer deleteReply(HashMap<String, String> stringJson) {
		return tZEROreplyMapper.deleteReply(stringJson);
	}


}
