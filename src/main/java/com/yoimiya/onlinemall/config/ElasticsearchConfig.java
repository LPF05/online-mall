package com.yoimiya.onlinemall.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 手动配置 Elasticsearch 8.13 客户端。
 * 原因：Spring Boot 4.0.6 默认提供的 elasticsearch-java 9.x 与本地 ES 8.13
 * 服务器的 Content-Type 协议不兼容，因此直接使用原生 8.13.x 客户端。
 */
@Configuration
public class ElasticsearchConfig {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchConfig.class);

    @Value("${spring.elasticsearch.uris:http://localhost:9200}")
    private String uris;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        String[] urls = uris.split(",");
        HttpHost[] hosts = new HttpHost[urls.length];
        for (int i = 0; i < urls.length; i++) {
            String u = urls[i].trim().replaceAll("^https?://", "");
            String host = u.contains(":") ? u.split(":")[0] : u;
            int port = u.contains(":") ? Integer.parseInt(u.split(":")[1]) : 9200;
            hosts[i] = new HttpHost(host, port, urls[i].trim().startsWith("https") ? "https" : "http");
        }

        RestClient restClient = RestClient.builder(hosts).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient client = new ElasticsearchClient(transport);

        log.info("[ES] 客户端初始化完成，目标节点: {}", uris);
        return client;
    }
}
