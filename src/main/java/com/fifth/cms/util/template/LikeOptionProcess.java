package com.fifth.cms.util.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fifth.cms.service.template.TemplateUtilService;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class LikeOptionProcess {

	private final Environment environment;
	private final TemplateUtilService templateUtilService;
	
	public LikeOptionProcess(Environment environment, TemplateUtilService templateUtilService){
		this.environment = environment;
		this.templateUtilService = templateUtilService;
	}
	
	public void likeOptionFileCreate(HashMap<String, String> stringJson) throws Exception {
		
		@SuppressWarnings("unchecked")
		HashMap<String, String> likeOptionProcessMap = (HashMap<String, String>) stringJson.clone();

		String likeOptionType = likeOptionProcessMap.get("likeOptionType") != null ? likeOptionProcessMap.get("likeOptionType") : "likeOption";

		String directory = "template" + File.separator + likeOptionProcessMap.get("codeHead") + File.separator + likeOptionType;
		String optionPath = environment.getProperty("STORAGEPATH") + File.separator + directory;
		String fileName = likeOptionProcessMap.get("uid") + ".txt";
		String filePath = optionPath + File.separator + fileName;

		File optionPathDir = new File(optionPath);
		if (!optionPathDir.exists()) {
			optionPathDir.mkdirs();
		}

		File file = new File(filePath);
		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {

			writer.write("like : " + "\n");
			writer.write("dislike : " + "\n");
			IOUtils.closeQuietly(writer);

			likeOptionProcessMap.put("likeOptionPath", fileName);
			templateUtilService.updateLikeOptionPath(likeOptionProcessMap);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateLikeOptionPath(HttpServletRequest req, HashMap<String, String> stringJson, String processMark) {
		
		
		@SuppressWarnings("unchecked")
		HashMap<String, String> likeOptionProcessMap = (HashMap<String, String>) stringJson.clone();

		String likeOptionType = likeOptionProcessMap.get("likeOptionType") != null ? likeOptionProcessMap.get("likeOptionType") : "likeOption";

		String directory = "template" + File.separator + likeOptionProcessMap.get("codeHead") + File.separator + likeOptionType;	
		String optionPath = environment.getProperty("STORAGEPATH") + File.separator + directory;
		String fileName = likeOptionProcessMap.get("uid") + ".txt";
		String filePath = optionPath + File.separator + fileName;

		File likeOptionFile = new File(filePath);
		String accessId = req.getHeader("AccessId");

		try {
			// 파일 내용을 읽어서 각 줄을 저장
			List<String> lines = Files.readAllLines(likeOptionFile.toPath(), StandardCharsets.UTF_8);
			
			// processmark에 따라 해당 라인에 accessId 추가
			if (processMark.equals("like")) {
				lines.set(0, lines.get(0) + accessId + ", ");
			} else if (processMark.equals("dislike")) {
				lines.set(1, lines.get(1) + accessId + ", ");
			}
			
			// 수정된 내용을 파일에 다시 쓰기
			try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(likeOptionFile), StandardCharsets.UTF_8)) {
				for (String line : lines) {
					writer.write(line + "\n");
				}
				writer.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isVaildLikeOption(HttpServletRequest req, HashMap<String, String> stringJson, String processMark) {

		@SuppressWarnings("unchecked")
		HashMap<String, String> likeOptionProcessMap = (HashMap<String, String>) stringJson.clone();

		String likeOptionType = likeOptionProcessMap.get("likeOptionType") != null ? likeOptionProcessMap.get("likeOptionType") : "likeOption";

		String directory = "template" + File.separator + likeOptionProcessMap.get("codeHead") + File.separator + likeOptionType;
		String optionPath = environment.getProperty("STORAGEPATH") + File.separator + directory;
		String fileName = likeOptionProcessMap.get("uid") + ".txt";
		String filePath = optionPath + File.separator + fileName;

		File likeOptionFile = new File(filePath);
		String accessId = req.getHeader("AccessId");

		try {
			// 파일 내용을 읽어서 각 줄을 저장
			List<String> lines = Files.readAllLines(likeOptionFile.toPath(), StandardCharsets.UTF_8);
			
			// processmark에 따라 해당 라인에 accessId 존재 여부 확인
			if (processMark.equals("like")) {
				if (lines.get(0).contains(accessId)) {
					return false;
				}
			} else if (processMark.equals("dislike")) {
				if (lines.get(1).contains(accessId)) {
					return false;
				}
			}
			
			// 수정된 내용을 파일에 다시 쓰기
			try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(likeOptionFile), StandardCharsets.UTF_8)) {
				for (String line : lines) {
					writer.write(line + "\n");
				}
				writer.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	public void deleteLikeOptionPath(HashMap<String, String> stringJson) {
		
		@SuppressWarnings("unchecked")
		HashMap<String, String> likeOptionProcessMap = (HashMap<String, String>) stringJson.clone();

		String likeOptionType = likeOptionProcessMap.get("likeOptionType") != null ? likeOptionProcessMap.get("likeOptionType") : "likeOption";
		String directory = "template" + File.separator + likeOptionProcessMap.get("codeHead") + File.separator + likeOptionType;
		String optionPath = environment.getProperty("STORAGEPATH") + File.separator + directory;
		String fileName = likeOptionProcessMap.get("uid") + ".txt";
		String filePath = optionPath + File.separator + fileName;

		//System.out.println("LikeOptionProcess deleteLikeOptionPath filePath : " + filePath);

		File likeOptionFile = new File(filePath);
		if (likeOptionFile.exists()) {
			likeOptionFile.delete();
		}
	}

}
