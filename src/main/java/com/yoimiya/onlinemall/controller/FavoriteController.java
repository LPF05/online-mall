package com.yoimiya.onlinemall.controller;

import com.yoimiya.onlinemall.common.Result;
import com.yoimiya.onlinemall.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 收藏控制器
 */
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping
    public Result<List<Map<String, Object>>> getFavorites(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(favoriteService.getFavorites(userId));
    }

    @PostMapping
    public Result<Void> addFavorite(@RequestBody java.util.Map<String, Object> body, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        Long productId = Long.valueOf(body.get("productId").toString());
        favoriteService.addFavorite(userId, productId);
        return Result.success();
    }

    @DeleteMapping("/{productId}")
    public Result<Void> removeFavorite(@PathVariable Long productId, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        favoriteService.removeFavorite(userId, productId);
        return Result.success();
    }
}
