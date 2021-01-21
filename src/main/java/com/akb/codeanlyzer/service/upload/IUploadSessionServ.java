package com.akb.codeanlyzer.service.upload;

public interface IUploadSessionServ {
    boolean putNewToken(String uToken);
    boolean reSetSessionTimeOut(WebSocketUploadServer server);
    boolean reSetSessionTimeOut(String token);
    boolean removeUToken(WebSocketUploadServer server);
}
