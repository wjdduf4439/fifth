package com.fifth.cms.util.template;

import java.util.HashMap;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploadValidateWork {
	
//테이블에 등록된 각 사이트 정보를 가져와서 파일 검증에 필요한 필드를 가져온다.
	//bean 등록을 해야 제대로된 dao에서 값을 가져오고 검증 과정을 거칠 수 있음
	//의존성 주입이 제대로 이루어지지 않았다.
	
	private boolean resultValidateWork = false;
	private String message = "";

	private final Environment environment;
	private final HashMap<String, String> codeheadOptionMap;


	public FileUploadValidateWork(Environment environment, HashMap<String, String> codeheadOptionMap) throws Exception {

		this.environment = environment;
		this.codeheadOptionMap = codeheadOptionMap;

	}

	public String getFileExtension(String file) {
		int dotIndex = file.lastIndexOf('.');
		if (dotIndex > 0 && dotIndex < file.length() - 1) {
			return file.substring(dotIndex + 1);
		}
		return "";
	}
	
	//파일 업로드 파일 검증
	public boolean validatingFileType(List<MultipartFile> files) throws Exception {
		
		//차후 구현
		String fileUploadType = codeheadOptionMap.get("fileUploadType").toLowerCase();
		String validatingFileExtension = "";

		for(MultipartFile validatingFile : files){

			validatingFileExtension = getFileExtension(validatingFile.getOriginalFilename());
			System.out.println("파일 확장자 : " + validatingFileExtension + " > 해당 게시판의 허용 파일 목록 : " + fileUploadType);
			if(!fileUploadType.contains(validatingFileExtension.toLowerCase())){
				return false;
			}
		}
		
		return true;
	}
	
	
	//파일 업로드 개수 검증
	public boolean validatingFileNumber(List<MultipartFile> files) throws Exception {
		
		//차후 구현
		String maxFileUploadNumber = codeheadOptionMap.get("maxFileUploadNumber");

		if(maxFileUploadNumber == null || maxFileUploadNumber.equals("")){
			return false;
		}

		if(files.size() > Integer.parseInt(maxFileUploadNumber)){
			return false;
		}
		
		return true;
	}
	
	//최종 파일 검증 결과값 출력
	public boolean isResultValidateWork() {
		return resultValidateWork;
	}

	public String getMessage() {
		return message;
	}

}
