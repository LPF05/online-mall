package com.yoimiya.onlinemall.mapper;

import com.yoimiya.onlinemall.entity.Address;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface AddressMapper {

    @Select("SELECT * FROM address WHERE user_id = #{userId} ORDER BY is_default DESC, created_at DESC")
    List<Address> findByUserId(Long userId);

    @Select("SELECT * FROM address WHERE id = #{id} AND user_id = #{userId}")
    Address findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Insert("INSERT INTO address (user_id, receiver_name, phone, province, city, district, detail_address, is_default, created_at) VALUES (#{userId}, #{receiverName}, #{phone}, #{province}, #{city}, #{district}, #{detailAddress}, #{isDefault}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Address address);

    @Update("UPDATE address SET receiver_name = #{receiverName}, phone = #{phone}, province = #{province}, city = #{city}, district = #{district}, detail_address = #{detailAddress}, is_default = #{isDefault}, updated_at = NOW() WHERE id = #{id} AND user_id = #{userId}")
    int update(Address address);

    @Delete("DELETE FROM address WHERE id = #{id} AND user_id = #{userId}")
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Update("UPDATE address SET is_default = 0 WHERE user_id = #{userId} AND is_default = 1")
    int clearDefault(Long userId);

    @Select("SELECT * FROM address WHERE user_id = #{userId} AND is_default = 1 LIMIT 1")
    Address findDefaultByUserId(Long userId);
}
