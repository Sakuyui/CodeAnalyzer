package com.akb.codeanlyzer.controller;

import com.akb.codeanlyzer.pojo.User;
import com.akb.codeanlyzer.service.UserServiceImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public String tryLogin(@RequestBody JSONObject param){
        String userName = param.getString("username");
        String password = param.getString("password");
        System.out.println(userName +"," +password);
        User u = userService.tryLogin(userName, password);
        JSONObject jsonObject = new JSONObject();
        if(u == null){
            jsonObject.put("state","-1");
            return jsonObject.toJSONString();
        }
        System.out.println(u.getUserName());

        jsonObject.put("state","200");
        jsonObject.put("uri",u.getuIcon());
        jsonObject.put("nickname",u.getUserName());
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    @ResponseBody
    public String createNewAccount(@RequestBody JSONObject param){
        String userName = param.getString("username");
        String password = param.getString("password");
        System.out.println(userName +"," +password);
        User u = userService.tryLogin(userName, password);
        JSONObject jsonObject = new JSONObject();
        if(u == null){
            jsonObject.put("state","-1");
            return jsonObject.toJSONString();
        }
        System.out.println(u.getUserName());

        jsonObject.put("state","200");
        jsonObject.put("uri",u.getuIcon());
        jsonObject.put("nickname",u.getUserName());
        return jsonObject.toJSONString();
    }
}
