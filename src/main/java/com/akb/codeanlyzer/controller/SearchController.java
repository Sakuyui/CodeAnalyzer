package com.akb.codeanlyzer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SearchController {
    @RequestMapping(value = "/search/byFile")
    public String SearchByFile(){
        return "/search_byFile";
    }
}
