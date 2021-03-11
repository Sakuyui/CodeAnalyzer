package com.akb.codeanlyzer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TokenHelper {
    public static int expireTime = 20 * 60; //20min
    public static int defaultUploadTokenExpireTime = 30;



    @Autowired
    private RedisTemplate redisTemplate;
    public void setExpireTime(int expireTime) {
        TokenHelper.expireTime = expireTime;
    }

    public void deleteUToken(String token){
        redisTemplate.delete(token + "_upload");
    }
    public void setLogin(String token, String username, int expireT){
        System.out.println("set login");
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
            System.out.println("username = " + userName);
            if(redisTemplate.opsForValue().get(userName + "_token").equals(token)){
                return true;
            }
        }
        return false;
    }

    public boolean putNewUpLoadToken(String token, int expireTime){
        RedisSerializer serializer = new StringRedisSerializer();
        if(redisTemplate.hasKey(token)) return false;
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.opsForValue().set(token + "_upload", token, expireTime, TimeUnit.SECONDS);
        return true;
    }


    public boolean resetSessionTimeOut(String token, int expireTime){
        if(!redisTemplate.hasKey(token + "_upload")) return false;
        redisTemplate.expire(token,expireTime,TimeUnit.SECONDS);
        return true;
    }


}
