package com.yoimiya.onlinemall.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 */
@Data
public class Product {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private Integer stock;

    private String category;

    private String imageUrl;

    private Integer isOnSale;

    private LocalDateTime saleStart;

    private LocalDateTime saleEnd;

    private Integer viewCount;

    private Integer salesCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
