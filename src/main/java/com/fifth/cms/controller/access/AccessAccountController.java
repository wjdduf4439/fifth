package com.fifth.cms.controller.access;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fifth.cms.model.login.AccessVO;
import com.fifth.cms.service.login.access.AccessService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/api/admin/accessAccount")
public class AccessAccountController {

    private final AccessService accessService;

    public AccessAccountController(AccessService accessService) {
        this.accessService = accessService;
    }

    @ResponseBody
    @RequestMapping(value = "/{processMark:list|one|count|approveList}", method = { RequestMethod.POST }, produces = "application/json")
    public HashMap<String, Object> list(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark") String processMark) {

        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", false);

        //승인된 사용자 목록 조회
        if ("list".equals(processMark)) {
            stringJson.put("approve", "Y");
            List<AccessVO> accessAccountList = accessService.selectAccessList(stringJson);
            resultMap.put("resultList", accessAccountList);

        //승인된 사용자 수 조회
        }else if ("one".equals(processMark)) {

            AccessVO accessVO = accessService.selectAccessOne(stringJson);
            resultMap.put("resultList", accessVO);

        //승인 대기 목록 조회
        }else if ("approveList".equals(processMark)) {
            stringJson.put("approve", "N");
            List<AccessVO> accessAccountList = accessService.selectAccessList(stringJson);
            resultMap.put("resultList", accessAccountList);
        }

        resultMap.put("result", true);

        return resultMap;
    }

    @ResponseBody
	@RequestMapping(value = "/{processMark:approve|update|disable|restore|delete}", method = { RequestMethod.POST }, produces = "application/json")
	public HashMap<String, Object> process(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark")String processMark ) {

        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", false);

        String processName = "";
        Integer result = 0;
        
        System.out.println("AccessAccountController process : " + processName + " 전 정보 : " + stringJson.toString());

        if("approve".equals(processMark)) {
            processName = "승인";
        }else if ("update".equals(processMark)) {

        }else if ("disable".equals(processMark)) {

        }else if ("restore".equals(processMark)) {

        }else if ("delete".equals(processMark)) {

        }

        if("approve".equals(processMark)) {
            String[] approveAccountList = stringJson.get("approveAccountList").split(",");

            HashMap<String, Object> approveMap = new HashMap<>();
            approveMap.put("approveAccountList", approveAccountList);
            
            result = accessService.updateApprove(approveMap);
        }else if ("update".equals(processMark)) {

            result = accessService.updateAccount(stringJson);

        }else if ("disable".equals(processMark)) {

            result = accessService.disableApprove(stringJson);

        }else if ("restore".equals(processMark)) {

            

        }else if ("delete".equals(processMark)) {

            result = accessService.deleteAccount(stringJson);

        }

        if(result > 0) {
            resultMap.put("result", true);
            resultMap.put("message", processName + "작업 처리 완료");
        }

        return resultMap;
    }   

}
