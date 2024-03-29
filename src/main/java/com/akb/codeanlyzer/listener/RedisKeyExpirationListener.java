package com.akb.codeanlyzer.listener;

import com.akb.codeanlyzer.FileUploadState;
import com.akb.codeanlyzer.UploadManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }
    /**
     * 针对redis数据失效事件，进行数据处理
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
        String expiredKey = message.toString();
        System.out.println("Key Expire:" + expiredKey);
        if(expiredKey.endsWith("_upload")){
            //TODO
            String token = expiredKey.replaceAll("_upload","");
            FileUploadState fileUploadState =  UploadManager.getInstance().getFileUploadState(token);
            fileUploadState.setUnavailable(true);
            fileUploadState.rollBack();
        }if(expiredKey.startsWith("f_uuid_")){
            //TODO
            String f_uuid = expiredKey.substring(7);
            File f = new File("D:\\storage\\upload\\tmp\\" + f_uuid +".java");
            if(f.exists()){
                f.delete();
            }
        }
    }
}
