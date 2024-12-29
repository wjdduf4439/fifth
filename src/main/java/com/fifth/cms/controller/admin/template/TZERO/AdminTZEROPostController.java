package com.fifth.cms.controller.admin.template.TZERO;

import java.util.HashMap;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fifth.cms.model.template.TemplateZeroFileVO;
import com.fifth.cms.model.template.TemplateZeroVO;
import com.fifth.cms.service.template.TemplateUtilService;
import com.fifth.cms.service.template.TZERO.TZEROFileService;
import com.fifth.cms.service.template.TZERO.TZEROPostService;
import com.fifth.cms.util.template.FileProcess;
import com.fifth.cms.util.template.LikeOptionProcess;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin/template/tzero/post")
public class AdminTZEROPostController {

	private final Environment environment;
	private final FileProcess fileProcess;
	private final TZEROPostService tZEROPostService;
	private final TZEROFileService tZEROFileService;
	private final TemplateUtilService templateUtilService;
	private final LikeOptionProcess likeOptionProcess;

	public AdminTZEROPostController(Environment environment, FileProcess fileProcess, TZEROPostService tZEROPostService, TZEROFileService tZEROFileService, TemplateUtilService templateUtilService, LikeOptionProcess likeOptionProcess) {
		this.environment = environment;
		this.fileProcess = fileProcess;
		this.tZEROPostService = tZEROPostService;
		this.tZEROFileService = tZEROFileService;
		this.templateUtilService = templateUtilService;
		this.likeOptionProcess = likeOptionProcess;
	}
	
	@ResponseBody
	@RequestMapping(value = "/{processMark:list|one|count}", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> list(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);

		if("list".equals(processMark)) {
			
			List<TemplateZeroVO> resultList = tZEROPostService.getPostList(stringJson);
			resultMap.put("resultList", resultList);

		}else if("one".equals(processMark)) {

			TemplateZeroVO codeHeadVO = tZEROPostService.getPost(stringJson);
			resultMap.put("resultList", codeHeadVO);

		}

		resultMap.put("result", true);
		

		return resultMap;
	}
	

	@ResponseBody
	@RequestMapping(value = "/{processMark:insert}", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> process(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark ) throws Exception {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);
		String processName = "";
		if("insert".equals(processMark)) {
			processName = "입력";
		}

		Integer result = 0;
		System.out.println("AdminTZEROPostController process : " + processName + " 전 정보 : " + stringJson.toString());

		if("insert".equals(processMark)) {

			stringJson.put("frstRegistNm", req.getHeader("AccessId").toString());
			result = tZEROPostService.insertPost(stringJson);

			stringJson.put("uid", result.toString());
			resultMap.put("uid", result.toString());

			result = tZEROPostService.insertContent(stringJson);

		}


		if (result > 0) {

			if("insert".equals(processMark)) {
				likeOptionProcess.likeOptionFileCreate(stringJson);
				resultMap.put("message", processName + " 작업에 성공하였습니다.");
			}

			resultMap.put("result", true);
		}

		return resultMap;

	}

	@ResponseBody
	@RequestMapping(value = "/{processMark:update|disable|restore|delete|like|dislike}", method = { RequestMethod.PUT }, produces = "application/json")
	public HashMap<String, Object> putProcess(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark ) throws Exception {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);
		String processName = "";
		if("update".equals(processMark)) {
			processName = "수정";
		}else if("disable".equals(processMark)) {
			processName = "비활성화";
		}else if("delete".equals(processMark)) {
			processName = "삭제";
		}else if("restore".equals(processMark)) {
			processName = "복구";
		}

		Integer result = 0;
		System.out.println("AdminTZEROPostController putProcess : " + processName + " 전 정보 : " + stringJson.toString());

		if("update".equals(processMark)) {

			stringJson.put("lastUpdtNm", req.getHeader("AccessId").toString());
			result = tZEROPostService.updatePost(stringJson);
			result = tZEROPostService.updateContent(stringJson);

		}else if("disable".equals(processMark)) {
			result = tZEROPostService.disablePost(stringJson);
		}else if("restore".equals(processMark)) {
			result = tZEROPostService.restorePost(stringJson);
		}else if("delete".equals(processMark)) {

			List<TemplateZeroFileVO> postFilesList = tZEROPostService.getFileList(stringJson);

			System.out.println(stringJson.get("uid") + "번 게시물 해당 게시물의 파일 갯수 : " + postFilesList.size());

			int processOne = tZEROPostService.deletePost(stringJson);
			int processTwo = tZEROPostService.deleteContent(stringJson);

			int processThree = 0;
			if(postFilesList.size() > 0)	{
				for(TemplateZeroFileVO fileVO : postFilesList) {
					fileProcess.deleteFile(fileVO.getFpath());
				}
				processThree = tZEROFileService.deleteFileRecord(stringJson);
			}else{
				processThree = 1;
			}

			if(processOne > 0 && processTwo > 0 && processThree > 0) result = 1;

		}else if("like".equals(processMark)) {
			boolean isVaild = likeOptionProcess.isVaildLikeOption(req, stringJson, processMark);
			if(isVaild) {
				result = templateUtilService.plusLike(stringJson);
			}else{
				resultMap.put("message", "이미 좋아요를 누른 게시물입니다.");
				return resultMap;
			}
		}else if("dislike".equals(processMark)) {
			boolean isVaild = likeOptionProcess.isVaildLikeOption(req, stringJson, processMark);
			if(isVaild) {
				result = templateUtilService.plusDislike(stringJson);
			}else{
				resultMap.put("message", "이미 싫어요를 누른 게시물입니다.");
				return resultMap;
			}
		}

		if (result > 0) {

			if("like".equals(processMark) || "dislike".equals(processMark)) {
				likeOptionProcess.updateLikeOptionPath(req, stringJson, processMark);
			}else if("delete".equals(processMark)) {
				likeOptionProcess.deleteLikeOptionPath(stringJson);
			}

			resultMap.put("result", true);
			if(!"like".equals(processMark) && !"dislike".equals(processMark)) {
				resultMap.put("message", processName + " 작업에 성공하였습니다.");
			}
		}

		return resultMap;
	}

}
