package com.akb.codeanlyzer.controller;

import com.akb.codeanlyzer.service.FileSystemServiceImpl;
import com.alibaba.fastjson.JSONObject;
import network.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.ast.ASTExtractor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

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
    @RequestMapping("/sc/ast")
    @ResponseBody
    public String getSourceCodeAst(@RequestParam("fileRelativePath") String relativePath, @RequestParam("projPath")String projPath){
        String basePath = "D:\\storage\\upload";
        String fullPath = basePath + projPath + relativePath;
        JSONObject jsonObject = new JSONObject();
        String code = fileSystemService.readFileAsString(fullPath);
        String astFilePath = fullPath.split("\\.")[0] + ".aststr";
        String f = fileSystemService.readFileAsString(astFilePath);
        System.out.println(astFilePath);
        if("".equals(code)){
            jsonObject.put("status", -1);
            return jsonObject.toJSONString();
        }else{
            if("".equals(f)){
                //产生ast
                String ast = ASTExtractor.getAst(code);
                System.out.println(ast);
                fileSystemService.writeStringToFile(astFilePath, ast);
            }else{
                System.out.println("exists: \n" + f);
            }

            Map<String, String> param = new HashMap<>();
            param.put("astPath", astFilePath);
            param.put("relativePath", relativePath);

            try {
                String s = Tools.httpRequest("http://127.0.0.1:5001/ast/generate",
                        param, false);
                jsonObject.put("status", 200);
                JSONObject res = JSONObject.parseObject(s);
                jsonObject.put("uri", res.getString("uri"));
                return jsonObject.toJSONString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            jsonObject.put("status", -1);
            return jsonObject.toJSONString();

        }


    }
}
