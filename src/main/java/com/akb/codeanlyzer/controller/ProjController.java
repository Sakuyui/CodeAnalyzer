package com.akb.codeanlyzer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class ProjController {
    @RequestMapping("/proj/{userName}/main")
    public String projMain(@PathVariable String userName, HttpSession session){

        return "proj-main";
    }
}
