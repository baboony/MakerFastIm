package com.maker;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.tio.websocket.starter.EnableTioWebSocketServer;

/**
 * @author lucky winner
 * 启动类
 */
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@EnableTioWebSocketServer
@EnableScheduling
@MapperScan("com.maker.mapper")
public class FastImStart {

    public static void main(String[] args) {
        SpringApplication.run(FastImStart.class);
    }
}
