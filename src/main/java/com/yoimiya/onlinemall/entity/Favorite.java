package com.yoimiya.onlinemall.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 收藏实体类
 */
@Data
public class Favorite {
    private Long id;
    private Long userId;
    private Long productId;
    private LocalDateTime createdAt;
}
