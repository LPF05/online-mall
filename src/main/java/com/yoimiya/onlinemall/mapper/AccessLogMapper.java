package com.yoimiya.onlinemall.mapper;

import com.yoimiya.onlinemall.entity.AccessLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDateTime;

/**
 * 访问日志 Mapper 接口
 */
@Mapper
public interface AccessLogMapper {

    @Insert("INSERT INTO access_log (user_id, page_url, ip, user_agent, session_id, created_at) " +
            "VALUES (#{userId}, #{pageUrl}, #{ip}, #{userAgent}, #{sessionId}, NOW())")
    int insert(AccessLog accessLog);

    @Select("SELECT COUNT(*) FROM access_log WHERE created_at >= #{start} AND created_at <= #{end}")
    long countPV(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Select("SELECT COUNT(DISTINCT session_id) FROM access_log WHERE created_at >= #{start} AND created_at <= #{end}")
    long countUV(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
