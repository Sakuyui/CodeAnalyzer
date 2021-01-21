package com.akb.codeanlyzer.service.upload;

import com.akb.codeanlyzer.FileUploadState;
import com.akb.codeanlyzer.UploadManager;
import com.akb.codeanlyzer.util.FileSysHelper;
import com.akb.codeanlyzer.util.TokenHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/upload/{uid}/{orgName}/socket")
@Component
public class WebSocketUploadServer {

    private static int onlineCount = 0;
    private static CopyOnWriteArraySet<WebSocketUploadServer> webSocketSet = new CopyOnWriteArraySet<>();
    private Session session;
    private static SaveFileI saveFileI;
    private HashMap docUri;

    private static IUploadSessionServ uploadSessionServ;


    @Autowired
    public void setUploadSessionServ(IUploadSessionServ uploadSessionServ){
        WebSocketUploadServer.uploadSessionServ = uploadSessionServ;
    }

    @Autowired
    public void setSaveFileI(SaveFileI saveFileI){
        WebSocketUploadServer.saveFileI = saveFileI;
    }
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid) {
        this.session = session;
        //加入set中
        webSocketSet.add(this);
        //在线人数加1
        addOnlineCount();
        System.out.println(uid+  "连接成功" + "----当前在线人数为：" + webSocketSet.size());
    }

    @OnClose
    public void onClose(@PathParam("uid") String uid, Session session) {
        //在线人数减1
        subOnlineCount();
        WebSocketUploadServer s = this;
        //从set中删除
        webSocketSet.remove(this);
        try{
            session.close();
        }catch (Exception e){}
        System.out.println(uid + "已关闭连接" + "----剩余链接数为：" + webSocketSet.size());
    }


    public void exceptionClose(int code, Session session){
        try{
            if(session != null)
                session.close();
            webSocketSet.remove(this);
            uploadSessionServ.removeUToken(this);
            FileUploadState fileUploadState = UploadManager.getInstance().getFileUploadState(
                    UploadManager.getInstance().serverUpLoadTokenMap.get(this)
            );
            fileUploadState.setUnavailable(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static List<String> jsonArrayToStringList(String json){
         String s1 = json.replaceAll("]","").replaceAll("\\[","");
         String[] ss1 = s1.replaceAll("\"","").split(",");
         return Arrays.asList(ss1.clone());
    }
    @OnMessage
    public void onMessage(String message, @PathParam("uid") String uid, @PathParam("orgName") String orgName) throws IOException{
        //前端传过来的消息都是一个json
        JSONObject jsonObject = JSON.parseObject(message);
        //消息类型
        String type = jsonObject.getString("type");
        //消息内容
        String data = jsonObject.getString("data");
        String curIndex = jsonObject.getString("curIndex");
        String totalCount = jsonObject.getString("count");
        //判断类型是否为文件名
        if ("fileName".equals(type)) {
            System.out.println("传输的文件为: " + data + " 第" + curIndex + "/" + totalCount);
            try {
                System.out.println("惹，收到文件信息惹, org = " + orgName);

                String uToken = jsonObject.getString("uToken");
                String pName = UploadManager.getInstance().getFileUploadState(uToken).getProjName();
                Map<String, Object> map = saveFileI.docPath(data, uid, pName);
                docUri = (HashMap) map;
                if(!uploadSessionServ.reSetSessionTimeOut(uToken)){
                    return;
                }
                FileUploadState fileUploadState = UploadManager.getInstance().getFileUploadState(uToken);
                if(fileUploadState == null){
                    this.sendMessage("error, Token not found");
                    return;
                }
                fileUploadState.sessionJoinIn(this, data);
                UploadManager.getInstance().serverUpLoadTokenMap.put(this, uToken);
                this.sendMessage("ok");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if ("setup".equals(type)){
            String upLoadId =  UUID.randomUUID().toString().replaceAll("-", "");
            String filesName = jsonObject.getString("files");
            System.out.println("files : " + filesName);

            List<String> fileNames = jsonArrayToStringList(filesName);

            int fileCount = jsonObject.getInteger("count");
            UploadManager.getInstance().putNewUploadToken(upLoadId, fileCount, uid, fileNames);
            this.sendMessage("OK," + upLoadId);
        }
        else if ("fileCount".equals(type)){
            System.out.println("传输第" + data + "份");
        }
        else if("transferBegin".equals(type)){
            String projName = jsonObject.getString("projName");
            //TODO 判断重名工程
            String uToken = jsonObject.getString("uToken");
            if(!UploadManager.getInstance().isContainsUploadToken(uToken)) { this.sendMessage("error,token error"); return;}
            UploadManager.getInstance().getFileUploadState(uToken).setProjName(projName);
            UploadManager.getInstance().getFileUploadState(uToken).setOrgName(orgName);
            //插入cache
            uploadSessionServ.putNewToken(uToken);
            sendMessage("ok,right token");
        }
        else if("fileTransferFinish".equals(type)){
            if(!uploadSessionServ.reSetSessionTimeOut(this)){

                return;
            }
            String uToken = jsonObject.getString("uToken");
            if(UploadManager.getInstance().isContainsUploadToken(uToken)){
                int c = UploadManager.getInstance().finishOneFile(uToken, this);
                System.out.println("正常结束 - " + uToken + " 剩余文件数:" + c);
                UploadManager.getInstance().getFileUploadState(uToken).removeTask(this);
                if(UploadManager.getInstance().getFileCountStillNotFinish(uToken) <= 0){
                    System.out.println("所有文件已完成");
                    //TODO: 转移文件
                    String oName = UploadManager.getInstance().getFileUploadState(uToken).getOrgName();
                    System.out.println("转移工程: " + UploadManager.getInstance().getFileUploadState(uToken).getOrgName());
                    String projName =  UploadManager.getInstance().getFileUploadState(uToken).getProjName();

                    if("individual".equals(oName)){
                        String targetPathBase = "D:\\storage\\upload\\user\\" + uid + "\\";
                        try{
                            FileSysHelper.copyFolder("D:\\storage\\upload\\tmp\\" + uid + "\\" + projName ,targetPathBase);
                            FileSysHelper.delAllFile(
                                    new File("D:\\storage\\upload\\tmp\\" + uid + "\\" + projName));
                        }catch (Exception e){e.printStackTrace(); }

                    }

                    UploadManager.getInstance().getFileUploadState(uToken).dispose();
                }
            }else {
                throw new RuntimeException("不存在的上传标记");
            }
        }
    }


    @OnMessage
    public void onMessage(byte[] message, Session session) {

        String token = UploadManager.getInstance().serverUpLoadTokenMap.get(this);
        String file = UploadManager.getInstance().upLoadTokenMap.get(token).getTransferFileName(this);
        if(!uploadSessionServ.reSetSessionTimeOut(this) || token == null || file == null){
            exceptionClose(-1, session);
            return;
        }
        System.out.println("data arrived: " + file + " " + token);
        try {
            //将流写入文件
            saveFileI.saveFileFromBytes(message, docUri);
            //文件写入成功，返回一个ok
            this.sendMessage("ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
    /**
     * 原子性的++操作
     */
    public static synchronized void addOnlineCount() {
        WebSocketUploadServer.onlineCount++;
    }

    /**
     * 原子性的--操作
     */
    public static synchronized void subOnlineCount() {
        WebSocketUploadServer.onlineCount--;
    }
}
