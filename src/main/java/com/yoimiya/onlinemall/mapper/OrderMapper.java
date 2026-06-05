package com.yoimiya.onlinemall.mapper;

import com.yoimiya.onlinemall.entity.Order;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 订单 Mapper 接口
 */
@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO `order` (order_no, user_id, items, total_amount, status, created_at, updated_at) " +
            "VALUES (#{orderNo}, #{userId}, #{items}, #{totalAmount}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    @Select("SELECT * FROM `order` WHERE id = #{id}")
    Order findById(Long id);

    @Select("SELECT * FROM `order` WHERE order_no = #{orderNo}")
    Order findByOrderNo(String orderNo);

    @Select("<script>" +
            "SELECT * FROM `order` WHERE user_id = #{userId}" +
            "<if test='status != null'> AND status = #{status}</if>" +
            " ORDER BY created_at DESC" +
            "</script>")
    List<Order> findByUserId(@Param("userId") Long userId, @Param("status") Integer status);

    @Select("SELECT * FROM `order` ORDER BY created_at DESC")
    List<Order> findAll();

    @Update("UPDATE `order` SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
