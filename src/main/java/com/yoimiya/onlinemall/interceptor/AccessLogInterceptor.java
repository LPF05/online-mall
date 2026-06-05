package com.yoimiya.onlinemall.interceptor;

import com.yoimiya.onlinemall.entity.AccessLog;
import com.yoimiya.onlinemall.mapper.AccessLogMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 访问日志拦截器
 */
@Component
public class AccessLogInterceptor implements HandlerInterceptor {

    @Autowired
    private AccessLogMapper accessLogMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            AccessLog log = new AccessLog();

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof Long) {
                log.setUserId((Long) auth.getPrincipal());
            }

            log.setPageUrl(request.getRequestURI());
            log.setIp(getClientIP(request));
            log.setUserAgent(request.getHeader("User-Agent"));
            log.setSessionId(request.getSession(false) != null ? request.getSession().getId() : null);

            accessLogMapper.insert(log);
        } catch (Exception e) {
            // 日志记录失败不影响主流程
        }
        return true;
    }

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
