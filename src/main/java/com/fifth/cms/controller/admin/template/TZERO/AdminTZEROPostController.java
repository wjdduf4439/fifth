package com.fifth.cms.controller.admin.template.TZERO;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fifth.cms.model.login.AccessVO;
import com.fifth.cms.model.template.TemplateZeroFileVO;
import com.fifth.cms.model.template.TemplateZeroVO;
import com.fifth.cms.service.login.access.AccessService;
import com.fifth.cms.service.template.TemplateUtilService;
import com.fifth.cms.service.template.TZERO.TZEROFileService;
import com.fifth.cms.service.template.TZERO.TZEROPostService;
import com.fifth.cms.util.access.AccessInfo;
import com.fifth.cms.util.template.FileProcess;
import com.fifth.cms.util.template.LikeOptionProcess;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin/template/tzero/post")
public class AdminTZEROPostController {

	private final AccessInfo accessInfo = new AccessInfo();
	private final Environment environment;
	private final FileProcess fileProcess;
	private final AccessService accessService;
	private final TZEROPostService tZEROPostService;
	private final TZEROFileService tZEROFileService;
	private final TemplateUtilService templateUtilService;
	private final LikeOptionProcess likeOptionProcess;

	public AdminTZEROPostController(Environment environment, FileProcess fileProcess, AccessService accessService, TZEROPostService tZEROPostService, TZEROFileService tZEROFileService, TemplateUtilService templateUtilService, LikeOptionProcess likeOptionProcess) {
		this.environment = environment;
		this.fileProcess = fileProcess;
		this.accessService = accessService;
		this.tZEROPostService = tZEROPostService;
		this.tZEROFileService = tZEROFileService;
		this.templateUtilService = templateUtilService;
		this.likeOptionProcess = likeOptionProcess;
	}
	
