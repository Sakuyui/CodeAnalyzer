package com.akb.codeanlyzer.controller;

import com.alibaba.fastjson.JSONObject;
import network.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import se.ast.ASTExtractor;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class SearchController {
    @Autowired
    RedisTemplate redisTemplate;
    @RequestMapping(value = "/search/byFile")
    @ResponseBody
    public String SearchByFile(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam(value = "page",defaultValue = "0",required = false)
                               int page, HttpSession session, @RequestParam(value = "fUUID", required = false) String fUuid) {
        String uuid = file != null ? UUID.randomUUID().toString() : fUuid;
        System.out.println("fuuid = " + uuid + " " + fUuid);
        File f = new File("D:\\storage\\upload\\tmp\\" + uuid  +".java");
        try{
            if(file != null){
                file.transferTo(f);
            }

            redisTemplate.opsForValue().set("f_uuid_" + uuid, uuid,300, TimeUnit.SECONDS);
            Map<String, String> param = new HashMap<>();
            param.put("f_uuid", uuid);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            String code = new String(bis.readAllBytes());
            String token = ASTExtractor.GetTokenizedSymbolicsNames(code);
            param.put("tokens", token);
            param.put("page", "" + page);
            String s = Tools.httpRequest("http://127.0.0.1:5001/feature/byFile",
                    param, false);
            session.setAttribute("s_result", s);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", 200);
            return jsonObject.toJSONString();

        }catch (Exception e){
            if(f.exists()){
                f.delete();
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", -1);
            return jsonObject.toJSONString();
        }



    }


    @RequestMapping("/searchResult")
    public String resultPage(){
        return "search_byFile";
    }
}
