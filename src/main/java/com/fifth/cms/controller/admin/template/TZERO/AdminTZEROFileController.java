package com.fifth.cms.controller.admin.template.TZERO;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fifth.cms.model.admin.CodeHeadVO;
import com.fifth.cms.model.template.TemplateZeroFileVO;
import com.fifth.cms.service.admin.CodeHeadService;
import com.fifth.cms.service.template.TZERO.TZEROFileService;
import com.fifth.cms.service.template.TZERO.TZEROPostService;
import com.fifth.cms.util.CodeProcess;
import com.fifth.cms.util.template.FileProcess;
import com.fifth.cms.util.template.FileUploadValidateWork;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/api/admin/template/tzero/file")
public class AdminTZEROFileController {

	private final Environment environment;
	private final FileProcess fileProcess;
	private final CodeHeadService codeHeadService;
	private final TZEROPostService tzeroPostService;
	private final TZEROFileService tzeroFileService;

	public AdminTZEROFileController(Environment environment, FileProcess fileProcess, CodeHeadService codeHeadService, TZEROPostService tzeroPostService, TZEROFileService tzeroFileService) {
		this.environment = environment;
		this.fileProcess = fileProcess;
		this.codeHeadService = codeHeadService;
		this.tzeroPostService = tzeroPostService;
		this.tzeroFileService = tzeroFileService;
	}

