package com.yoimiya.onlinemall.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoimiya.onlinemall.entity.Cart;
import com.yoimiya.onlinemall.entity.Order;
import com.yoimiya.onlinemall.entity.Product;
import com.yoimiya.onlinemall.entity.User;
import com.yoimiya.onlinemall.exception.BusinessException;
import com.yoimiya.onlinemall.mapper.OrderMapper;
import com.yoimiya.onlinemall.mapper.CartMapper;
import com.yoimiya.onlinemall.mapper.ProductMapper;
import com.yoimiya.onlinemall.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired(required = false)
    private ObjectMapper objectMapper;

    private ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    @Transactional
    public Order createOrder(Long userId) {
        Cart cart = cartMapper.findByUserId(userId);
        if (cart == null || cart.getItems() == null || cart.getItems().equals("[]")) {
            throw new BusinessException("购物车为空，无法创建订单");
        }

        List<Map<String, Object>> cartItems;
        try {
            cartItems = getObjectMapper().readValue(cart.getItems(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new BusinessException("购物车数据解析失败");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Map<String, Object> item : cartItems) {
            Long productId = ((Number) item.get("productId")).longValue();
            int quantity = (int) item.get("quantity");
            Product product = productMapper.findById(productId);
            if (product == null) throw new BusinessException("商品不存在：" + productId);
            if (product.getStock() < quantity) throw new BusinessException("商品库存不足：" + product.getName());

            int rows = productMapper.decreaseStock(productId, quantity);
            if (rows == 0) throw new BusinessException("商品库存不足（并发冲突）：" + product.getName());

            productMapper.increaseSalesCount(productId, quantity);

            item.put("price", product.getPrice());
            item.put("productName", product.getName());
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        String orderNo = System.currentTimeMillis() + "" + new Random().nextInt(10000);

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        try {
            order.setItems(getObjectMapper().writeValueAsString(cartItems));
        } catch (JsonProcessingException e) {
            throw new BusinessException("订单数据序列化失败");
        }
        order.setTotalAmount(totalAmount);
        order.setStatus(0);
        orderMapper.insert(order);

        cartMapper.updateItems(userId, "[]");

        return order;
    }

    @Transactional
    public void payOrder(String orderNo, Long userId) {
        Order order = orderMapper.findByOrderNo(orderNo);
        if (order == null) throw new BusinessException("订单不存在");
        if (!order.getUserId().equals(userId)) throw new BusinessException("无权操作此订单");
        if (order.getStatus() != 0) throw new BusinessException("订单状态不允许支付");

        User user = userMapper.findById(userId);
        if (user == null) throw new BusinessException("用户不存在");
        if (user.getBalance().compareTo(order.getTotalAmount()) < 0) {
            throw new BusinessException("余额不足，当前余额：" + user.getBalance());
        }

        userMapper.updateBalance(userId, user.getBalance().subtract(order.getTotalAmount()));
        orderMapper.updateStatus(order.getId(), 1);

        // Add points: 1 yuan = 1 point
        int points = order.getTotalAmount().intValue();
        userMapper.addPoints(userId, points);
    }

    public List<Order> getOrderList(Long userId, Integer status) {
        return orderMapper.findByUserId(userId, status);
    }

    public Order getOrderDetail(String orderNo) {
        Order order = orderMapper.findByOrderNo(orderNo);
        if (order == null) throw new BusinessException("订单不存在");
        return order;
    }

    public List<Order> getAllOrders() {
        return orderMapper.findAll();
    }

    @Transactional
    public void confirmReceipt(String orderNo, Long userId) {
        Order order = orderMapper.findByOrderNo(orderNo);
        if (order == null) throw new BusinessException("订单不存在");
        if (!order.getUserId().equals(userId)) throw new BusinessException("无权操作此订单");
        if (order.getStatus() != 2) throw new BusinessException("订单状态不允许确认收货");
        orderMapper.updateStatus(order.getId(), 3);
    }

    @Transactional
    public void cancelOrder(String orderNo, Long userId) {
        Order order = orderMapper.findByOrderNo(orderNo);
        if (order == null) throw new BusinessException("订单不存在");
        if (!order.getUserId().equals(userId)) throw new BusinessException("无权操作此订单");
        if (order.getStatus() != 0) throw new BusinessException("只能取消待支付订单");
        orderMapper.updateStatus(order.getId(), 4);
    }

    @Transactional
    public void shipOrder(String orderNo) {
        Order order = orderMapper.findByOrderNo(orderNo);
        if (order == null) throw new BusinessException("订单不存在");
        if (order.getStatus() != 1) throw new BusinessException("只能发货已付款订单");
        orderMapper.updateStatus(order.getId(), 2);
    }

    @Transactional
    public void updateOrderStatus(String orderNo, Integer newStatus) {
        Order order = orderMapper.findByOrderNo(orderNo);
        if (order == null) throw new BusinessException("订单不存在");
        if (newStatus < 0 || newStatus > 4) throw new BusinessException("无效的订单状态");
        orderMapper.updateStatus(order.getId(), newStatus);
    }
}