package com.yoimiya.onlinemall.document;

import com.yoimiya.onlinemall.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Elasticsearch 商品文档（纯 POJO，字段类型由 Jackson 推断，索引名固定为 "product"
 * - name / description: text（由默认 standard 分词）
 * - category: keyword（精确匹配）
 * - createdAt: 由 LocalDateTime 格式化为 ISO 8601 字符串后写入，
 *              避免 Jackson 默认 ObjectMapper 不支持 Java 8 日期时间类型的问题
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDocument {

    private Long id;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private String imageUrl;
    private Integer isOnSale;
    private Integer viewCount;
    private Integer salesCount;
    /** ISO 8601 格式：yyyy-MM-dd'T'HH:mm:ss */
    private String createdAt;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static ProductDocument fromEntity(Product p) {
        if (p == null) return null;
        return ProductDocument.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .category(p.getCategory())
                .price(p.getPrice())
                .originalPrice(p.getOriginalPrice())
                .stock(p.getStock())
                .imageUrl(p.getImageUrl())
                .isOnSale(p.getIsOnSale())
                .viewCount(p.getViewCount())
                .salesCount(p.getSalesCount())
                .createdAt(p.getCreatedAt() == null ? null : p.getCreatedAt().format(FORMATTER))
                .build();
    }

    /** 搜索结果回读时把 ISO 字符串还原为 LocalDateTime，供上层业务使用 */
    public LocalDateTime parseCreatedAt() {
        return createdAt == null ? null : LocalDateTime.parse(createdAt, FORMATTER);
    }
}