	//consumes는 클라이언트가 서버에게 보내는 데이터 타입을 명시한다.
	//produces는 서버가 클라이언트에게 반환하는 데이터 타입을 명시한다.
	@ResponseBody
	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = "multipart/form-data")
	public HashMap<String, Object> upload(MultipartHttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson) throws Exception { 

		System.out.println("AdminTZEROFileController upload 작업 전 stringJson : " + stringJson.toString());

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result",  false );
		Integer result = 0;
		
		HashMap<String, String> fileValueMap = new HashMap<String, String>();
		String maxCode = "";
		
		fileProcess.makedir(stringJson, "file");	//파일이 있다면 파일경로를 먼저 지정
		String savepathString = fileProcess.getPREFIX_URL();
		String savefileString = "";
		
		//이 부분은 새로 만들 여지가 있음. codeheadvo를 가져와서 옵션 정보를 가져와 해당 파일안에 있는 maxFileUploadNumber, fileUploadType 옵션에 대한 검증 작업을 추가해야함
		FileUploadValidateWork fileUploadValidateWork = null;
		CodeHeadVO valCodeHeadVO = codeHeadService.selectCodeHeadOneforCode(stringJson);
		System.out.println("Codehead 업로드 사이트 코드 정보 : " + stringJson.toString() );
		System.out.println("Codehead 업로드 사이트 정보 : " + valCodeHeadVO.toString() );

		CodeProcess codeProcess = new CodeProcess(environment);
		HashMap<String, String> codeheadOptionMap = codeProcess.getOptionPathJson(stringJson);

		System.out.println("Codehead 옵션 codeheadOptionMap : " + codeheadOptionMap.toString() );
		
		Integer uploadedFileCnt = 0;
		uploadedFileCnt = tzeroFileService.getUploadedFileCount(stringJson);
		System.out.println("Codehead 이미 업로드된 파일 개수 : " + uploadedFileCnt );
		
		/*
		Iterator 객체는 컬렉션(Collection) 객체들을 순회(traverse)하는데 사용되는 디자인 패턴입니다. 
			boolean hasNext(): 컬렉션의 다음 요소가 존재하는지 여부를 반환합니다. 다음 요소가 존재하면 true, 그렇지 않으면 false를 반환합니다.
			E next(): 컬렉션의 다음 요소를 반환합니다. 만약 더 이상 요소가 존재하지 않는 경우 NoSuchElementException을 던집니다.
			void remove():	next() 메소드로 반환된 가장 최근의 요소를 컬렉션에서 제거합니다. 
							이 메소드는 선택적 기능이며, 모든 컬렉션이 이를 지원하지는 않습니다. 이 메소드를 호출하기 전에 next()를 호출하지 않았거나, 
							이전에 이미 remove()를 호출했다면 IllegalStateException을 던집니다.
		*/
        Iterator<String> fileRequest = req.getFileNames();
        int i_fileRequest = 0;
        while (fileRequest.hasNext()) {
            String fileRequestName = fileRequest.next();
            List<MultipartFile> files = req.getFiles(fileRequestName);
            

			//이 부분은 새로 만들 여지가 있음. codeheadvo를 가져와서 옵션 정보를 가져와 해당 파일안에 있는 FileUploadType, MaxFileUploadNumber 옵션에 대한 검증 작업을 추가해야함

            fileUploadValidateWork = new FileUploadValidateWork(environment, codeheadOptionMap);
			boolean resultValidateType = fileUploadValidateWork.validatingFileType(files);
			if(!resultValidateType) {
				resultMap.put("message",  "허용되지 않는 확장자의 파일을 업로드했습니다." );
				resultMap.put("result",  false );
				return resultMap;
			}

			boolean resultValidateNumber = fileUploadValidateWork.validatingFileNumber(files);
			if(!resultValidateNumber) {
				resultMap.put("message",  "첨부파일을 너무 많이 업로드했습니다." );
				resultMap.put("result",  false );
				return resultMap;
			}
            
            for (MultipartFile file : files) {
            	
            	savefileString = fileProcess.makeSavingFileCode(stringJson.get("codeHead"), i_fileRequest);
            	// System.out.println("insert code: " + stringJson.get("codeHead"));
				// System.out.println("insert pid: " + stringJson.get("uid"));
            	// System.out.println("insert fsign: " + i_fileRequest);
            	// System.out.println("insert fpath: " + savepathString + savefileString);
            	// System.out.println("insert savingFileName " + savefileString);
            	// System.out.println("insert fname " + file.getOriginalFilename());


            	fileValueMap.put("codeHead", stringJson.get("codeHead"));
            	maxCode = tzeroFileService.selectFileRecordListMax(fileValueMap);
				
				fileValueMap.put("pid", stringJson.get("uid"));
				fileValueMap.put("fsign", Integer.toString(i_fileRequest));
				fileValueMap.put("fpath", savepathString + savefileString);
				fileValueMap.put("savingFname", savefileString);
				fileValueMap.put("fname", file.getOriginalFilename());

            	result = tzeroFileService.insertFileRecord(fileValueMap);
            	if(result > 0) fileProcess.writeFile( file, savefileString );
            	
            	i_fileRequest++;
            }
        }

		resultMap.put("message",  "업로드 작업을 완료했습니다." );
		resultMap.put("result",  true );
		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.PUT, produces = "application/json")
	public HashMap<String, Object> delete(ModelMap map, HttpServletRequest req, @RequestParam HashMap<String, String> stringJson) throws Exception {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result",  false );
		Integer result = 0;
		
		TemplateZeroFileVO targetVO = tzeroFileService.getFile(stringJson);
		
		result = tzeroFileService.deleteFileRecord(stringJson);
		if(result > 0 ) {
			fileProcess.deleteFile( targetVO.getFpath() );
			resultMap.put("result",  true );
		}
		
		resultMap.put("message",  "파일 삭제 작업을 완료했습니다." );
		
		return resultMap;
	}


	@ResponseBody
	@RequestMapping(value = "/{processMark:contentUploadNoPost}", method = RequestMethod.POST, consumes = "multipart/form-data")
	public HashMap<String, Object> contentUploadOne(MultipartHttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark") String processMark) throws Exception {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result",  false );
		Integer result = 0;

		
		String dirName = "";
		if("contentUploadNoPost".equals(processMark)) {
			dirName = environment.getProperty("TEMP_CONTENT_IMAGE_DIR");
		}

		fileProcess.makedir(stringJson, dirName);	//파일이 있다면 파일경로를 먼저 지정, 등록된 게시물이 없는 경우
		System.out.println("파일 경로 : " + fileProcess.getPREFIX_URL());
		String savepathString = fileProcess.getPREFIX_URL();
		String savefileString = "";

		System.out.println("AdminTZEROFileController contentUpload 작업 전 stringJson : " + stringJson.toString());


		Iterator<String> fileRequest = req.getFileNames();
        int i_fileRequest = 0;
		String fileType = "";
		String saveTempContentImageFileString = "";
        while (fileRequest.hasNext()) {
            String fileRequestName = fileRequest.next();
            List<MultipartFile> files = req.getFiles(fileRequestName);

			for (MultipartFile file : files) {

				//System.out.println("file type : " + file.getContentType() + " : " + file.getOriginalFilename() );

				if(	!"image/png".equals(file.getContentType()) &&
					!"image/jpeg".equals(file.getContentType()) &&
					!"image/jpg".equals(file.getContentType()) &&
					!"image/gif".equals(file.getContentType()) &&
					!"image/webp".equals(file.getContentType())
					) {
						resultMap.put("message",  "내용 이미지 업로드 파일 형식에 맞지 않습니다. : " + file.getOriginalFilename() );
						return resultMap;
					}

				fileType = fileProcess.getFileType(file.getOriginalFilename().toString().toLowerCase());
				savefileString = fileProcess.makeTempContentImageFileCode(stringJson.get("codeHead"), i_fileRequest) + fileType;
				fileProcess.writeFile( file, savefileString );
				saveTempContentImageFileString += savefileString + ",";

				i_fileRequest++;
			}

			
		}

		resultMap.put("result",  true );
		//저장한 파일명 목록을 추가, 마지막 ',' 제거
		resultMap.put("saveTempContentImageFileString",  saveTempContentImageFileString.substring(0, saveTempContentImageFileString.length() - 1) );
		return resultMap;
	}


}
