package com.akb.codeanlyzer.service;

import com.akb.codeanlyzer.network.SocketMQManager;
import com.akb.codeanlyzer.network.SocketRequest;
import com.akb.codeanlyzer.pojo.JavaProject;
import network.SocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProjectService implements IProjectService{
    @Autowired
    SocketClient socketClient;
    @Autowired
    SocketMQManager socketMQManager;
    @Override
    public List<String> getIndividualProjects(String uid) {
        int seq = socketMQManager.createNewRequest(381, uid);
        socketClient.sendMessage("<Type>381</><uid>" + uid + "</><seq>" + seq + "</>");
        SocketRequest r = null;
        while((r = socketMQManager.getResponse(seq)) == null){
        }
        System.out.println(r.response);

        socketMQManager.removeBySeq(seq);
        return Arrays.asList(r.response.replaceAll(",", " ").trim().split(" ").clone());
    }

    @Override
    public JavaProject getProjectEntity(String id, String pName, String oName) {

            JavaProject javaProject = new JavaProject();
            javaProject.setProjectName(pName);
            javaProject.setFilesName(getJavaProjectFiles(pName, id, oName));
            return javaProject;

    }

    private List<String> getJavaProjectFiles(String pName, String id, String oName){
        int seq = socketMQManager.createNewRequest(383, id);
        String msg = "<Type>383</><uid>" + id + "</><seq>" + seq + "</><pName>"+
                pName +
                "</>";
        if(oName == null){
            msg += "<oName></>";
        }else{
            msg += "<oName>" + oName + "</>";
        }
        socketClient.sendMessage(msg);
        SocketRequest r = null;
        while((r = socketMQManager.getResponse(seq)) == null){
        }
        System.out.println(r.response);

        if("-1".equals(r.response))
            return null;
        List<String> result = (List<String>) socketMQManager.get(seq, "fileList");
        socketMQManager.removeBySeq(seq);
        return result;
    }

    @Autowired
    UserServiceImpl userService;
    public List<String> getAccessibleOrgProjects(String uid){
        List<String> orgBelonged = userService.getUserOrgNames(uid);
        int seq = socketMQManager.createNewRequest(385, uid);
        socketClient.sendMessage("<Type>385</><uid>" + uid + "</><seq>" + seq + "</><uid>"+
                uid +
                "</>");
        SocketRequest r = null;
        while((r = socketMQManager.getResponse(seq)) == null){
        }
        System.out.println(r.response);
        socketMQManager.removeBySeq(seq);
        return Arrays.asList(r.response.replace("|", " ").trim().split(" ").clone());
    }

}
