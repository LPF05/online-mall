package com.yoimiya.onlinemall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 直接使用原生 elasticsearch-java 8.13 客户端，不使用 Spring Boot 自带的 ES 自动配置
 * 客户端由 com.yoimiya.onlinemall.config.ElasticsearchConfig 负责构建
 */
@SpringBootApplication
public class OnlineMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineMallApplication.class, args);
    }

}
