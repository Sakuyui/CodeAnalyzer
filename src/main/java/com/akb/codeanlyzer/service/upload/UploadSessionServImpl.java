package com.akb.codeanlyzer.service.upload;

import com.akb.codeanlyzer.UploadManager;
import com.akb.codeanlyzer.util.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("uploadSessionServImpl")
public class UploadSessionServImpl implements IUploadSessionServ{
    @Autowired
    TokenHelper tokenHelper;
    @Override
    public boolean putNewToken(String uToken) {
        return tokenHelper.putNewUpLoadToken(uToken, TokenHelper.defaultUploadTokenExpireTime);
    }

    @Override
    public boolean reSetSessionTimeOut(WebSocketUploadServer server) {
        System.out.println(UploadManager.getInstance().serverUpLoadTokenMap.get(server));
        return tokenHelper.resetSessionTimeOut(UploadManager.getInstance().serverUpLoadTokenMap.get(server),
                TokenHelper.defaultUploadTokenExpireTime);
    }

    @Override
    public boolean removeUToken(WebSocketUploadServer server) {
        tokenHelper.deleteUToken(UploadManager.getInstance().serverUpLoadTokenMap.get(server));
        return true;
    }

    @Override
    public boolean reSetSessionTimeOut(String token) {
        return tokenHelper.resetSessionTimeOut(token, TokenHelper.defaultUploadTokenExpireTime);
    }
}
