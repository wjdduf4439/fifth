package com.fifth.cms.controller.access;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fifth.cms.model.login.AuthMailVO;
import com.fifth.cms.service.login.AuthMailService;

import jakarta.mail.MessagingException;

@Controller
@RequestMapping("/api")
public class AuthMailController {

    private final AuthMailService authMailService;

    public AuthMailController(AuthMailService authMailService) {
        this.authMailService = authMailService;
    }

    @ResponseBody
    @PostMapping("/emailCheck.go") 
    public HashMap<String, Object> emailCheck(@RequestParam HashMap<String, String> stringJson) throws MessagingException, UnsupportedEncodingException {

        HashMap<String, Object> resultMap = new HashMap<>();
        //System.out.println("authMailController 접근 이메일 대상 : " + stringJson.get("email"));

        String authCode = authMailService.sendSimpleMessage(stringJson.get("email"));
        
        resultMap.put("result", true);
        resultMap.put("authCode", authCode);
        return resultMap;
    }

    @ResponseBody
    @PostMapping("/emailAuth.go") 
    public HashMap<String, Object> emailAuth(@RequestParam HashMap<String, String> stringJson) throws MessagingException, UnsupportedEncodingException {

        HashMap<String, Object> resultMap = new HashMap<>();

        AuthMailVO authMailVO = authMailService.getAuthMailInfo(stringJson);

        if(authMailVO == null) {
            resultMap.put("result", false);
            resultMap.put("message", "인증 코드가 존재하지 않습니다.");
            return resultMap;
        }else{
            System.out.println("인증 코드 일치 확인 : " + authMailVO.getAuthkey() + " : " + stringJson.get("authEmailCode"));
            if(authMailVO.getAuthkey().equals(stringJson.get("authEmailCode"))) {
                
                authMailService.deleteAuthMailInfo(authMailVO.getEmail());
            }else{
                resultMap.put("result", false);
                resultMap.put("message", "인증 코드가 일치하지 않습니다.");
                return resultMap;
            }
        }
        
        resultMap.put("message", "인증 완료했습니다.");
        resultMap.put("result", true);

        return resultMap;
    }
    
}
