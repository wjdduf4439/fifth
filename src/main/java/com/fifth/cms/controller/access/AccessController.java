package com.fifth.cms.controller.access;

import java.util.HashMap;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fifth.cms.model.login.AccessVO;
import com.fifth.cms.service.login.access.AccessService;
import com.fifth.cms.util.access.AccessInfo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping(value = "/api/common")
@Controller
public class AccessController {

	private final AccessService accessService;
	private final BCryptPasswordEncoder passwordEncoder;

	AccessInfo accessInfo = new AccessInfo();

	public AccessController(AccessService accessService, BCryptPasswordEncoder passwordEncoder) {
		this.accessService = accessService;
		this.passwordEncoder = passwordEncoder;
	}

	@ResponseBody
	@RequestMapping(value = "/accLogout.go", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> personalControllerMethod(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);

		HashMap<String, String> accessInfoMap = accessInfo.getAccessInfo(req);

		Integer result = accessService.updateBlankRefreshToken(accessInfoMap);

		if (result > 0) {
			resultMap.put("result", true);
			resultMap.put("message", "로그아웃 되었습니다.");
		}
		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = "/accRegist.go", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> accRegist(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", false);

		//System.out.println("accRegist.go stringJson : " + stringJson.toString());
		String encodePw = passwordEncoder.encode(stringJson.get("pw"));
		// System.out.println("accRegist.go stringJson.get(pw) : " + stringJson.get("pw"));
		// System.out.println("accRegist.go 입력할 stringJson.get(pw) : " + encodePw);

		stringJson.put("pw", encodePw);
		//닉네임과 태그에 #이 안들어가게 하기
		String nick = stringJson.get("nick").replace("#", "");
		String tag = stringJson.get("tag").replace("#", "");
		stringJson.put("nick", nick + "#" + tag);
		stringJson.put("role", "ROLE_user");
		stringJson.put("authority", "user");

		//이메일 중복감지
		AccessVO checkEmailVO = accessService.checkEmail(stringJson);
		if(checkEmailVO != null) {
			resultMap.put("result", false);
			resultMap.put("message", "중복된 이메일이 존재해 회원가입이 불가능합니다.");
			return resultMap;
		}

		Integer result = accessService.insertAccount(stringJson);

		if (result > 0) {
			resultMap.put("result", true);
			resultMap.put("message", "회원가입 되었습니다.");
		}

		return resultMap;
	}

	

    @ResponseBody
    @RequestMapping("/nickNameCheck.go")
    public HashMap<String, Object> nickNameCheck(@RequestParam HashMap<String, String> stringJson) throws Exception {

        HashMap<String, Object> resultMap = new HashMap<>();

        AccessVO result = accessService.checkNickName(stringJson);

        if(result != null) {
            resultMap.put("result", false);
            resultMap.put("message", "닉네임 중복입니다.");
        }else{
            resultMap.put("result", true);
            resultMap.put("message", "닉네임 사용 가능합니다.");
        }

        return resultMap;
    }


}
