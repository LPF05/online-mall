package com.yoimiya.onlinemall.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoimiya.onlinemall.entity.Cart;
import com.yoimiya.onlinemall.entity.Product;
import com.yoimiya.onlinemall.exception.BusinessException;
import com.yoimiya.onlinemall.mapper.CartMapper;
import com.yoimiya.onlinemall.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired(required = false)
    private ObjectMapper objectMapper;

    private ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    public Cart getCart(Long userId) {
        Cart cart = cartMapper.findByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setItems("[]");
        }
        return cart;
    }

    public void addItem(Long userId, Long productId, int quantity) {
        Product product = productMapper.findById(productId);
        if (product == null) throw new BusinessException("商品不存在");
        if (product.getStock() < quantity) throw new BusinessException("库存不足");

        Cart cart = cartMapper.findByUserId(userId);
        List<Map<String, Object>> items;
        try {
            if (cart == null) {
                items = new ArrayList<>();
            } else {
                items = getObjectMapper().readValue(cart.getItems(), new TypeReference<>() {});
            }
            Optional<Map<String, Object>> existing = items.stream()
                    .filter(i -> {
                        Object id = i.get("productId");
                        if (id instanceof Number) {
                            return ((Number) id).longValue() == productId;
                        }
                        return Objects.equals(id, productId);
                    })
                    .findFirst();
            if (existing.isPresent()) {
                int currentQty = (int) existing.get().get("quantity");
                if (product.getStock() < currentQty + quantity) {
                    throw new BusinessException("库存不足");
                }
                existing.get().put("quantity", currentQty + quantity);
            } else {
                Map<String, Object> item = new HashMap<>();
                item.put("productId", productId);
                item.put("productName", product.getName());
                item.put("price", product.getPrice());
                item.put("quantity", quantity);
                item.put("imageUrl", product.getImageUrl());
                items.add(item);
            }
            String json = getObjectMapper().writeValueAsString(items);
            if (cart == null) {
                cart = new Cart();
                cart.setUserId(userId);
                cart.setItems(json);
                cartMapper.insert(cart);
            } else {
                cartMapper.updateItems(userId, json);
            }
        } catch (JsonProcessingException e) {
            throw new BusinessException("购物车数据处理失败");
        }
    }

    public void updateQuantity(Long userId, Long productId, int quantity) {
        if (quantity <= 0) throw new BusinessException("数量必须大于0");
        Cart cart = cartMapper.findByUserId(userId);
        if (cart == null) throw new BusinessException("购物车为空");
        try {
            List<Map<String, Object>> items = getObjectMapper().readValue(cart.getItems(), new TypeReference<>() {});
            boolean found = false;
            for (Map<String, Object> item : items) {
                Object id = item.get("productId");
                boolean match = false;
                if (id instanceof Number) {
                    match = ((Number) id).longValue() == productId;
                } else {
                    match = Objects.equals(id, productId);
                }
                if (match) {
                    Long pid = ((Number) item.get("productId")).longValue();
                    Product product = productMapper.findById(pid);
                    if (product != null && product.getStock() < quantity) {
                        throw new BusinessException("库存不足");
                    }
                    item.put("quantity", quantity);
                    found = true;
                    break;
                }
            }
            if (!found) throw new BusinessException("购物车中不存在该商品");
            cartMapper.updateItems(userId, getObjectMapper().writeValueAsString(items));
        } catch (JsonProcessingException e) {
            throw new BusinessException("购物车数据处理失败");
        }
    }

    public void removeItem(Long userId, Long productId) {
        Cart cart = cartMapper.findByUserId(userId);
        if (cart == null) throw new BusinessException("购物车为空");
        try {
            List<Map<String, Object>> items = getObjectMapper().readValue(cart.getItems(), new TypeReference<>() {});
            boolean removed = items.removeIf(i -> {
                Object id = i.get("productId");
                if (id instanceof Number) {
                    return ((Number) id).longValue() == productId;
                }
                return Objects.equals(id, productId);
            });
            if (!removed) throw new BusinessException("购物车中不存在该商品");
            cartMapper.updateItems(userId, getObjectMapper().writeValueAsString(items));
        } catch (JsonProcessingException e) {
            throw new BusinessException("购物车数据处理失败");
        }
    }

    public void clearCart(Long userId) {
        Cart cart = cartMapper.findByUserId(userId);
        if (cart != null) {
            cartMapper.updateItems(userId, "[]");
        }
    }
}