package com.yoimiya.onlinemall.mapper;

import com.yoimiya.onlinemall.entity.Favorite;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 收藏 Mapper 接口
 */
@Mapper
public interface FavoriteMapper {

    @Insert("INSERT INTO favorite (user_id, product_id, created_at) VALUES (#{userId}, #{productId}, NOW())")
    int insert(Favorite favorite);

    @Select("SELECT * FROM favorite WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Favorite> findByUserId(Long userId);

    @Delete("DELETE FROM favorite WHERE user_id = #{userId} AND product_id = #{productId}")
    int deleteByUserIdAndProductId(Long userId, Long productId);

    @Select("SELECT COUNT(*) > 0 FROM favorite WHERE user_id = #{userId} AND product_id = #{productId}")
    boolean existsByUserIdAndProductId(Long userId, Long productId);
}
