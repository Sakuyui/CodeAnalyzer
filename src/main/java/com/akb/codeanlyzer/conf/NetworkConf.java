package com.akb.codeanlyzer.conf;

import com.akb.codeanlyzer.network.SocketMQManager;
import com.akb.codeanlyzer.network.SocketRequest;
import network.SocketClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NetworkConf {
    @Bean
    public SocketClient socketClient(){
        SocketClient sc = new SocketClient("127.0.0.1", 2448);
        sc.sendMessage("<Type>101</><value>javaEE</>");
        return sc;
    }
    @Bean
    public SocketMQManager socketMQManager(){
        return new SocketMQManager();
    }
}
