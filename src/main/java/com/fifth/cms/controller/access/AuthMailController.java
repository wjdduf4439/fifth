package com.fifth.cms.controller.access;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fifth.cms.model.login.AccessVO;
import com.fifth.cms.model.login.AuthMailVO;
import com.fifth.cms.service.login.AuthMailService;
import com.fifth.cms.service.login.access.AccessService;

import jakarta.mail.MessagingException;

@Controller
@RequestMapping("/api")
public class AuthMailController {

    private final AuthMailService authMailService;
    private final AccessService accessService;

    public AuthMailController(AuthMailService authMailService, AccessService accessService) {
        this.authMailService = authMailService;
        this.accessService = accessService;
    }

    @ResponseBody
    @PostMapping("/idCheck.go") 
    public HashMap<String, Object> idCheck(@RequestParam HashMap<String, String> stringJson) throws MessagingException, UnsupportedEncodingException {

        HashMap<String, Object> resultMap = new HashMap<>();
        //System.out.println("authMailController 접근 이메일 대상 : " + stringJson.get("email"));

        List<AccessVO> accessVOList = accessService.checkAccessListId(stringJson);

        if(accessVOList.size() > 0) {
            resultMap.put("result", false);
            resultMap.put("message", "인증된 아이디가 이미 존재합니다.");
            return resultMap;
        }

        String authCode = authMailService.sendSimpleMessage(stringJson.get("id"));
        
        resultMap.put("result", true);
        resultMap.put("authCode", authCode);
        return resultMap;
    }

    @ResponseBody
    @PostMapping("/emailCheck.go") 
    public HashMap<String, Object> emailCheck(@RequestParam HashMap<String, String> stringJson) throws MessagingException, UnsupportedEncodingException {

        HashMap<String, Object> resultMap = new HashMap<>();
        //System.out.println("authMailController 접근 이메일 대상 : " + stringJson.get("email"));

        List<AccessVO> accessVOList = accessService.checkAccessListEmail(stringJson);

        if(accessVOList.size() > 0) {
            resultMap.put("result", false);
            resultMap.put("message", "인증된 이메일이 이미 존재합니다.");
            return resultMap;
        }

        String authCode = authMailService.sendSimpleMessage(stringJson.get("email"));
        
        resultMap.put("result", true);
        resultMap.put("authCode", authCode);
        return resultMap;
    }

    @ResponseBody
    @PostMapping("/nickCheck.go") 
    public HashMap<String, Object> nickCheck(@RequestParam HashMap<String, String> stringJson) throws MessagingException, UnsupportedEncodingException {

        HashMap<String, Object> resultMap = new HashMap<>();
        //System.out.println("authMailController 접근 이메일 대상 : " + stringJson.get("email"));

        List<AccessVO> accessVOList = accessService.checkAccessListNick(stringJson);

        if(accessVOList.size() > 0) {
            resultMap.put("result", false);
            resultMap.put("message", "인증된 닉네임이 이미 존재합니다.");
            return resultMap;
        }

        String authCode = authMailService.sendSimpleMessage(stringJson.get("nick"));
        
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
