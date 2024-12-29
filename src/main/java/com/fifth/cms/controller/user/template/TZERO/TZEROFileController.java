package com.fifth.cms.controller.user.template.TZERO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.HashMap;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fifth.cms.model.template.TemplateZeroFileVO;
import com.fifth.cms.service.template.TZERO.TZEROFileService;
import com.fifth.cms.util.template.FileProcess;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user/template/tzero/file")
public class TZEROFileController {
	
	private final Environment environment;
	private final TZEROFileService tzeroFileService;
		
	public TZEROFileController( Environment environment, TZEROFileService tzeroFileService ) {
		this.environment = environment;
		this.tzeroFileService = tzeroFileService;
		
	}

	// @RequestMapping(value = "/download", method = RequestMethod.GET)
    // public void download( HttpServletResponse res, @RequestParam("uid") String uid, @RequestParam("codeHead") String codeHead ) throws Exception {
		
	// 	HashMap<String, Object> resultMap = new HashMap<String, Object>();
	// 	resultMap.put("result",  false );
		
	// 	System.out.println("다운로드 파일 정보 : " + uid + " / " + codeHead);
		
	// 	TemplateZeroFileVO targetVO = tzeroFileService.getFile(codeHead, uid);
		
	// 	File downloadFile = new File(targetVO.getFpath());
	// 	File realFileName = new File(targetVO.getFname()); 
		
	// 	// 파일이 존재하는지 확인
	// 	if (downloadFile.exists()) {
    //         res.setContentType(Files.probeContentType(downloadFile.toPath()));
    //         res.setContentLength((int) downloadFile.length());

	// 		// 공백을 %20으로 대체
	// 		String encodedFileName = URLEncoder.encode(realFileName.getName(), "UTF-8").replace("+", "%20");
    //         res.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\";");
            
    //         try (InputStream inputStream = new FileInputStream(downloadFile);
    //              OutputStream outputStream = res.getOutputStream()) {
    //             byte[] buffer = new byte[1024];
    //             int bytesRead;
    //             while ((bytesRead = inputStream.read(buffer)) != -1) {
    //                 outputStream.write(buffer, 0, bytesRead);
    //             }
    //             outputStream.flush();
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //             res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "다운로드 작업중 문제가 발생했습니다.");
    //         }
    //     } else {
    //         res.sendError(HttpServletResponse.SC_NOT_FOUND, "다운로드 작업의 파일을 찾을 수 없습니다.");
    //     }
	// }
	
	@RequestMapping(value = "/download", consumes = "application/x-www-form-urlencoded")
    public void download(HttpServletResponse res, @RequestParam("uid") String uid, @RequestParam("codeHead") String codeHead) throws Exception {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result",  false );
		
		TemplateZeroFileVO targetVO = tzeroFileService.getFile(codeHead, uid);
		
		File downloadFile = new File(targetVO.getFpath());
		File realFileName = new File(targetVO.getFname());
		
		System.out.println("다운로드 파일 경로 : " + downloadFile.getAbsolutePath());
		System.out.println("다운로드 파일 정보 : " + realFileName.getName());

		FileProcess fileProcess = new FileProcess(environment);
		String contentType = fileProcess.getMimeType(targetVO.getFname());
		
		System.out.println("파일 크기: " + downloadFile.length() + " bytes");
		System.out.println("다운로드 파일 MIME 타입 : " + contentType);
		
		// 파일이 존재하는지 확인
		if (downloadFile.exists()) {
			// 바이너리 파일의 MIME 타입 설정
			
			res.setContentType(contentType != null ? contentType : "application/octet-stream");
            res.setContentLength((int) downloadFile.length());
			res.setHeader("Content-Transfer-Encoding", "binary");
			res.setHeader("Cache-Control", "no-cache");  // 캐시 비활성화

			// 공백을 %20으로 대체
			String encodedFileName = URLEncoder.encode(realFileName.getName(), "UTF-8").replace("+", "%20");
            res.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\";");
			res.setHeader("fName", encodedFileName);
            
            try (InputStream inputStream = new FileInputStream(downloadFile);
                 OutputStream outputStream = res.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
				long totalBytesRead = 0;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
					totalBytesRead += bytesRead;
					System.out.println("전송된 바이트: " + totalBytesRead);
                }
                outputStream.flush();
				System.out.println("파일 전송 완료. 총 전송된 바이트: " + totalBytesRead);
            } catch (IOException e) {
                e.printStackTrace();
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "다운로드 작업중 문제가 발생했습니다.");
            }
        } else {
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "다운로드 작업의 파일을 찾을 수 없습니다.");
        }
	}
	
}
