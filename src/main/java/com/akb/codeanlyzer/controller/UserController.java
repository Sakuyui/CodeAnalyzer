package com.akb.codeanlyzer.controller;

import com.akb.codeanlyzer.pojo.User;
import com.akb.codeanlyzer.service.UserServiceImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public String tryLogin(@RequestBody JSONObject param, HttpServletRequest request, HttpSession session){
        String userName = param.getString("username");
        String password = param.getString("password");
        Cookie[] cookies = request.getCookies();
        Object t = session.getAttribute("token_login");
        Object uname = session.getAttribute("username");


        if(t != null && uname != null){
            if(userService.checkToken( (String) t, (String) uname)){
                System.out.println("用户 "+uname + " with token = " + t + "已登入" );
            }else{
                System.out.println("已有token但无效");
            }
        }



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


        String token = (userService.setUserLoginState(true, u.getUserName()));

        Cookie userIconUriCookie = new Cookie("userIconUri", u.getuIcon());
        session.setAttribute("token_login", token);
        session.setAttribute("username", u.getUserName());
        session.setAttribute("userIconUri", u.getuIcon());

        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "/tokenLogin", method = RequestMethod.POST)
    @ResponseBody
    public User getUserInfByToken(HttpSession session){
        Object t = session.getAttribute("token_login");
        Object uname = session.getAttribute("username");
        User u = new User();
        u.setuIcon("default.jpg");
        System.out.println(t);
        if(t == null || uname == null) return u;
        User uw = userService.tryLoginByToken((String)t, (String)uname);
        if (uw!= null){
            u = uw;
        }
        return u;
    }


    @RequestMapping(value = "/signout")
    public void signOut(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException{
        Object userName = session.getAttribute("username");
        session.removeAttribute("token_login");
        session.removeAttribute("username");
        if(userName != null){
            userService.setUserLoginState(false, (String) userName);
        }
        response.sendRedirect("/");
    }

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public String createNewAccount(@RequestParam("file")MultipartFile uploadImage,@RequestParam("username")String userName
        ,@RequestParam("password")String pwd, @RequestParam("nickname")String nickname, @RequestParam("phone") String phone)  throws IOException{

        System.out.println("reg: username = " + userName);
        System.out.println("reg: pwd = " + pwd);
        System.out.println("reg: nickname = " + nickname);
        System.out.println("reg: phone = " + phone);
        JSONObject jsonObject = new JSONObject();
        User u = new User();


        u.setNickName(nickname);
        u.setPwd(pwd);
        u.setUserName(userName);

        int result;
        if(uploadImage == null || uploadImage.getBytes().length < 128){
            System.out.println("img = null");
            u.setuIcon("default.jpg");
            result = userService.createNewUser(u);
        }else{
            String suffix = uploadImage.getContentType().toLowerCase();//图片后缀，用以识别哪种格式数据
            suffix = suffix.substring(suffix.lastIndexOf("/") + 1);
            String userImgPath = "D:\\storage\\usericon\\";
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
            u.setuIcon(fileName);
            result = userService.createNewUser(u);

            if(result != 1){
                jsonObject.put("state", -2);
                jsonObject.put("desc", "用户已存在");
                return jsonObject.toJSONString();
            }
            File targetFile = new File(userImgPath, fileName);
            uploadImage.transferTo(targetFile);


            System.out.println("img saved");

            u.setuIcon(fileName);


        }

        if(result > 0){
            jsonObject.put("state", 200);
            jsonObject.put("desc", "success");
        }else{
            jsonObject.put("state", -1);
            jsonObject.put("desc", "数据库错误");
        }

        System.out.println("!!!");
        return jsonObject.toJSONString();
    }


}
