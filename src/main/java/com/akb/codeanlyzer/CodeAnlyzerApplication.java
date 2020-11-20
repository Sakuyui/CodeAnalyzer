package com.akb.codeanlyzer;

import com.akb.codeanlyzer.mapper.UserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.akb.codeanlyzer.mapper")
public class CodeAnlyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeAnlyzerApplication.class, args);
    }

}