	@ResponseBody
	@RequestMapping(value = "/{processMark:list|one|count}", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> list(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark) throws Exception {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);

		if("list".equals(processMark)) {
			
			List<TemplateZeroVO> resultList = tZEROPostService.getPostList(stringJson);
			resultMap.put("resultList", resultList);

		}else if("one".equals(processMark)) {

			TemplateZeroVO codeHeadVO = tZEROPostService.getPost(stringJson);
			List<TemplateZeroFileVO> contentImagefileList = tZEROFileService.getContextImageFileList(stringJson);
			String saveTempContentImageFileString = "";
			for(TemplateZeroFileVO fileVO : contentImagefileList) {
				saveTempContentImageFileString += fileVO.getFname() + ",";
			}

			resultMap.put("resultList", codeHeadVO);
			resultMap.put("saveTempContentImageFileString", saveTempContentImageFileString.substring(0, saveTempContentImageFileString.length() - 1));

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
		boolean	moveContentImageProcessResult = true;

		if("insert".equals(processMark)) {

			stringJson.put("frstRegistNm", req.getHeader("AccessId").toString());
			result = tZEROPostService.insertPost(stringJson);

			stringJson.put("uid", result.toString());
			resultMap.put("uid", result.toString());

			fileProcess.makedir(stringJson, environment.getProperty("SAVE_CONTENT_IMAGE_DIR"));

			// 글내용에서 추출된 이미지 파일명을 담을 리스트
			List<String> imageNames = fileProcess.getTempContentImageFileNameList(stringJson);

			//saveTempContentImageFileString 기반의 이미지 파일 이동 작업
			//saveTempContentImageFileString 변수는 사용자가 내용에 파일을 업로드 할때 입력한 파일명을 담은 변수
			//실제 내용에 saveTempContentImageFileString 목록의 파일이 있는지는 불분명하므로 내용 안의 이미지 파일명을 정규식으로 추출해서 실제로 이동할 이미지 파일 이동 작업 진행
			String saveTempContentImageFileStringAll = stringJson.get("saveTempContentImageFileString");
			List<String> saveTempContentImageFileStringList = new ArrayList<String>(Arrays.asList(saveTempContentImageFileStringAll.split(",")));
			List<String> processList = new ArrayList<>(saveTempContentImageFileStringList);
			List<String> removeTempContentImageFileStringList = new ArrayList<String>();

			System.out.println("saveTempContentImageFileStringList에 기록된 파일명 : " + saveTempContentImageFileStringList.toString());
			for(int i_content = 0; i_content < saveTempContentImageFileStringList.size(); i_content++) {
				if(!imageNames.toString().contains(saveTempContentImageFileStringList.get(i_content))) {
					removeTempContentImageFileStringList.add(saveTempContentImageFileStringList.get(i_content));
					processList.remove(saveTempContentImageFileStringList.get(i_content));
				}
			}

			saveTempContentImageFileStringList = new ArrayList<>(processList);

			//System.out.println("글의 내용 : " + stringJson.get("processContext"));
			//System.out.println("최종적으로 db에 저장될 내용 이미지의 파일명 : " + saveTempContentImageFileStringList.toString());
			//System.out.println("최종적으로 삭제된 내용 이미지의 파일명 : " + removeTempContentImageFileStringList.toString());

			moveContentImageProcessResult = fileProcess.postContentImageFile(saveTempContentImageFileStringList, stringJson.get("codeHead"));
			fileProcess.deleteTempContentImageFile(removeTempContentImageFileStringList, stringJson.get("codeHead"));

			if(moveContentImageProcessResult) {

				//db에 이미지 내용 파일 정보 입력
				for(String tempContentImageFileString : saveTempContentImageFileStringList) {

					HashMap<String, String> fileValueMap = new HashMap<String, String>();

					String fpathString = environment.getProperty("STORAGEPATH") + "template" + File.separator + stringJson.get("codeHead") + environment.getProperty("SAVE_CONTENT_IMAGE_DIR") + tempContentImageFileString;
					fileValueMap.put("codeHead", stringJson.get("codeHead"));
					fileValueMap.put("code", stringJson.get("codeHead"));
					fileValueMap.put("pid", stringJson.get("uid"));
					fileValueMap.put("fpath", fpathString);
					fileValueMap.put("fname", tempContentImageFileString);

					tZEROFileService.insertContextImageFile(fileValueMap);

				}

				String regExp = "(?<=<img src=\"[^\"]*)"+ environment.getProperty("TEMP_CONTENT_IMAGE_DIR") +"(?=[^\"]*\">)";
				//content의 내용을 바꾸기
				String contextImageFixedContent = stringJson.get("processContext");
				//정규식으로 <img src=\" 태그 안의 src environment.getProperty("TEMP_CONTENT_IMAGE_DIR") 문자열을 찾아서 바꾸기
				// System.out.println("regExp : " + regExp);
				contextImageFixedContent = contextImageFixedContent.replaceAll(regExp, environment.getProperty("SAVE_CONTENT_IMAGE_DIR"));
				//System.out.println("contextImageFixedContent : " + contextImageFixedContent);

				stringJson.put("processContext", contextImageFixedContent);

			}

			result = tZEROPostService.insertContent(stringJson);

		}


		String message = processName + " 작업에 성공하였습니다.";
		if (result > 0) {

			if("insert".equals(processMark)) {
				likeOptionProcess.likeOptionFileCreate(stringJson);
				if(!moveContentImageProcessResult) {
					message += ", 이미지 이동 작업에 실패하였습니다.";
				}
				resultMap.put("message", message);
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
		boolean	moveContentImageProcessResult = true;
		boolean dropPostContentImageProcessResult = true;

		//삭제, 복구, 비활성화시, 인증 기능 추가
		if("delete".equals(processMark) || "restore".equals(processMark) || "disable".equals(processMark)) {
			HashMap<String, String> accessInfoMap = accessInfo.getAccessInfo(req);
			AccessVO accessVO = accessService.checkAdminOne(accessInfoMap);
			stringJson.put("frstRegistNm", accessVO.getId());
		}

		if("update".equals(processMark)) {

			stringJson.put("lastUpdtNm", req.getHeader("AccessId").toString());
			result = tZEROPostService.updatePost(stringJson);

			// 글내용에서 추출된 이미지 파일명을 담을 리스트
			List<String> imageNames = fileProcess.getTempContentImageFileNameList(stringJson);

			//saveTempContentImageFileString, postContentImageFileString 기반의 이미지 파일 이동 작업
			//saveTempContentImageFileString 변수는 사용자가 내용에 파일을 업로드 할때 입력한 파일명을 담은 변수
			//postContentImageFileString 변수는 이전 입력 작업에서 이미 업로드 된 이미지 파일명을 담은 변수
			//실제 내용에 saveTempContentImageFileString 목록의 파일이 있는지는 불분명하므로 내용 안의 이미지 파일명을 정규식으로 추출해서 실제로 이동할 이미지 파일 이동 작업 진행
			//실제 내용중 postContentImageFileString 목록의 파일에 대하여, 기존 작성된 내용 이미지 중 삭제된 부분이 있다면 삭제 작업 실행
			String saveTempContentImageFileStringAll = stringJson.get("saveTempContentImageFileString");
			String postContentImageFileStringAll = stringJson.get("postContentImageFileString");
			List<String> saveTempContentImageFileStringList = new ArrayList<String>(Arrays.asList(saveTempContentImageFileStringAll.split(",")));
			List<String> postContentImageFileStringList = new ArrayList<String>(Arrays.asList(postContentImageFileStringAll.split(",")));
			List<String> processList = new ArrayList<>(saveTempContentImageFileStringList);
			List<String> removeTempContentImageFileStringList = new ArrayList<String>();
			List<String> dropPostContentImageFileStringList = new ArrayList<String>();

			System.out.println("saveTempContentImageFileStringList에 기록된 파일명 : " + saveTempContentImageFileStringList.toString());
			for(int i_content = 0; i_content < saveTempContentImageFileStringList.size(); i_content++) {
				if(!imageNames.toString().contains(saveTempContentImageFileStringList.get(i_content))) {
					removeTempContentImageFileStringList.add(saveTempContentImageFileStringList.get(i_content));
					processList.remove(saveTempContentImageFileStringList.get(i_content));
				}
			}

			saveTempContentImageFileStringList = new ArrayList<>(processList);

			System.out.println("postContentImageFileStringList에 기록된 파일명 : " + postContentImageFileStringList.toString());
			for(int i_post = 0; i_post < postContentImageFileStringList.size(); i_post++) {
				if(!imageNames.toString().contains(postContentImageFileStringList.get(i_post))) {
					dropPostContentImageFileStringList.add(postContentImageFileStringList.get(i_post));
				}
			}

			//System.out.println("글의 내용 : " + stringJson.get("processContext"));
			//System.out.println("최종적으로 db에 저장될 내용 이미지의 파일명 : " + saveTempContentImageFileStringList.toString());
			//System.out.println("최종적으로 삭제된 내용 이미지의 파일명 : " + removeTempContentImageFileStringList.toString());

			moveContentImageProcessResult = fileProcess.postContentImageFile(saveTempContentImageFileStringList, stringJson.get("codeHead"));
			fileProcess.deleteTempContentImageFile(removeTempContentImageFileStringList, stringJson.get("codeHead"));
			dropPostContentImageProcessResult = fileProcess.deletePostContentImageFile(dropPostContentImageFileStringList, stringJson.get("codeHead"));

			if(moveContentImageProcessResult) {

				//db에 이미지 내용 파일 정보 입력
				for(String tempContentImageFileString : saveTempContentImageFileStringList) {

					HashMap<String, String> fileValueMap = new HashMap<String, String>();

					String fpathString = environment.getProperty("STORAGEPATH") + "template" + File.separator + stringJson.get("codeHead") + environment.getProperty("SAVE_CONTENT_IMAGE_DIR") + tempContentImageFileString;
					fileValueMap.put("codeHead", stringJson.get("codeHead"));
					fileValueMap.put("code", stringJson.get("codeHead"));
					fileValueMap.put("pid", stringJson.get("uid"));
					fileValueMap.put("fpath", fpathString);
					fileValueMap.put("fname", tempContentImageFileString);

					tZEROFileService.insertContextImageFile(fileValueMap);

				}

			}

			if(dropPostContentImageProcessResult) {

				List<TemplateZeroFileVO> postContextImageFilesList = tZEROFileService.getContextImageFileList(stringJson);
				for(TemplateZeroFileVO fileVO : postContextImageFilesList) {
					for(String dropPostContentImageFileString : dropPostContentImageFileStringList) {
						if(fileVO.getFname().equals(dropPostContentImageFileString)) {
							stringJson.put("dropPostContentImageFileUid", fileVO.getUid().toString());
							tZEROFileService.deletePostedContextImageFile(stringJson);
						}
					}
				}
				
				
			}

			if(moveContentImageProcessResult || dropPostContentImageProcessResult) {
				
				String regExp = "(?<=<img src=\"[^\"]*)"+ environment.getProperty("TEMP_CONTENT_IMAGE_DIR") +"(?=[^\"]*\">)";
				//content의 내용을 바꾸기
				String contextImageFixedContent = stringJson.get("processContext");
				//정규식으로 <img src=\" 태그 안의 src environment.getProperty("TEMP_CONTENT_IMAGE_DIR") 문자열을 찾아서 바꾸기
				// System.out.println("regExp : " + regExp);
				contextImageFixedContent = contextImageFixedContent.replaceAll(regExp, environment.getProperty("SAVE_CONTENT_IMAGE_DIR"));
				//System.out.println("update process contextImageFixedContent : " + contextImageFixedContent);

				stringJson.put("processContext", contextImageFixedContent);

			}
			
			result = tZEROPostService.updateContent(stringJson);

		}else if("disable".equals(processMark)) {
			result = tZEROPostService.disablePost(stringJson);
		}else if("restore".equals(processMark)) {
			result = tZEROPostService.restorePost(stringJson);
		}else if("delete".equals(processMark)) {
			List<TemplateZeroFileVO> postFilesList = tZEROPostService.getFileList(stringJson);
			List<TemplateZeroFileVO> postContextImageFilesList = tZEROFileService.getContextImageFileList(stringJson);

			//System.out.println(stringJson.get("uid") + "번 게시물 해당 게시물의 파일 갯수 : " + postFilesList.size());
			//System.out.println(stringJson.get("uid") + "번 게시물 해당 게시물의 내용 이미지 파일 갯수 : " + postContextImageFilesList.size());

			//게시물 삭제
			int processOne = tZEROPostService.deletePost(stringJson);
			//게시물 내용 삭제
			int processTwo = 0;
			if(processOne > 0) processTwo = tZEROPostService.deleteContent(stringJson);

			//게시물 파일 삭제
			int processThree = 0;
			if(postFilesList.size() > 0)	{
				for(TemplateZeroFileVO fileVO : postFilesList) {
					fileProcess.deleteFile(fileVO.getFpath());
				}
				processThree = tZEROFileService.deleteFileRecord(stringJson);
			}else{
				processThree = 1;
			}

			if(postContextImageFilesList.size() > 0) {
				for(TemplateZeroFileVO fileVO : postContextImageFilesList) {
					fileProcess.deleteFile(fileVO.getFpath());
				}
				processThree = tZEROFileService.deleteContextImageFile(stringJson);
			}else{
				processThree = 1;
			}

			//댓글 삭제 기능 추가 예정
			int processFour = 0;

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
