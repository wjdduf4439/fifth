package com.fifth.cms.util.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public void makedir( MultipartHttpServletRequest req, String type ) {
		
		this.SAVE_PATH = environment.getProperty("STORAGEPATH") + "template" + File.separator + req.getHeader("codeHead") + File.separator + type;
		this.PREFIX_URL =  SAVE_PATH + "/";
		File directory = new File(this.PREFIX_URL);
		if(!directory.exists()) {
			directory.mkdirs();
		}
		System.out.println(this.getClass().getName() + " makedir 파일 경로 : " + this.PREFIX_URL);
		
	}
	
	//파일을 기입하거나 저장할때 경로는 지정
	public void makedir( HashMap<String, String> stringJson, String type ) {
		
		this.SAVE_PATH = environment.getProperty("STORAGEPATH") + "template" + File.separator + stringJson.get("codeHead") + File.separator + type;
		this.PREFIX_URL =  SAVE_PATH + "/";
		File directory = new File(this.PREFIX_URL);
		if(!directory.exists()) {
			directory.mkdirs();
		}
		System.out.println(this.getClass().getName() + " makedir 파일 경로 : " + this.PREFIX_URL);
		
	}
	
	//파일을 기입하거나 저장할때 현재 시간과 fid, sign에 기반해서 코드를 생성
	public String makeSavingFileCode( String code, int fsign ) {

		LocalDate  savingDate = LocalDate.now();
		DateTimeFormatter savingDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		LocalTime  savingTime = LocalTime.now();
		DateTimeFormatter savingTimeFormatter = DateTimeFormatter.ofPattern("HH-mm-ss");
		
		return code + "_" + fsign + "_" + savingDate.format(savingDateFormatter)+ "_" + savingTime.format(savingTimeFormatter);
	}

	//파일을 기입하거나 저장할때 현재 시간과 fid, sign에 기반해서 코드를 생성
	public String makeTempContentImageFileCode( String code, int fsign ) {

		LocalDate  savingDate = LocalDate.now();
		DateTimeFormatter savingDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		LocalTime  savingTime = LocalTime.now();
		DateTimeFormatter savingTimeFormatter = DateTimeFormatter.ofPattern("HH-mm-ss");
		
		return code + "_" + "tempContentImage" + "_" + fsign + "_" + savingDate.format(savingDateFormatter)+ "_" + savingTime.format(savingTimeFormatter);
	}
	
	public void writeFile(MultipartFile b_filename, String saveFileName) throws IOException, InterruptedException {

		//byte[] data = b_filename.getBytes();
		byte[] fileData = null;
		try (InputStream inputStream = b_filename.getInputStream()) {
			// 예시: 바이트 배열로 변환
			fileData = inputStream.readAllBytes();
		}
		
		FileOutputStream fos;
		
		String path = this.PREFIX_URL;
		File Folder = new File(path);
		
		if (!Folder.isDirectory()) {
			    
			    Folder.setWritable(true, true);
			    Folder.setReadable(true, true);
			    Folder.setExecutable(true, true);
		}
		
		//실질적으로 파일을 서버 디렉터리에 업로드 및 입력하는 부분
		System.out.println("writeFile 파일 저장 경로 : " + path);
		System.out.println("writeFile 파일 이름 : " + saveFileName);
		System.out.println("파일 저장 위치 : " + (path + saveFileName.toString()));
		try {fos = new FileOutputStream(path + saveFileName.toString()); fos.write(fileData); fos.close();} catch(Exception e) {}
		
	}

	public List<String> getTempContentImageFileNameList(HashMap<String, String> stringJson) {

		// 글내용에서 추출된 이미지 파일명을 담을 리스트
		List<String> imageNames = new ArrayList<>();
		
		// 정규 표현식: "src=" 뒤에 오는 "..." 안에서 TEMP_tempContentImage로 시작하는 파일명을 찾음
		//  - src\s*=\s*["'] : src= 또는 src = 과 같은 패턴 매칭 (공백 고려)
		//  - ([^"']*TEMP_tempContentImage[^"']*) : TEMP_tempContentImage로 시작하여, " 또는 ' 전까지의 모든 문자
		//  - ['\"] : " 또는 ' 로 끝나는 패턴 매칭
		//  TEMP는 코드헤드 이름이므로 코드헤드 이름으로 변경
		String regex = "src\\s*=\\s*['\"]([^'\"]*"+ stringJson.get("codeHead") + "_tempContentImage[^'\"]*)['\"]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(stringJson.get("processContext"));

		
		while (matcher.find()) {
			// 예: http://localhost:3003/images/TEMP/.../TEMP_tempContentImage_0_2025-01-12_08-53-56.png
			// 전체 URL에서 파일명만 잘라내기 위해 마지막 '/' 이후 부분만 추출
			String fullPath = matcher.group(1); // 그룹 1은 실제 URL 또는 경로
			String fileName = fullPath.substring(fullPath.lastIndexOf('/') + 1);
			imageNames.add(fileName);
		}

		return imageNames;

	}

	
	public boolean postContentImageFile(List<String> saveTempContentImageFileStringList, String codeHead) throws IOException, InterruptedException {

		// String saveTempContentImageFileString = stringJson.get("saveTempContentImageFileString");
		// String[] saveTempContentImageFileStringArray = saveTempContentImageFileString.split(",");

		//storage의 경로를 찾아서 해당 이미지를 탐색
		//storage 경로는 STORAGEPATH 환경변수에 저장되어 있음
		String noPostStoragePath = environment.getProperty("STORAGEPATH") + "template" + "/" + codeHead + environment.getProperty("TEMP_CONTENT_IMAGE_DIR");
		String postStoragePath = environment.getProperty("STORAGEPATH") + "template" + "/" + codeHead + environment.getProperty("SAVE_CONTENT_IMAGE_DIR");
		System.out.println("postContentImageFile noPostStoragePath : " + noPostStoragePath + "// postStoragePath : " + postStoragePath);
		System.out.println("saveTempContentImageFileStringList size : " + saveTempContentImageFileStringList.size());
		
		//이동할 이미지가 없으면 이상없음 true 보내기
		if(	saveTempContentImageFileStringList.size() <= 0 || 
			( "".equals(saveTempContentImageFileStringList.get(0)) && saveTempContentImageFileStringList.size() == 1 )
			) return true;

		File noPostStoragePathFile = new File(noPostStoragePath);
		if(noPostStoragePathFile.exists() && noPostStoragePathFile.isDirectory()) {
			// 디렉터리 내의 파일 목록을 가져옵니다.
			File[] files = noPostStoragePathFile.listFiles();
			if (files != null) {
				for (File file : files) {
					// 각 파일의 이름을 로그로 출력합니다.
					for(String tempContentImageFileString : saveTempContentImageFileStringList) {
						if(file.getName().equals(tempContentImageFileString)) {
							// System.out.println("\t옮겨야 되는 파일 이름: " + file.getName());
							file.renameTo(new File(postStoragePath + file.getName()));
							// System.out.println("\t옮긴 파일 이름: " + postStoragePath + file.getName());
						}
					}
				}
			} else {
				System.out.println("디렉터리 내에 파일이 없습니다.");
				return false;
			}
		} else {
			System.out.println("지정된 경로가 디렉터리가 아니거나 존재하지 않습니다.");
			return false;
		}


		return true;
	}


	public void deleteTempContentImageFile(List<String> removeTempContentImageFileStringList, String codeHead) throws IOException, InterruptedException {
		
		String noPostStoragePath = environment.getProperty("STORAGEPATH") + "template" + "/" + codeHead + environment.getProperty("TEMP_CONTENT_IMAGE_DIR");
		System.out.println("deleteTempContentImageFile noPostStoragePath : " + noPostStoragePath);

		File noPostStoragePathFile = new File(noPostStoragePath);
		if(noPostStoragePathFile.exists() && noPostStoragePathFile.isDirectory()) {
			for(String tempContentImageFileString : removeTempContentImageFileStringList) {
				deleteFile(noPostStoragePath + tempContentImageFileString);
			}
		} else {
			System.out.println("지정된 경로가 디렉터리가 아니거나 존재하지 않습니다.");
		}

	}

	public boolean deletePostContentImageFile(List<String> dropPostContentImageFileStringList, String codeHead) throws IOException, InterruptedException {
		
		String postedStoragePath = environment.getProperty("STORAGEPATH") + "template" + "/" + codeHead + environment.getProperty("SAVE_CONTENT_IMAGE_DIR");
		System.out.println("deletePostContentImageFile postedStoragePath : " + postedStoragePath);

		if(dropPostContentImageFileStringList.size() <= 0 || 
			( "".equals(dropPostContentImageFileStringList.get(0)) && dropPostContentImageFileStringList.size() == 1 )
			) return false;

		File postedStoragePathFile = new File(postedStoragePath);
		if(postedStoragePathFile.exists() && postedStoragePathFile.isDirectory()) {
			for(String dropPostContentImageFileString : dropPostContentImageFileStringList) {
				deleteFile(postedStoragePath + dropPostContentImageFileString);
			}
			return true;
		} else {
			return false;
		}

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

	public String getFileType(String fname) {

		// 파일 확장자로 MIME 타입 결정
		String fileType = "";
		if (fname.endsWith(".png"))												fileType = "png";
		else if (fname.endsWith(".jpg") || fname.endsWith(".jpeg")) 		fileType = "jpg";
		else if (fname.endsWith(".gif"))											fileType = "gif";
		else if (fname.endsWith(".pdf"))											fileType = "pdf";
		else if (fname.endsWith(".zip"))											fileType = "zip";
		else if (fname.endsWith(".exe"))											fileType = "exe";
		else if (fname.endsWith(".sh"))											fileType = "sh";
		else if (fname.endsWith(".bat"))											fileType = "bat";
		else if (fname.endsWith(".tar"))											fileType = "tar";
		else if (fname.endsWith(".doc") || fname.endsWith(".docx"))		fileType = "doc";
		else if (fname.endsWith(".xls") || fname.endsWith(".xlsx"))		fileType = "xls";
		else 																			fileType = "unknown";  // 기본 바이너리 타입

		return "." + fileType;
	}	
	
}




 