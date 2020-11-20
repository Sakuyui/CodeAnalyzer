package com.akb.codeanlyzer.service;

import com.akb.codeanlyzer.mapper.UserMapper;
import com.akb.codeanlyzer.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService{
    @Autowired
    UserMapper userMapper;

    @Override
    public User tryLogin(String uid, String pwd) {
        return userMapper.findUserById(uid);
    }


}
