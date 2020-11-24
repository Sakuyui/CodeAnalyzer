package com.akb.codeanlyzer.service;

import com.akb.codeanlyzer.mapper.UserMapper;
import com.akb.codeanlyzer.pojo.User;
import com.akb.codeanlyzer.util.AESUtil;
import com.akb.codeanlyzer.util.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.Session;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements IUserService{
    @Autowired
    UserMapper userMapper;

    @Autowired
    TokenHelper tokenHelper;

    @Override
    public User tryLogin(String uid, String pwd) {
        return userMapper.findUserById(uid);
    }

    @Transactional
    @Override
    public int createNewUser(User u) {
        if(userMapper.isUserExist(u.getUserName()) >= 1) return -1;
        return userMapper.addUser(u);
    }

    @Override
    public String setUserLoginState(boolean isLogin, String username) {
        if(!isLogin){
            tokenHelper.signOut(username);
            return "";
        }
        Random random = new Random();
        long r =  random.nextInt(9999999);
        String p = username + r;
        AESUtil aesUtil = AESUtil.getInstance();
        try {
            String token = aesUtil.encrypt(p, "" + r);
            tokenHelper.setLogin(token, username, TokenHelper.expireTime);
            return token;
        }catch (Exception e){
            return p;
        }

    }

    @Override
    public boolean checkToken(String token, String userName) {
        if(token == null || userName == null) return false;
        return tokenHelper.isTokenAvailable(token, userName );
    }

    @Override
    public User tryLoginByToken(String token, String uid) {
        if(tokenHelper.isTokenAvailable(token, uid)){
            tokenHelper.setLogin(token, uid, TokenHelper.expireTime);
            return userMapper.findUserById(uid);
        }
        return null;
    }

    @Override
    public List<String> getUserOrgNames(String uid) {
        return userMapper.getUserOrgNames(uid);
    }
}
