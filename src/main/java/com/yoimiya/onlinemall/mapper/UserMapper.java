package com.yoimiya.onlinemall.mapper;

import com.yoimiya.onlinemall.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户 Mapper 接口
 */
@Mapper
public interface UserMapper {

    /**
     * 查询所有用户
     */
    @Select("SELECT * FROM user")
    List<User> findAll();

    /**
     * 插入用户
     */
    @Insert("INSERT INTO user (username, password, email, balance, created_at, updated_at) VALUES (#{username}, #{password}, #{email}, #{balance}, NOW(), NOW())")
    int insert(User user);
}
