package com.fifth.cms.service.template.TZERO;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fifth.cms.mapper.template.TZERO.TZEROPostMapper;
import com.fifth.cms.model.template.TemplateZeroVO;
import com.fifth.cms.model.template.TemplateZeroFileVO;

@Service
public class TZEROPostService {
	
	private final TZEROPostMapper tZEROpostTableMapper;

	public TZEROPostService(TZEROPostMapper tZEROpostTableMapper) {
		this.tZEROpostTableMapper = tZEROpostTableMapper;
	}

	public List<TemplateZeroVO> getPostList(HashMap<String, String> stringJson) {

		HashMap<String, Object> ObjectJson = new HashMap<String, Object>();
		ObjectJson.put("startPoint", Integer.parseInt(stringJson.get("startPoint")) );
		ObjectJson.put("limit", Integer.parseInt(stringJson.get("limit")) );
		ObjectJson.put("codeHead", stringJson.get("codeHead") );

		return tZEROpostTableMapper.getPostList(ObjectJson);
	}		
	
	public TemplateZeroVO getPost(HashMap<String, String> stringJson) {
		return tZEROpostTableMapper.getPost(stringJson);
	}

	public TemplateZeroVO getViewLikeDislikePost(HashMap<String, String> stringJson) {
		return tZEROpostTableMapper.getViewLikeDislikePost(stringJson);
	}

	public List<TemplateZeroFileVO> getFileList(HashMap<String, String> stringJson) {
		return tZEROpostTableMapper.getFileList(stringJson);
	}

	public String selectPostMaxCode(HashMap<String, String> stringJson) {
		return tZEROpostTableMapper.selectPostMaxCode(stringJson);
	}

	public Integer selectPostCount(HashMap<String, String> stringJson) {
		return tZEROpostTableMapper.selectPostCount(stringJson);
	}

	public Integer insertPost(HashMap<String, String> stringJson) {
		tZEROpostTableMapper.insertPost(stringJson);  // insert 실행
		return Integer.valueOf(stringJson.get("uid")); // selectKey로 설정된 uid 값 반환
	}

	public Integer insertContent(HashMap<String, String> stringJson) {
		return tZEROpostTableMapper.insertContent(stringJson);
	}

	public Integer updatePost(HashMap<String, String> stringJson) {
		return tZEROpostTableMapper.updatePost(stringJson);
	}		

	public Integer updateContent(HashMap<String, String> stringJson) {
		return tZEROpostTableMapper.updateContent(stringJson);
	}

	public Integer disablePost(HashMap<String, String> stringJson) {
		return tZEROpostTableMapper.disablePost(stringJson);
	}

	public Integer restorePost(HashMap<String, String> stringJson) {
		return tZEROpostTableMapper.restorePost(stringJson);
	}

	public Integer plusViewNum(HashMap<String, String> stringJson) {
		return tZEROpostTableMapper.plusViewNum(stringJson);
	}

	public Integer plusLike(HashMap<String, String> stringJson) {
		return tZEROpostTableMapper.plusLike(stringJson);
	}

	public Integer plusDislike(HashMap<String, String> stringJson) {
		return tZEROpostTableMapper.plusDislike(stringJson);
	}

	public Integer deletePost(HashMap<String, String> stringJson) {
		return tZEROpostTableMapper.deletePost(stringJson);
	}
	
	public Integer deleteContent(HashMap<String, String> stringJson) {
		return tZEROpostTableMapper.deleteContent(stringJson);
	}
}
