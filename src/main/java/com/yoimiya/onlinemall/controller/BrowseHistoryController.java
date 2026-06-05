package com.yoimiya.onlinemall.controller;

import com.yoimiya.onlinemall.common.Result;
import com.yoimiya.onlinemall.entity.BrowseHistory;
import com.yoimiya.onlinemall.service.BrowseHistoryService;
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
 * 浏览记录控制器
 */
@RestController
@RequestMapping("/api/browse")
public class BrowseHistoryController {

    @Autowired
    private BrowseHistoryService browseHistoryService;

    @GetMapping
    public Result<List<Map<String, Object>>> getHistory(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(browseHistoryService.getHistory(userId));
    }

    @PostMapping
    public Result<Void> addHistory(@RequestBody java.util.Map<String, Object> body, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        Long productId = Long.valueOf(body.get("productId").toString());
        browseHistoryService.addHistory(userId, productId);
        return Result.success();
    }

    @DeleteMapping
    public Result<Void> clearHistory(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        browseHistoryService.clearHistory(userId);
        return Result.success();
    }
}
