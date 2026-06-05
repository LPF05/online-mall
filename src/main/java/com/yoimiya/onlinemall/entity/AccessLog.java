package com.yoimiya.onlinemall.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 访问日志实体类
 */
@Data
public class AccessLog {
    private Long id;
    private Long userId;
    private String pageUrl;
    private String ip;
    private String userAgent;
    private String sessionId;
    private LocalDateTime createdAt;
}
