package com.fifth.cms.util.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Component
public class FileProcess {
	

	private final Environment environment;
	private String SAVE_PATH =  "";
	private String PREFIX_URL =  SAVE_PATH + "/";

	public FileProcess(Environment environment) {
		this.environment = environment;
	}

	//파일을 기입하거나 저장할때 경로는 지정
	public void makedir( MultipartHttpServletRequest req ) {; 
		
		this.SAVE_PATH = environment.getProperty("STORAGEPATH") + "template" + File.separator + req.getHeader("codeHead") + File.separator + "file";
		this.PREFIX_URL =  SAVE_PATH + "/";
		
	}
	
	//파일을 기입하거나 저장할때 경로는 지정
	public void makedir( HashMap<String, String> stringJson ) {
		
		this.SAVE_PATH = environment.getProperty("STORAGEPATH") + "template" + File.separator + stringJson.get("codeHead") + File.separator + "file";
		this.PREFIX_URL =  SAVE_PATH + "/";
		
	}
	
	//파일을 기입하거나 저장할때 현재 시간과 fid, sign에 기반해서 코드를 생성
	public String makeSavingFileCode( String code, int fsign ) {

		LocalDate  savingDate = LocalDate.now();
		DateTimeFormatter savingDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		LocalTime  savingTime = LocalTime.now();
		DateTimeFormatter savingTimeFormatter = DateTimeFormatter.ofPattern("HH-mm-ss");
		
		return code + "_" + fsign + "_" + savingDate.format(savingDateFormatter)+ "_" + savingTime.format(savingTimeFormatter);
	}
	
	public void writeFile(MultipartFile b_filename, String saveFileName) throws IOException, InterruptedException {
		
		byte[] data = b_filename.getBytes();
		
		FileOutputStream fos;
		
		String path = this.PREFIX_URL;
		File Folder = new File(path);
		
		if (!Folder.isDirectory()) {
			
			    Folder.mkdir();
			    
			    Folder.setWritable(true, true);
			    Folder.setReadable(true, true);
			    Folder.setExecutable(true, true);
		}
		
		//실질적으로 파일을 서버 디렉터리에 업로드 및 입력하는 부분
		// System.out.println("writeFile 파일 저장 경로 : " + path);
		// System.out.println("writeFile 파일 이름 : " + saveFileName);
		try {fos = new FileOutputStream(path + saveFileName); fos.write(data); fos.close();} catch(Exception e) {}
		
	}
	
	
	public void deleteFile(String path) throws IOException, InterruptedException {
		
		File deleteFile = new File(path);
		System.out.println("FileProcess deleteFile 파일 삭제 경로 : " + path);
		
		try {
			
			deleteFile.delete();
					
		}catch(Exception e) {
			
			System.out.println(e.getMessage());
			
		}
		
	}	

	public String getSAVE_PATH() {
		return SAVE_PATH;
	}

	public String getPREFIX_URL() {
		return PREFIX_URL;
	}

	public String getMimeType(String fname) {

		// 파일 확장자로 MIME 타입 결정
		String contentType = "";
		if (fname.endsWith(".png"))												contentType = "image/png";
		else if (fname.endsWith(".jpg") || fname.endsWith(".jpeg")) 		contentType = "image/jpeg";
		else if (fname.endsWith(".gif"))											contentType = "image/gif";
		else if (fname.endsWith(".pdf"))											contentType = "application/pdf";
		else if (fname.endsWith(".zip"))											contentType = "application/zip";
		else if (fname.endsWith(".exe"))											contentType = "application/x-msdownload";
		else if (fname.endsWith(".sh"))											contentType = "application/x-sh";
		else if (fname.endsWith(".bat"))											contentType = "application/x-bat";
		else if (fname.endsWith(".tar"))											contentType = "application/x-tar";
		else if (fname.endsWith(".doc") || fname.endsWith(".docx"))		contentType = "application/msword";
		else if (fname.endsWith(".xls") || fname.endsWith(".xlsx"))		contentType = "application/vnd.ms-excel";
		else 																			contentType = "application/octet-stream";  // 기본 바이너리 타입

		return contentType;
	}	
	
}




 