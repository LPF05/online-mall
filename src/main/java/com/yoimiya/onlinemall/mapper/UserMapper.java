package com.yoimiya.onlinemall.mapper;

import com.yoimiya.onlinemall.entity.User;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户 Mapper 接口
 */
@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user")
    List<User> findAll();

    @Insert("INSERT INTO user (username, password, email, phone, balance, role, created_at, updated_at) " +
            "VALUES (#{username}, #{password}, #{email}, #{phone}, #{balance}, #{role}, NOW(), NOW())")
    int insert(User user);

    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(String email);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);

    @Update("UPDATE user SET balance = #{balance}, updated_at = NOW() WHERE id = #{id}")
    int updateBalance(@Param("id") Long id, @Param("balance") BigDecimal balance);

    @Update("UPDATE user SET username = #{username}, phone = #{phone}, updated_at = NOW() WHERE id = #{id}")
    int updateUserInfo(@Param("id") Long id, @Param("username") String username, @Param("phone") String phone);

    @Update("UPDATE user SET password = #{password}, updated_at = NOW() WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Update("UPDATE user SET points = points + #{points} WHERE id = #{userId}")
    int addPoints(@Param("userId") Long userId, @Param("points") int points);
}
