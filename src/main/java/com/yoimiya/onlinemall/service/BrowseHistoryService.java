package com.yoimiya.onlinemall.service;

import com.yoimiya.onlinemall.entity.BrowseHistory;
import com.yoimiya.onlinemall.entity.Product;
import com.yoimiya.onlinemall.mapper.BrowseHistoryMapper;
import com.yoimiya.onlinemall.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 浏览记录服务类
 */
@Service
public class BrowseHistoryService {

    @Autowired
    private BrowseHistoryMapper browseHistoryMapper;

    @Autowired
    private ProductMapper productMapper;

    public void recordBrowse(Long userId, Long productId) {
        BrowseHistory history = new BrowseHistory();
        history.setUserId(userId);
        history.setProductId(productId);
        browseHistoryMapper.insert(history);
    }

    public void addHistory(Long userId, Long productId) {
        BrowseHistory history = new BrowseHistory();
        history.setUserId(userId);
        history.setProductId(productId);
        browseHistoryMapper.insert(history);
    }

    public List<Map<String, Object>> getHistory(Long userId) {
        List<BrowseHistory> histories = browseHistoryMapper.findByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (BrowseHistory history : histories) {
            Product product = productMapper.findById(history.getProductId());
            if (product != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", history.getId());
                item.put("userId", history.getUserId());
                item.put("productId", history.getProductId());
                item.put("productName", product.getName());
                item.put("price", product.getPrice());
                item.put("imageUrl", product.getImageUrl());
                item.put("createdAt", history.getCreatedAt());
                result.add(item);
            }
        }
        return result;
    }

    public void clearHistory(Long userId) {
        browseHistoryMapper.deleteByUserId(userId);
    }
}
