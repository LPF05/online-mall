package com.yoimiya.onlinemall.mapper;

import com.yoimiya.onlinemall.entity.Review;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ReviewMapper {

    @Insert("INSERT INTO review (user_id, product_id, order_no, rating, content, created_at) VALUES (#{userId}, #{productId}, #{orderNo}, #{rating}, #{content}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Review review);

    @Select("SELECT r.*, u.username, p.name as productName FROM review r LEFT JOIN user u ON r.user_id = u.id LEFT JOIN product p ON r.product_id = p.id WHERE r.product_id = #{productId} ORDER BY r.created_at DESC")
    List<Review> findByProductId(Long productId);

    @Select("SELECT r.*, u.username, p.name as productName FROM review r LEFT JOIN user u ON r.user_id = u.id LEFT JOIN product p ON r.product_id = p.id WHERE r.user_id = #{userId} ORDER BY r.created_at DESC")
    List<Review> findByUserId(Long userId);

    @Select("SELECT COUNT(*) FROM review WHERE user_id = #{userId} AND order_no = #{orderNo} AND product_id = #{productId}")
    int existsByUserOrderProduct(@Param("userId") Long userId, @Param("orderNo") String orderNo, @Param("productId") Long productId);
}
