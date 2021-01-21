package com.akb.codeanlyzer;

import com.akb.codeanlyzer.mapper.UserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.multipart.MultipartResolver;


@SpringBootApplication
@ServletComponentScan
@ComponentScan
@MapperScan("com.akb.codeanlyzer.mapper")
public class CodeAnlyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeAnlyzerApplication.class, args);
    }

}
