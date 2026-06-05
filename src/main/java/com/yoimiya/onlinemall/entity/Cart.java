package com.yoimiya.onlinemall.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 购物车实体类
 */
@Data
public class Cart {
    private Long id;
    private Long userId;
    private String items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
