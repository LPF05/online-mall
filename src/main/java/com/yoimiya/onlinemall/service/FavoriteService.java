package com.yoimiya.onlinemall.service;

import com.yoimiya.onlinemall.entity.Favorite;
import com.yoimiya.onlinemall.entity.Product;
import com.yoimiya.onlinemall.mapper.FavoriteMapper;
import com.yoimiya.onlinemall.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收藏服务类
 */
@Service
public class FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private ProductMapper productMapper;

    public void addFavorite(Long userId, Long productId) {
        if (!favoriteMapper.existsByUserIdAndProductId(userId, productId)) {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setProductId(productId);
            favoriteMapper.insert(favorite);
        }
    }

    public void removeFavorite(Long userId, Long productId) {
        favoriteMapper.deleteByUserIdAndProductId(userId, productId);
    }

    public List<Map<String, Object>> getFavorites(Long userId) {
        List<Favorite> favorites = favoriteMapper.findByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Favorite favorite : favorites) {
            Product product = productMapper.findById(favorite.getProductId());
            if (product != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", favorite.getId());
                item.put("userId", favorite.getUserId());
                item.put("productId", favorite.getProductId());
                item.put("productName", product.getName());
                item.put("price", product.getPrice());
                item.put("imageUrl", product.getImageUrl());
                item.put("createdAt", favorite.getCreatedAt());
                result.add(item);
            }
        }
        return result;
    }

    public boolean isFavorite(Long userId, Long productId) {
        return favoriteMapper.existsByUserIdAndProductId(userId, productId);
    }
}
