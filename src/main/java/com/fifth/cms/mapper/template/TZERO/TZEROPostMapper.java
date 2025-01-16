package com.fifth.cms.mapper.template.TZERO;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fifth.cms.model.template.TemplateZeroVO;
import com.fifth.cms.model.template.TemplateZeroFileVO;
@Mapper
public interface TZEROPostMapper {
	
	public List<TemplateZeroVO> getPostList(HashMap<String, Object> ObjectJson);
	public List<TemplateZeroVO> getNoticePostList(HashMap<String, String> stringJson);
	public TemplateZeroVO getPost(HashMap<String, String> stringJson);
	public TemplateZeroVO getViewLikeDislikePost(HashMap<String, String> stringJson);
	public List<TemplateZeroFileVO> getFileList(HashMap<String, String> stringJson);
	public String selectPostMaxCode(HashMap<String, String> stringJson);
	public Integer selectPostCount(HashMap<String, String> stringJson);

	public Integer insertPost(HashMap<String, String> stringJson);
	public Integer insertContent(HashMap<String, String> stringJson);
	public Integer updatePost(HashMap<String, String> stringJson);
	public Integer updateContent(HashMap<String, String> stringJson);
	public Integer disablePost(HashMap<String, String> stringJson);
	public Integer restorePost(HashMap<String, String> stringJson);
	public Integer plusViewNum(HashMap<String, String> stringJson);
	public Integer plusLike(HashMap<String, String> stringJson);
	public Integer plusDislike(HashMap<String, String> stringJson);

	public Integer deletePost(HashMap<String, String> stringJson);
	public Integer deleteContent(HashMap<String, String> stringJson);
	public Integer deleteFile(HashMap<String, String> stringJson);

}
