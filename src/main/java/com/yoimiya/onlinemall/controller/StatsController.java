package com.yoimiya.onlinemall.controller;

import com.yoimiya.onlinemall.common.Result;
import com.yoimiya.onlinemall.mapper.AccessLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 统计控制器
 */
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private AccessLogMapper accessLogMapper;

    @GetMapping("/pv-uv")
    public Result<Map<String, Object>> getPVUV(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        long pv = accessLogMapper.countPV(start, end);
        long uv = accessLogMapper.countUV(start, end);

        Map<String, Object> result = new HashMap<>();
        result.put("pv", pv);
        result.put("uv", uv);
        result.put("start", start);
        result.put("end", end);

        return Result.success(result);
    }

    @GetMapping("/access")
    public Result<Map<String, Object>> getAccessStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        if (start == null) start = LocalDateTime.now().minusDays(7);
        if (end == null) end = LocalDateTime.now();

        long pv = accessLogMapper.countPV(start, end);
        long uv = accessLogMapper.countUV(start, end);

        Map<String, Object> result = new HashMap<>();
        result.put("pv", pv);
        result.put("uv", uv);
        result.put("start", start);
        result.put("end", end);

        return Result.success(result);
    }
}
