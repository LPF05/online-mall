package com.yoimiya.onlinemall.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private String items;
    private BigDecimal totalAmount;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
