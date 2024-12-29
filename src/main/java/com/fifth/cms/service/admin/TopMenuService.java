package com.fifth.cms.service.admin;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fifth.cms.mapper.admin.TemplateOptionMapper;
import com.fifth.cms.mapper.admin.TopMenuMapper;
import com.fifth.cms.model.admin.MenuVO;

@Service
public class TopMenuService {

	private final TopMenuMapper topMenuMapper;
	
	public TopMenuService( TopMenuMapper topMenuMapper) {
		
		this.topMenuMapper = topMenuMapper;
	}
	
	public List<MenuVO> getList(HashMap<String, String> stringJson) throws Exception {	
		// TODO Auto-generated method stub
		return topMenuMapper.getList( stringJson );
	}
	
	public List<MenuVO> getView(HashMap<String, String> stringJson) throws Exception {
		// TODO Auto-generated method stub
		return topMenuMapper.getView( stringJson );
	}
	
	public MenuVO get(HashMap<String, String> stringJson) throws Exception {
		// TODO Auto-generated method stub
		return topMenuMapper.get( stringJson );
	}
	
	public MenuVO get(String siteCode) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, String> paramHashMap = new HashMap<String, String>();
		return topMenuMapper.get( paramHashMap );
	}
	
	public MenuVO getTargetNode( HashMap<String, String> stringJson ) throws Exception {
		// TODO Auto-generated method stub
		return topMenuMapper.getTargetNode( stringJson );
	}

	public MenuVO getDraggedNode( HashMap<String, String> stringJson ) throws Exception {
		// TODO Auto-generated method stub
		return topMenuMapper.getDraggedNode( stringJson );
	}
	
	public String selectTableMenuListMax(HashMap<String, String> stringJson) throws Exception {
		// TODO Auto-generated method stub 
		return topMenuMapper.selectTableMenuListMax( stringJson );
	}
	
	public String selectTableMenuListSortMax(HashMap<String, String> stringJson) throws Exception {
		// TODO Auto-generated method stub 
		return topMenuMapper.selectTableMenuListSortMax( stringJson );
	}
	
	public Integer insertMenuRecord( HashMap<String, String> stringJson ) throws Exception {
		// TODO Auto-generated method stub
		return topMenuMapper.insertMenuRecord( stringJson );
	}
	
	public Integer renameMenuRecord( HashMap<String, String> stringJson ) throws Exception {
		// TODO Auto-generated method stub
		return topMenuMapper.renameMenuRecord( stringJson );
	}
	
	//노드 정보 수정
	public Integer updateMenuRecord( HashMap<String, String> stringJson ) throws Exception {
		// TODO Auto-generated method stub
		return topMenuMapper.updateMenuRecord( stringJson );
	}

	//target 노드와 dragged 노드의 위치정보 업데이트
	public Integer updateNodeMoving( HashMap<String, String> stringJson ) throws Exception {
		// TODO Auto-generated method stub
		return topMenuMapper.updateNodeMoving( stringJson );
	}

	//특정 위치 이후의 노드 정렬 순서 업데이트
	public Integer updateNodeSortAfterPosition( HashMap<String, String> stringJson ) throws Exception {
		return topMenuMapper.updateNodeSortAfterPosition( stringJson );
	}

	//특정 노드 안에 노드 넣기
	public Integer updateNodeInto( HashMap<String, String> stringJson ) throws Exception {
		return topMenuMapper.updateNodeInto( stringJson );
	}
	
	public Integer setTargetSortMenuRecord( HashMap<String, String> stringJson ) throws Exception {
		// TODO Auto-generated method stub
		//target
		return topMenuMapper.setTargetSortMenuRecord( stringJson );
	}
	
	public Integer setDraggedSortMenuRecord( HashMap<String, String> stringJson ) throws Exception {
		// TODO Auto-generated method stub
		//target
		return topMenuMapper.setDraggedSortMenuRecord( stringJson );
	}
	
	public Integer disableMenuRecord( HashMap<String, String> stringJson ) throws Exception {
		// TODO Auto-generated method stub
		return topMenuMapper.disableMenuRecord( stringJson );
	}
	
	public Integer deleteMenuRecord( HashMap<String, String> stringJson ) throws Exception {
		// TODO Auto-generated method stub
		return topMenuMapper.deleteMenuRecord( stringJson );
	}
	
	public Integer restoreMenuRecord( HashMap<String, String> stringJson ) throws Exception {
		// TODO Auto-generated method stub
		return topMenuMapper.restoreMenuRecord( stringJson );
	}
		
}




 