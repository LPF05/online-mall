package com.yoimiya.onlinemall.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Review {
    private Long id;
    private Long userId;
    private Long productId;
    private String orderNo;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    // Transient fields for display
    private String username;
    private String productName;
}
