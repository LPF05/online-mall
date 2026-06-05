package com.yoimiya.onlinemall.controller;

import com.yoimiya.onlinemall.common.Result;
import com.yoimiya.onlinemall.entity.Order;
import com.yoimiya.onlinemall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Result<Order> createOrder(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(orderService.createOrder(userId));
    }

    @PostMapping("/{orderNo}/pay")
    public Result<Void> payOrder(Authentication authentication, @PathVariable String orderNo) {
        Long userId = (Long) authentication.getPrincipal();
        orderService.payOrder(orderNo, userId);
        return Result.success();
    }

    @GetMapping
    public Result<List<Order>> list(Authentication authentication,
                                    @RequestParam(required = false) Integer status) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(orderService.getOrderList(userId, status));
    }

    @GetMapping("/{orderNo}")
    public Result<Order> detail(@PathVariable String orderNo) {
        return Result.success(orderService.getOrderDetail(orderNo));
    }

    @PostMapping("/{orderNo}/confirm")
    public Result<Void> confirmReceipt(Authentication authentication, @PathVariable String orderNo) {
        Long userId = (Long) authentication.getPrincipal();
        orderService.confirmReceipt(orderNo, userId);
        return Result.success();
    }

    @PostMapping("/{orderNo}/cancel")
    public Result<Void> cancelOrder(Authentication authentication, @PathVariable String orderNo) {
        Long userId = (Long) authentication.getPrincipal();
        orderService.cancelOrder(orderNo, userId);
        return Result.success();
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<Order>> getAllOrders() {
        return Result.success(orderService.getAllOrders());
    }

    @PostMapping("/admin/{orderNo}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateOrderStatus(@PathVariable String orderNo, @RequestBody Map<String, Integer> body) {
        orderService.updateOrderStatus(orderNo, body.get("status"));
        return Result.success();
    }

    @PostMapping("/admin/{orderNo}/ship")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> shipOrder(@PathVariable String orderNo) {
        orderService.shipOrder(orderNo);
        return Result.success();
    }
}
