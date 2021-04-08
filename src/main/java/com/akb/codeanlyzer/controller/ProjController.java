package com.akb.codeanlyzer.controller;

import com.akb.codeanlyzer.pojo.JavaProject;
import com.akb.codeanlyzer.service.ProjectService;
import com.akb.codeanlyzer.service.UserServiceImpl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import network.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProjController {
    @RequestMapping("/proj/{userName}/main")
    public String projMain(@PathVariable String userName, HttpSession session){

        return "proj-main";
    }


    @RequestMapping("/proj/img/{pName}/getHierarchy")
    @ResponseBody
    public String getHierarchicalClustering(@RequestParam("isInd") int isInd, @PathVariable String pName,
                                            @RequestParam("belongName") String belongName) throws Exception {
        JSONObject object = new JSONObject();
        String basePath = "D:\\storage\\upload\\";
        String uri =  (isInd == 1? "user\\" :"org\\") + belongName + "\\" + pName + "\\" + pName + "_himg.png";
        String fullPath = basePath + uri;
        File imgFile = new File(fullPath);
        if(imgFile.exists()){
            object.put("state", 200);
            return object.toJSONString();
        }else{
            Map<String, String> param = new HashMap<>();
            param.put("isInd", isInd + "");
            param.put("projName", pName);
            param.put("belongedName", belongName);
            //请求python后端
            String s = Tools.httpRequest("http://127.0.0.1:5001/img/hierarchy",
                    param, false);

        }




        object.put("state", -1);
        return object.toJSONString();
    }

    @Autowired
    ProjectService projectService;
    @Autowired
    UserServiceImpl userService;
    @RequestMapping(value = "/proj/getProjects")
    @ResponseBody
    //获取个人工程和能够访问组织工程
    public JSONObject getProjects(HttpSession session){
        String token = (String) session.getAttribute("token_login");
        String uName = (String) session.getAttribute("username");
        List<String> projects =  projectService.getIndividualProjects(uName);
        JSONArray indProjectArray = new JSONArray();
        for(String s : projects){
            indProjectArray.add(s);
        }
        JSONObject object = new JSONObject();
        object.put("indProjects", indProjectArray);
        JSONArray orgProjectArray = new JSONArray();
        List<String> orgs = projectService.getAccessibleOrgProjects(uName);
        for(String org : orgs){
            System.out.println("cur = " + org);
            String[] tmp = org.replace("(","").replace(")","").split(",");
            JSONObject tmpJSONObject = new JSONObject();
            tmpJSONObject.put("pName", tmp[0]);
            tmpJSONObject.put("orgName", tmp[1]);
            orgProjectArray.add(tmpJSONObject);
        }
        object.put("orgProjects", orgProjectArray);
        return object;
    }


    @RequestMapping(value = "/proj/p/{pName}")
    public String getProjectPage(@PathVariable("pName") String pName, @RequestParam("ind")int isIndividual,
                                 @RequestParam(value = "org", required = false) String orgName, HttpSession session){
        String token = (String) session.getAttribute("token_login");
        String uName = (String) session.getAttribute("username");
        session.setAttribute("pName", pName);
        session.setAttribute("isInd", isIndividual);

        if(isIndividual != 1){
           if(orgName == null){
               return "/";
           }else{
               session.setAttribute("porg", orgName);
           }
        }
        System.out.println("isIndividual = " + isIndividual);
        return "project-page";
    }
    @RequestMapping(value = "/proj/p/{pName}/getfiles")
    @ResponseBody
    public JSONObject getProjectFiles(@PathVariable("pName") String pName, @RequestParam(value = "ind")int isIndividual,
            @RequestParam(value = "org", required = false) String orgName
            , HttpSession session){
        String token = (String) session.getAttribute("token_login");
        String uName = (String) session.getAttribute("username");
        if(isIndividual != 1){
            if(orgName == null){
                return new JSONObject();
            }
        }
        JavaProject javaProjects = projectService.getProjectEntity(uName, pName, orgName);
        JSONArray array = new JSONArray();

        for(String s : javaProjects.getFilesName()){
            array.add(s);
        }
        JSONObject object = new JSONObject();
        object.put("files", array);
        return object;
    }
}
