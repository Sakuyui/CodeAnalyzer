package com.akb.codeanlyzer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


public class TokenHelper {
    public static int expireTime = 60 * 20; //20min




    @Autowired
    private RedisTemplate redisTemplate;
    public void setExpireTime(int expireTime) {
        TokenHelper.expireTime = expireTime;
    }

    public void setLogin(String token, String username, int expireT){
        if (redisTemplate.hasKey(username + "_token")){
            redisTemplate.delete(username + "_token");
        }
        RedisSerializer serializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setValueSerializer(serializer);

        redisTemplate.opsForValue().set(username + "_token", token, expireTime, TimeUnit.SECONDS);
    }

    public void signOut (String username){
        if(redisTemplate.hasKey(username + "_token"))
            redisTemplate.delete(username);
    }

    public boolean isTokenAvailable(String token, String userName){
        if(redisTemplate.hasKey(userName + "_token")){
            if(redisTemplate.opsForValue().get(userName + "_token").equals(token)){
                return true;
            }
        }
        return false;
    }


}
