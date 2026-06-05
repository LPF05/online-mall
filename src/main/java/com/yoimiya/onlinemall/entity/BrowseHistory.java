package com.yoimiya.onlinemall.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 浏览记录实体类
 */
@Data
public class BrowseHistory {
    private Long id;
    private Long userId;
    private Long productId;
    private LocalDateTime createdAt;
}
