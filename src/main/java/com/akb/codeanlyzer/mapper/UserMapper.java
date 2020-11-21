package com.akb.codeanlyzer.mapper;

import com.akb.codeanlyzer.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component("userMapper")
public interface UserMapper{
    @Select("select * from user_inf where uid = #{uid}")
    @Results(value = {@Result(property = "userName",column = "uid",javaType = String.class),
            @Result(property = "pwd",column = "pwd",javaType = String.class),
            @Result(property = "uIcon", column = "icon"),
            @Result(property = "nickName", column = "nickname")
    })
    User findUserById(@Param("uid") String uid);

    @Insert({"insert into user_inf(uid, icon, nickname, random, pwd, hashpwd) " +
            "values(#{userName}, #{uIcon}, #{nickName}, #{random}, #{pwd}, #{hashPwd})"})
    int addUser(User user);

    @Select("select count(*) from user_inf where uid = #{uid}")
    int isUserExist(@Param("uid") String uid);


}
