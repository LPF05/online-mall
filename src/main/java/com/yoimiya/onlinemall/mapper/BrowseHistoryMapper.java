package com.yoimiya.onlinemall.mapper;

import com.yoimiya.onlinemall.entity.BrowseHistory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 浏览记录 Mapper 接口
 */
@Mapper
public interface BrowseHistoryMapper {

    @Insert("INSERT INTO browse_history (user_id, product_id, created_at) VALUES (#{userId}, #{productId}, NOW()) ON DUPLICATE KEY UPDATE created_at = NOW()")
    int insert(BrowseHistory browseHistory);

    @Select("SELECT * FROM browse_history WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT 50")
    List<BrowseHistory> findByUserId(Long userId);

    @Delete("DELETE FROM browse_history WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);

    @Select("SELECT COUNT(*) FROM browse_history WHERE user_id = #{userId} AND product_id = #{productId}")
    int existsByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}
