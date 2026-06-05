package com.yoimiya.onlinemall.mapper;

import com.yoimiya.onlinemall.entity.Product;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 商品 Mapper 接口
 */
@Mapper
public interface ProductMapper {

    @Select("SELECT * FROM product WHERE id = #{id}")
    Product findById(Long id);

    @Select("<script>" +
            "SELECT * FROM product" +
            "<where>" +
            "  <if test='category != null and category != \"\"'> AND category = #{category}</if>" +
            "  <if test='keyword != null and keyword != \"\"'> AND (name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))</if>" +
            "  <if test='minPrice != null'> AND price &gt;= #{minPrice}</if>" +
            "  <if test='maxPrice != null'> AND price &lt;= #{maxPrice}</if>" +
            "</where>" +
            " ORDER BY ${sort}" +
            "</script>")
    List<Product> findList(@Param("category") String category, @Param("sort") String sort,
                           @Param("keyword") String keyword,
                           @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    @Select("SELECT * FROM product")
    List<Product> findAll();

    @Select("SELECT * FROM product WHERE name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%')")
    List<Product> searchByKeyword(String keyword);

    @Update("UPDATE product SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(Long id);

    @Update("UPDATE product SET stock = stock - #{quantity} WHERE id = #{id} AND stock >= #{quantity}")
    int decreaseStock(@Param("id") Long id, @Param("quantity") int quantity);

    @Update("UPDATE product SET sales_count = sales_count + #{quantity} WHERE id = #{id}")
    int increaseSalesCount(@Param("id") Long id, @Param("quantity") int quantity);

    @Insert("INSERT INTO product (name, description, price, stock, category, image_url, created_at, updated_at) " +
            "VALUES (#{name}, #{description}, #{price}, #{stock}, #{category}, #{imageUrl}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Product product);

    @Update("UPDATE product SET name = #{name}, description = #{description}, price = #{price}, stock = #{stock}, " +
            "category = #{category}, image_url = #{imageUrl}, updated_at = NOW() WHERE id = #{id}")
    int update(Product product);

    @Select("SELECT DISTINCT category FROM product WHERE category IS NOT NULL")
    List<String> findCategories();

    @Select("SELECT * FROM product WHERE is_on_sale = 1 AND sale_start <= NOW() AND sale_end >= NOW() ORDER BY sales_count DESC LIMIT #{limit}")
    List<Product> findOnSale(@Param("limit") int limit);

    @Select("SELECT * FROM product ORDER BY created_at DESC LIMIT #{limit}")
    List<Product> findLatest(@Param("limit") int limit);

    @Delete("DELETE FROM product WHERE id = #{id}")
    int deleteById(Long id);
}
