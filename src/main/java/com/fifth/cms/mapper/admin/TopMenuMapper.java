package com.fifth.cms.mapper.admin;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fifth.cms.model.admin.MenuVO;

@Mapper
public interface TopMenuMapper {
	public List<MenuVO> getList(HashMap<String, String> stringJson);
	public List<MenuVO> getView(HashMap<String, String> stringJson);
	
	public MenuVO get(HashMap<String, String> stringJson);
	public MenuVO getTargetNode(HashMap<String, String> stringJson);
	public MenuVO getDraggedNode(HashMap<String, String> stringJson);
	
	public String selectTableMenuListMax(HashMap<String, String> stringJson);
	public String selectTableMenuListSortMax(HashMap<String, String> stringJson);

	public Integer insertMenuRecord( HashMap<String, String> stringJson );
	
	public Integer renameMenuRecord( HashMap<String, String> stringJson );
	
	public Integer updateMenuRecord( HashMap<String, String> stringJson );
	public Integer updateNodeMoving( HashMap<String, String> stringJson );
	public Integer updateNodeSortAfterPosition( HashMap<String, String> stringJson );
	public Integer updateNodeInto( HashMap<String, String> stringJson );
	public Integer setTargetSortMenuRecord( HashMap<String, String> stringJson );
	public Integer setDraggedSortMenuRecord( HashMap<String, String> stringJson );
	
	public Integer disableMenuRecord( HashMap<String, String> stringJson );
	public Integer deleteMenuRecord( HashMap<String, String> stringJson );
	public Integer restoreMenuRecord( HashMap<String, String> stringJson );
	
}




 