package com.akb.codeanlyzer.controller;

import com.akb.codeanlyzer.service.FileSystemServiceImpl;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SourceCodeController {
    @Autowired
    FileSystemServiceImpl fileSystemService;
    @RequestMapping("/sc/file")
    @ResponseBody
    public String getSourceCode(@RequestParam("path") String relativePath){
        String basePath = "D:\\storage\\upload";
        System.out.println(relativePath);
        JSONObject jsonObject = new JSONObject();
        String code = fileSystemService.readFileAsString(basePath + relativePath);
        if("".equals(code)){
            jsonObject.put("status", -1);
            return jsonObject.toJSONString();
        }else{
            jsonObject.put("status", 200);
            jsonObject.put("code", code);
            return jsonObject.toJSONString();
        }


    }
}
