package com.fifth.cms.controller.admin;

import java.util.HashMap;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fifth.cms.model.admin.MenuVO;
import com.fifth.cms.service.admin.TopMenuService;
import com.fifth.cms.util.admin.MenuUtil;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/api/admin/menu")
@Controller
public class AdminMenuController {


	private final TopMenuService topMenuService;
	private final MenuUtil menuUtil;
	private final Environment environment;
	
	public AdminMenuController( Environment environment, TopMenuService topMenuService, MenuUtil menuUtil ) {
		
		this.environment = environment;
		this.topMenuService = topMenuService;
		this.menuUtil = menuUtil;
	}
	
	@ResponseBody
	@RequestMapping(value = "/{processMark:list|write|view}", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> list(ModelMap map, HttpServletRequest req, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark ) throws Exception {  
		
		//System.out.println("TestCrudMenu stringJson : " + stringJson.toString());
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result",  false );
		
		if("".equals(processMark) || "list".equals(processMark) ) {

			stringJson.put("depth", "0");
			List<MenuVO> resultList = topMenuService.getList(stringJson);
			
			resultMap.put("resultList",  resultList );
			
		}else if( "write".equals(processMark) ) {
			
			MenuVO resultList = topMenuService.get(stringJson);

			resultMap.put("resultList",  resultList );
		}else if( "view".equals(processMark) ) {
			
			// List<MenuVO> resultList = topMenuService.getView(stringJson);
			
			// resultMap.put("resultList",  resultList );

		}
		
		resultMap.put("message",  "react project on" );
		resultMap.put("result",  true );
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/{processMark:insert|rename|update|delete|restore|move}", method = { RequestMethod.POST }, produces = "application/json")	
	public HashMap<String, Object> process(ModelMap map, HttpServletRequest req, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark ) throws Exception { 
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result",  false );
		/*
		if( !"true".equals(req.getHeader("AJAX"))	) {
			resultMap.put("message",  "ajax 요청 아님!" ); return resultMap;
		}
		*/
		
		String processMark_name = "";
		if("insert".equals(processMark)) {
			processMark_name = "입력";
		}else if("rename".equals(processMark)) {
			processMark_name = "이름변경";
		}else if("Update".equals(processMark)) {
			processMark_name = "수정";
		}else if("delete".equals(processMark)) {
			processMark_name = "삭제";
		}else if("restore".equals(processMark)) {
			processMark_name = "복구";
		}else if("move".equals(processMark)) {
			processMark_name = "이동";
		}
		
		Integer result = 0;
		
		if( "insert".equals(processMark) ) {

			String codeProcess_codeString = "";
			String maxSort = topMenuService.selectTableMenuListSortMax(stringJson);

			stringJson.put("codeHead", "MENU");
			stringJson.put("frstRegistNm", req.getHeader("AccessId").toString());
			stringJson.put("sort", maxSort);

			System.out.println("입력 대상 정보 : " + stringJson.toString());
			
			result = topMenuService.insertMenuRecord(stringJson);
			
			if(result > 0) resultMap.put("process_code", codeProcess_codeString );
		}else if( "rename".equals(processMark) ) {
			
			result = topMenuService.renameMenuRecord(stringJson);
			
		}else if( "update".equals(processMark) ) {

			MenuVO processGetVo = topMenuService.get(stringJson);
			stringJson.put("lastUpdtNm", req.getHeader("AccessId").toString());

			System.out.println("수정 대상 정보 : " + processGetVo.toString());

			result = topMenuService.updateMenuRecord(stringJson);
			
		}else if("delete".equals(processMark)) {
			
			MenuVO processGetVo = topMenuService.get(stringJson); 
			if( "N".equals( processGetVo.getDel_chk() ) ) {
				processMark_name = "비활성화";
				result = topMenuService.disableMenuRecord(stringJson);
			}else if( "Y".equals( processGetVo.getDel_chk() ) ) {
				result = topMenuService.deleteMenuRecord(stringJson);
			}
			
			
		}else if("restore".equals(processMark)) {
			
			MenuVO processGetVo = topMenuService.get(stringJson);
			if ( "Y".equals( processGetVo.getDel_chk() ) ) {
					result = topMenuService.restoreMenuRecord(stringJson);
			}
			
		}else if("move".equals(processMark)) {
			
			//draggedUid > 끌기로 시작한 노드
			//targetUid > 끌어놓은 노드 위치
			//position > above, next 이벤트

			/*
			 1. draggedUid의 sort값을 targetUid의 노드 값에 + 1 해준다
			 2. draggedUid노트의 sort 값이 같거나 높은 sort 값의 범위를 모두 + 1해준다
			 
			*/
			System.out.println("stringJson : " + stringJson.toString());

			MenuVO targetNode = topMenuService.getTargetNode(stringJson);
			//System.out.println("targetSort : " + targetNode.toString());
			MenuVO draggedNode = topMenuService.getDraggedNode(stringJson);
			//System.out.println("draggedSort : " + draggedNode.toString());


			//타겟의 노드나 드래그의 노드가 child가 있을 경우, 해당 노드의 이동 작업을 막기

			// if(targetNode.getChildNode() != null && targetNode.getChildNode().size() > 0) {
			// 	resultMap.put("message",  "타겟 노드의 자식 노드가 있어 이동할 수 없습니다." );
			// 	return resultMap;
			// }else 

			if(draggedNode.getChildNode() != null && draggedNode.calculateMaxFloor() > 2) {
				resultMap.put("message",  "이동 기능 중 드래그 노드의 자식 노드의 깊이는 최대 1단까지만 가능합니다." );
				return resultMap;
			}


			stringJson.put("targetPcode", targetNode.getPCode());
			stringJson.put("targetDepth", targetNode.getDepth().toString());
			stringJson.put("targetSort", targetNode.getSort().toString());

			stringJson.put("draggedPcode", draggedNode.getPCode());
			stringJson.put("draggedDepth", draggedNode.getDepth().toString());
			stringJson.put("draggedSort", draggedNode.getSort().toString());
			
			if( "above".equals(stringJson.get("position")) ) {

				//타겟의 노드에 dragged 노드의 위치정보를 입력
				stringJson.put("moveProcessType", "target");
				int process_one = topMenuService.updateNodeMoving(stringJson);

				//드래그의 노드에 target 노드의 위치정보를 입력
				stringJson.put("moveProcessType", "dragged");
				int process_two = topMenuService.updateNodeMoving(stringJson);
				
				if (process_one > 0 && process_two > 0) result = 1;
			}else if("next".equals(stringJson.get("position"))) {

				//자식 노드들의 sort 값을 +1 후 depth 조정
				int process_three = menuUtil.processChileNodePosition(draggedNode, targetNode, stringJson, stringJson.get("position"));

				//같은 깊이의 노드들의 targetSort와 같은 sort 값을 +1
				int process_one = topMenuService.updateNodeSortAfterPosition(stringJson);
				//드래그의 노드에 target 노드의 위치정보를 입력
				stringJson.put("moveProcessType", "target");
				stringJson.put("targetSort", Integer.toString(targetNode.getSort() + 1));
				stringJson.put("targetDepth", targetNode.getDepth().toString());

				int process_two = topMenuService.updateNodeMoving(stringJson);
				if (process_two > 0) result = 1;

			}else if("into".equals(stringJson.get("position"))) {
				//해당 target 노드 안에 dragged 노드를 넣기

				//자식 노드들의 sort 값을 +1 후 depth 조정
				int process_three = menuUtil.processChileNodePosition(draggedNode, targetNode, stringJson, stringJson.get("position"));

				stringJson.put("moveProcessType", "target");
				stringJson.put("targetDepth", Integer.toString(targetNode.getDepth() + 1));
				stringJson.put("targetSort", topMenuService.selectTableMenuListSortMax(stringJson));
				
				int process_one = topMenuService.updateNodeInto(stringJson);

				if (process_one > 0) result = 1;
			}
			
		}
		
		resultMap.put("message",  processMark_name + " 작업을 완료했습니다." );
		resultMap.put("result",  result );
		return resultMap;
	}
	
}




 