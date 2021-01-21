package com.akb.codeanlyzer;

import com.akb.codeanlyzer.service.upload.WebSocketUploadServer;

import javax.websocket.Session;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileUploadState {
    private String token;
    private int totalFileCount;
    private int currentFinishedFileCount = 0;
    private ConcurrentHashMap<WebSocketUploadServer, String> sessionFileNameMap = new ConcurrentHashMap<>();
    private String userName;
    private boolean upLoadBegin = false;
    private List<String> fileNames;
    private List<String> finishedFileNames = new ArrayList<>();
    private boolean unavailable = false;
    private String projName = "";
    private String orgName = "";



    public void dispose(){
        finishedFileNames.clear();
        sessionFileNameMap.clear();
    }
    public void removeTask(WebSocketUploadServer server){
        sessionFileNameMap.remove(server);
    }
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getProjName() {
        return projName;
    }

    public void setUnavailable(boolean b){ this.unavailable = b;}
    public String getTransferFileName(WebSocketUploadServer w){
        return sessionFileNameMap.get(w);
    }
    public void sessionJoinIn(WebSocketUploadServer server, String fileName){
        sessionFileNameMap.put(server, fileName);
    }
    public void setUpLoadBegin(boolean upLoadBegin) {
        this.upLoadBegin = upLoadBegin;
    }

    public FileUploadState(String token, int fileCount, String uName, List<String> fileNames){
        this.totalFileCount = fileCount;
        this.token = token;
        this.userName = uName;
        this.fileNames = fileNames;
    }

    public int getTotalFileCount() {
        return totalFileCount;
    }

    public int getCurrentFinishedFileCount() {
        return currentFinishedFileCount;
    }

    public synchronized int finishOneFile(String fName){
        finishedFileNames.add(fName);
        ++ currentFinishedFileCount;
        return totalFileCount - currentFinishedFileCount;
    }

    public void rollBack(){
        synchronized (this){  //回滚过程需要同步。拒绝其他线程访问此对象
            //TODO
            for(String str : finishedFileNames){
                File f = new File("D:\\storage\\upload\\tmp\\" + userName + "\\" + str);
                if(f.exists()){
                    f.delete();
                }
            }
        }
        setUnavailable(false); //设置本对象无效
    }


}
