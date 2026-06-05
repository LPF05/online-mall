package com.yoimiya.onlinemall.mapper;

import com.yoimiya.onlinemall.entity.Cart;
import org.apache.ibatis.annotations.*;

/**
 * 购物车 Mapper 接口
 */
@Mapper
public interface CartMapper {

    @Select("SELECT * FROM cart WHERE user_id = #{userId}")
    Cart findByUserId(Long userId);

    @Insert("INSERT INTO cart (user_id, items, created_at, updated_at) VALUES (#{userId}, #{items}, NOW(), NOW())")
    int insert(Cart cart);

    @Update("UPDATE cart SET items = #{items}, updated_at = NOW() WHERE user_id = #{userId}")
    int updateItems(@Param("userId") Long userId, @Param("items") String items);
}
