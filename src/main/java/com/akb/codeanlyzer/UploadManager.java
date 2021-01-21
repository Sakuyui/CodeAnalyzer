package com.akb.codeanlyzer;

import com.akb.codeanlyzer.service.upload.WebSocketUploadServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UploadManager {
    public Map<String, String> userNameUploadIDMap = new HashMap<>();

    private UploadManager(){}
    private static UploadManager instance;
    public ConcurrentHashMap<String, FileUploadState> upLoadTokenMap = new ConcurrentHashMap<>();
    public ConcurrentHashMap<WebSocketUploadServer, String> serverUpLoadTokenMap = new ConcurrentHashMap<>();

    public FileUploadState getFileUploadState(String token){
        return upLoadTokenMap.get(token);
    }


    public void putNewUploadToken(String token, int count, String userName, List<String> fileNames){
        upLoadTokenMap.put(token, new FileUploadState(token, count, userName, fileNames));
    }
    public int getFileCountStillNotFinish(String token){
        FileUploadState fileUploadState = upLoadTokenMap.get(token);
        if(fileUploadState == null) return -1;
        return fileUploadState.getTotalFileCount() - fileUploadState.getCurrentFinishedFileCount();
    }
    public int finishOneFile(String token, WebSocketUploadServer server){
        FileUploadState fileUploadState = upLoadTokenMap.get(token);
        if(fileUploadState == null) return -1;
        String fileName = fileUploadState.getTransferFileName(server);
        if(fileUploadState == null || fileUploadState.getCurrentFinishedFileCount() > fileUploadState.getTotalFileCount()) return -1;
        return fileUploadState.finishOneFile(fileName);
    }
    public boolean isContainsUploadToken(String token){
        return upLoadTokenMap.containsKey(token);
    }
    public static UploadManager getInstance(){
        if (instance == null){
            synchronized (UploadManager.class){
                if(instance == null){
                    instance = new UploadManager();
                }
            }
        }
        return instance;
    }



    public void rollBack(String uToken){
        FileUploadState fileUploadState = UploadManager.getInstance().getFileUploadState(uToken);
        
    }

    public void putNewUploadID(String userName, String upLoadID){
        userNameUploadIDMap.put(userName, upLoadID);
    }

}


