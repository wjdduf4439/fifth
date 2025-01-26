package com.fifth.cms.controller.admin.template.TZERO;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fifth.cms.service.login.access.AccessService;
import com.fifth.cms.service.template.TemplateUtilService;
import com.fifth.cms.service.template.TZERO.TZEROReplyService;
import com.fifth.cms.util.access.AccessInfo;
import com.fifth.cms.util.template.LikeOptionProcess;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Controller
@RequestMapping("/api/admin/template/tzero/reply")
public class AdminTZEROReplyController {
	
	private final AccessInfo accessInfo = new AccessInfo();
	private final AccessService accessService;
	private final TZEROReplyService tZEROreplyService;
	private final TemplateUtilService templateUtilService;
	private final LikeOptionProcess likeOptionProcess;

	public AdminTZEROReplyController(TZEROReplyService tZEROreplyService, TemplateUtilService templateUtilService, AccessService accessService, LikeOptionProcess likeOptionProcess) {
		this.tZEROreplyService = tZEROreplyService;
		this.templateUtilService = templateUtilService;
		this.accessService = accessService;	
		this.likeOptionProcess = likeOptionProcess;
	}


	@ResponseBody
	@RequestMapping(value = "/{processMark:insert|response}", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> process(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark ) throws Exception {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);
		String processName = "";
		if("insert".equals(processMark)) {
			processName = "입력";
		}else if("response".equals(processMark)) {
			processName = "답글 입력";
		}

		Integer result = 0;
		System.out.println("TZEROReplyController process : " + processName + " 전 정보 : " + stringJson.toString());

		if(stringJson.get("context").toString().length() > 200) {
			resultMap.put("message", "댓글은 200자 이하로 입력해주세요.");
			return resultMap;
		}

		if("insert".equals(processMark) || "response".equals(processMark)) {

			HashMap<String, String> accessInfoMap = accessInfo.getAccessInfo(req);

			stringJson.put("writerNick", accessInfoMap.get("nick"));
			stringJson.put("frstRegistNm", accessInfoMap.get("accessId"));

			//이 insert작업이 완료되면, 마지막 삽입된 uid를 가져와서 stringJson에 넣어준다.
			result = tZEROreplyService.insertReply(stringJson);

		}

		if (result > 0) {

			if("insert".equals(processMark)) {

				stringJson.put("likeOptionType", "likeOption_reply");
				likeOptionProcess.likeOptionFileCreate(stringJson);
				System.out.println("TZEROReplyController process : " + processName + " 전 정보2 : " + stringJson.toString());
				stringJson.put("momRepUid", stringJson.get("uid"));
				System.out.println("TZEROReplyController process : " + processName + " 전 정보3 : " + stringJson.toString());
				tZEROreplyService.updateReplyMomRepCode(stringJson);
				
			}else if("response".equals(processMark)) {

				stringJson.put("likeOptionType", "likeOption_reply");
				likeOptionProcess.likeOptionFileCreate(stringJson);
				tZEROreplyService.updateReplyMomRepCode(stringJson);

			}


			resultMap.put("result", true);
		}

		return resultMap;

	}

	@ResponseBody
	@RequestMapping(value = "/{processMark:update|like|dislike}", method = { RequestMethod.PUT }, produces = "application/json")
	public HashMap<String, Object> put(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark ) throws Exception {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);
		String processName = "";
		if("update".equals(processMark)) {
			processName = "수정";
		}else if("like".equals(processMark)) {
			processName = "추천";
		}else if("dislike".equals(processMark)) {
			processName = "비추천";
		}

		Integer result = 0;
		System.out.println("TZEROReplyController put : " + processName + " 전 정보 : " + stringJson.toString());

		//댓글 좋아요 검증 작업은 무조건 이 코드를 넣어야함
		if("update".equals(processMark)) {
			
			result = tZEROreplyService.updateReply(stringJson);

		}else if("like".equals(processMark) || "dislike".equals(processMark)) stringJson.put("likeOptionType", "likeOption_reply");

		if("like".equals(processMark)) {
			boolean isVaild = likeOptionProcess.isVaildLikeOption(req, stringJson, processMark);
			if(isVaild) {
				result = templateUtilService.plusReplyLike(stringJson);
			}else{
				resultMap.put("message", "이미 좋아요를 누른 댓글입니다.");
				return resultMap;
			}
		}else if("dislike".equals(processMark)) {
			boolean isVaild = likeOptionProcess.isVaildLikeOption(req, stringJson, processMark);
			if(isVaild) {
				result = templateUtilService.plusReplyDislike(stringJson);
			}else{
				resultMap.put("message", "이미 싫어요를 누른 댓글입니다.");
				return resultMap;
			}
		}

		if(result > 0) {

			if("like".equals(processMark) || "dislike".equals(processMark)) {
				likeOptionProcess.updateLikeOptionPath(req, stringJson, processMark);
			}

			resultMap.put("result", true);
			resultMap.put("message", processName + " 작업에 성공하였습니다.");
		}

		
		return resultMap;

	}

	//delete, put 메소드의 취약점은 rest api 형태로 전달되는 경우 파일 처리에 대한 취약점이 존재하지 않는다. 해당 메소드는 리소스 조작만을 지원한다.
	@ResponseBody
	@RequestMapping(value = "/{processMark:delete}", method = { RequestMethod.DELETE }, produces = "application/json")
	public HashMap<String, Object> delete(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark ) throws Exception {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);
		String processName = "";
		if("delete".equals(processMark)) {
			processName = "삭제";
		}

		Integer result = 0;
		stringJson.put("likeOptionType", "likeOption_reply");

		if("delete".equals(processMark)) {
			result = tZEROreplyService.deleteReply(stringJson);
		}

		if (result > 0) {

			if("delete".equals(processMark)) {
				likeOptionProcess.deleteLikeOptionPath(stringJson);
			}

			resultMap.put("result", true);
			resultMap.put("message", processName + " 작업에 성공하였습니다.");
		}

		return resultMap;

	}

}
