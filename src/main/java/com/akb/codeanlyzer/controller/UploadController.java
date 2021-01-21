package com.akb.codeanlyzer.controller;

import com.akb.codeanlyzer.UploadManager;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.UUID;

@Controller
public class UploadController {
    @RequestMapping("/upload/main")
    public String uploadPage(){
        return "/upload-main";
    }





    /**在上传文件前，首先使用这个来请求。发送要上传的文件数量和信息。服务器端检查是否已经存在进行中的上传请求。如果有的话，直接返回状态字。 **/
    @RequestMapping(value = "/upload/{userID}/uploadRequire")
    @ResponseBody
    public String uploadRequire(@PathVariable String userID, @RequestParam("fileCount") int count, HttpSession session){
        String upLoadID =  UUID.randomUUID().toString().replaceAll("-", "");
        if(UploadManager.getInstance().userNameUploadIDMap.containsKey(userID)){
            JSONObject object = new JSONObject();
            object.put("state", 100);
            object.put("desc", "Upload Session Already Exist.");
            return object.toJSONString();
        }
        JSONObject object = new JSONObject();
        object.put("state", 200);
        object.put("desc", "OK");
        object.put("upload_id", upLoadID);
        UploadManager.getInstance().userNameUploadIDMap.put(userID, upLoadID);
        session.setAttribute("upLoadID", upLoadID);
        return object.toJSONString();
    }


    @RequestMapping(value = "/upload/{userID}/getProgress", method = RequestMethod.POST)
    @ResponseBody
    public String getProgress(@PathVariable String userID, HttpSession session){
        JSONObject object = new JSONObject();
        return object.toJSONString();
    }

    @RequestMapping(value = "/upload/{userID}/doUpload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@PathVariable String userID, @RequestParam("file") MultipartFile file, @RequestParam("relativePath") String relativePath) throws IOException {
        String basePath = "D:\\storage\\upload\\tmp\\" + userID + "\\";
        System.out.println(userID + " " + relativePath +" ");
        File f = new File(basePath + relativePath);
        File tf = f.getParentFile();
        Stack<File> stack = new Stack<>();
        while(!tf.exists()){
           stack.push(tf);
           tf = tf.getParentFile();
        }
        while(!stack.isEmpty()){
            tf = stack.pop();
            tf.mkdir();
        }
        file.transferTo(f);


        JSONObject object = new JSONObject();
        object.put("state", 200);
        object.put("desc", "OK");
        return object.toJSONString();
    }
}
