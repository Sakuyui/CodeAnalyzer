package com.akb.codeanlyzer;

import com.akb.codeanlyzer.service.netty.NettyServer;
import network.SocketClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ServletComponentScan
@ComponentScan
@MapperScan("com.akb.codeanlyzer.mapper")
public class CodeAnlyzerApplication {

    public static void main(String[] args) {


        SpringApplication.run(CodeAnlyzerApplication.class, args);

    }

}
