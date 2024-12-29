package com.fifth.cms.util.admin;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.fifth.cms.model.admin.MenuVO;
import com.fifth.cms.service.admin.TopMenuService;

@Component
public class MenuUtil {
	
	private TopMenuService menuService;
	
	public MenuUtil(TopMenuService menuService) {
		this.menuService = menuService;
	}
	
	public int processChileNodePosition(MenuVO draggedNode, MenuVO targetNode, HashMap<String, String> stringJson, String moveProcessType) throws Exception {
		
		HashMap<String, String> tempJson = new HashMap<String, String>(stringJson); // 복사본 생성, 복사본을 생성하지 않으면, 참조 문제로 인해 원본 데이터가 변경될 수 있음

		int process_result = 0;

		if(draggedNode.getChildNode() != null && draggedNode.getChildNode().size() > 0) {

			int processChildNodeSort = menuService.updateNodeSortAfterPosition(tempJson);

			for(MenuVO childNode : draggedNode.getChildNode()) {

				// System.out.println("processChileNodePosition draggedNode : " + draggedNode.toString());
				// System.out.println("processChileNodePosition targetNode : " + targetNode.toString());
				// System.out.println("processChileNodePosition childNode : " + childNode.toString());

				tempJson.put("moveProcessType", "target");

				//target 노드의 pcode 값은 고정
				tempJson.put("targetPcode", draggedNode.getUid().toString());
				
				//target 노드의 depth값은 next 작업시 타겟 노드의 depth + 1, into 작업시 타겟 노드의 depth + 1 + 드래그 노드의 최대 깊이
				/*
				 왜 into 작업시 드래그 노드의 최대 깊이를 더하는가?
				 next 작업에서는 target노드의 depth을 따라가서 거기에 1을 더해주면 되지만,
				 into 작업에서는 target노드의 depth에 드래그 노드의 최대 깊이를 더해줘야 한다.
				 */
				if("next".equals(moveProcessType)) {
					tempJson.put("targetDepth", Integer.toString(targetNode.getDepth() + 1));
				}else if("into".equals(moveProcessType)) {
					tempJson.put("targetDepth", Integer.toString(targetNode.getDepth() + draggedNode.calculateMaxFloor() + 1));
				}
				tempJson.put("targetSort", Integer.toString(processChildNodeSort));
				tempJson.put("draggedUid", childNode.getUid().toString());
				
				//System.out.println("into process child node process 전 tempJson : " + tempJson.toString());

				process_result = menuService.updateNodeMoving(tempJson);
				processChildNodeSort++;
			}
		}

		return process_result;
	}

}
