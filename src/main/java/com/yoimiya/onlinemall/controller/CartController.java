package com.yoimiya.onlinemall.controller;

import com.yoimiya.onlinemall.common.Result;
import com.yoimiya.onlinemall.entity.Cart;
import com.yoimiya.onlinemall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 购物车控制器
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public Result<Cart> getCart(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(cartService.getCart(userId));
    }

    @PostMapping("/add")
    public Result<Void> addItem(Authentication authentication, @RequestBody Map<String, Object> body) {
        Long userId = (Long) authentication.getPrincipal();
        Long productId = Long.valueOf(body.get("productId").toString());
        int quantity = Integer.parseInt(body.get("quantity").toString());
        cartService.addItem(userId, productId, quantity);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> updateQuantity(Authentication authentication, @RequestBody Map<String, Object> body) {
        Long userId = (Long) authentication.getPrincipal();
        Long productId = Long.valueOf(body.get("productId").toString());
        int quantity = Integer.parseInt(body.get("quantity").toString());
        cartService.updateQuantity(userId, productId, quantity);
        return Result.success();
    }

    @DeleteMapping("/remove")
    public Result<Void> removeItem(Authentication authentication, @RequestParam Long productId) {
        Long userId = (Long) authentication.getPrincipal();
        cartService.removeItem(userId, productId);
        return Result.success();
    }

    @DeleteMapping("/clear")
    public Result<Void> clearCart(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        cartService.clearCart(userId);
        return Result.success();
    }
}
