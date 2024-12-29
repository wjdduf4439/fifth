package com.fifth.cms.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONObject;
import org.springframework.core.env.Environment;

public class CodeProcess {

	private final Environment environment;
	private final String directory = "codehead/options";
	private final String storageDirectory = "template";
	private final String optionPath;
	private final String storagePath;

	public CodeProcess(Environment environment) {
		this.environment = environment;
		this.optionPath = environment.getProperty("STORAGEPATH") + File.separator + directory;
		this.storagePath = environment.getProperty("STORAGEPATH") + File.separator + storageDirectory;
	}

	public static String createCode(String maxCode, String primaryCode, String processCode) {
		String result;
		int underscoreIndex;
		int hyphenIndex;
		
		// while문 인덱스
		int i = 1;
		
		underscoreIndex = maxCode.indexOf('_');
		hyphenIndex = maxCode.indexOf('-');
		
		//코드의 머리 제거
		maxCode = maxCode.replace(primaryCode, "");
		if (underscoreIndex > 0) {
			maxCode = maxCode.substring(underscoreIndex + 1);
		}
		if (hyphenIndex > 0) {
			maxCode = maxCode.replace("-", "");
		}
		if (maxCode.isEmpty()) {
			maxCode = "0";
		}
		
		result = primaryCode;
		Integer maxcodeInteger = Integer.parseInt(maxCode);
		
		System.out.println("CodeProcess createCode maxcodeInteger : " + maxcodeInteger);
		//System.out.println("CodeProcess createCode parameter : 20 - " + primaryCode.length() + " - " + Integer.toString(maxcodeInteger+1).length());
		
		while (i <= 20 - primaryCode.length() - ( Integer.toString(maxcodeInteger+1).length())) {
			if( processCode.equals("table") )	result = result.concat("0");
			else								result = result.concat("-");
			i++;
		}
		
		int maxCodeInt = Integer.parseInt(maxCode) + 1;
		result = result.concat(String.valueOf(maxCodeInt));
		
		return result;
	}	

	public String optionPathRead(HashMap<String, String> stringJson, String code) {
		String fileName = code + ".json";
		String filePath = optionPath + File.separator + fileName;

		try {
			// 파일 내용을 읽고 문자열로 변환
			String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
			return content;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public HashMap<String, String> getOptionPathJson(HashMap<String, String> stringJson) {

		String code = null == stringJson.get("code")|| "".equals(stringJson.get("code")) ? stringJson.get("codeHead") : stringJson.get("code");

		String fileName = code + ".json";
		String filePath = optionPath + File.separator + fileName;
		HashMap<String, String> resultMap = new HashMap<>();

		File optionFile = new File(filePath);
		if(optionFile.exists()) {
			try {
				// 파일 내용을 문자열로 읽기
				String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
				
				// JSON 문자열을 JSONObject로 파싱
				JSONObject jsonObject = new JSONObject(content);
				
				// JSONObject의 모든 키를 순회하면서 HashMap에 추가
				for (String key : jsonObject.keySet()) {
					String value = jsonObject.get(key).toString();
					resultMap.put(key, value);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return resultMap;
	}

	//codehead 테이블의 optionPath 컬럼에 저장된 경로를 통해 파일 존재 여부를 확인하고, 존재하지 않을 경우 파일을 생성합니다.
	public String optionPathJsonCreate(HashMap<String, String> stringJson) {
		
		String fileName = stringJson.get("code") + ".json";
		
		File file = new File(optionPath);
		if(!file.exists()) {
			file.mkdirs();
		}

		File optionFile = new File(optionPath + File.separator + fileName);
		if(!optionFile.exists()) {
			try {
				optionFile.createNewFile();

				// 순서가 유지되는 Map 생성
                Map<String, Object> orderedMap = new LinkedHashMap<>();
                orderedMap.put("disableAllow", "N");
                orderedMap.put("placerow", 0);
                orderedMap.put("placeName", "");
                orderedMap.put("placewidth", 0);
                orderedMap.put("maxFileUploadNumber", 1);
                orderedMap.put("fileUploadType", "");
                orderedMap.put("replyLimit", 10);

				// LinkedHashMap을 사용하여 JSONObject 생성
                JSONObject jsonObject = new JSONObject(orderedMap);

				try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(optionFile), StandardCharsets.UTF_8)) {

					writer.write(jsonObject.toString());
					IOUtils.closeQuietly(writer);
				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//파일 이름 반환해서 optionPath 컬럼에 저장시키게 유도
		return fileName;
	}

	public String optionPathJsonUpdate(HashMap<String, String> stringJson, String optionContent) {

		String fileName = stringJson.get("code") + ".json";
		String filePath = optionPath + File.separator + fileName;

		File optionFile = new File(filePath);
		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(optionFile), StandardCharsets.UTF_8)) {
			writer.write(optionContent);
			IOUtils.closeQuietly(writer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void optionPathJsonDelete(HashMap<String, String> stringJson) {

		String filePath = optionPath + File.separator + stringJson.get("optionPath");

		//System.out.println("CodeProcess optionPathJsonDelete filePath : " + filePath);

		File optionFile = new File(filePath);
		if(optionFile.exists()) {
			optionFile.delete();
		}
	}

	public void storagePathDropStatusUpdate(HashMap<String, String> stringJson) {

		String code = null == stringJson.get("code")|| "".equals(stringJson.get("code")) ? stringJson.get("codeHead") : stringJson.get("code");

		String jsonFilePath = optionPath + File.separator + stringJson.get("optionPath");
		System.out.println("CodeProcess jsonFilePath : " + jsonFilePath);
		File jsonFile = new File(jsonFilePath);
		if(jsonFile.exists()) {
			String newFilePath = optionPath + File.separator + stringJson.get("optionPath") + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			System.out.println("CodeProcess jsonFilePath > newFilePath : " + newFilePath);
			directoryRename(jsonFilePath, newFilePath);
		}

		String storageFilePath = storagePath + File.separator + code;
		System.out.println("CodeProcess storageFilePath : " + storageFilePath);
		File storageFile = new File(storageFilePath);
		if(storageFile.exists()) {
			String newFilePath = storagePath + File.separator + code + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			System.out.println("CodeProcess storageFilePath > newFilePath : " + newFilePath);
			directoryRename(storageFilePath, newFilePath);
		}


	}

	public void directoryRename(String oldDirectory, String newDirectory) {

		File oldFile = new File(oldDirectory);
		if(oldFile.exists()) {
			File newFile = new File(newDirectory);
			oldFile.renameTo(newFile);
		}
	}
}




 