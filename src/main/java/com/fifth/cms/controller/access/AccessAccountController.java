package com.fifth.cms.controller.access;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/api/accessAccount")
public class AccessAccountController {

    @ResponseBody
    @RequestMapping(value = "/{processMark:list|one|count}", method = {
            RequestMethod.POST }, produces = "application/json")
    public HashMap<String, Object> list(HttpServletRequest req, HttpServletResponse res,
            @RequestParam HashMap<String, String> stringJson, @PathVariable("processMark") String processMark) {

        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", false);

        if ("list".equals(processMark)) {
            // List<AccessAccountVO> accessAccountList =
            // accessAccountService.selectAccessAccountList(stringJson);
            // resultMap.put("resultList", accessAccountList);
        }

        resultMap.put("result", true);

        return resultMap;
    }

}
