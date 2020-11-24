package com.akb.codeanlyzer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UploadController {
    @RequestMapping("/upload/main")
    public String uploadPage(){
        return "/upload-main";
    }
}
