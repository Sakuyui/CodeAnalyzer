package com.akb.codeanlyzer.service;

import com.akb.codeanlyzer.mapper.UserMapper;
import com.akb.codeanlyzer.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public interface IUserService {
    public User tryLogin(String uid, String pwd);

}

