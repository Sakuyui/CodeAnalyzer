package com.akb.codeanlyzer.controller;

import network.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class SearchController {
    @Autowired
    RedisTemplate redisTemplate;
    @RequestMapping(value = "/search/byFile")
    public String SearchByFile(@RequestParam("file") MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        File f = new File("D:\\storage\\upload\\tmp\\" + uuid  +".java");
        try{
            file.transferTo(f);
            redisTemplate.opsForValue().set("f_uuid_" + uuid, uuid,300, TimeUnit.SECONDS);
            Map<String, String> param = new HashMap<>();
            param.put("f_uuid", uuid);
            String s = Tools.httpRequest("http://127.0.0.1:5001/feature/byFile",
                    param, false);
            System.out.println(s);
        }catch (Exception e){
            if(f.exists()){
                f.delete();
            }
            return "/";
        }

        return "/search_byFile";
    }


}
