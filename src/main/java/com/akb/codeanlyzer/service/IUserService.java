package com.akb.codeanlyzer.service;

import com.akb.codeanlyzer.mapper.UserMapper;
import com.akb.codeanlyzer.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public interface IUserService {
    User tryLogin(String uid, String pwd);
    int createNewUser(User u);
    String setUserLoginState(boolean isLogin, String username);
    boolean checkToken(String token, String userName);
    User tryLoginByToken(String token, String uid);
}

