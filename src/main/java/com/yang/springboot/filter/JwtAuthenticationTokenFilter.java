package com.yang.springboot.filter;

import cn.hutool.core.util.StrUtil;
import com.yang.springboot.param.LoginUser;
import com.yang.springboot.utils.JwtUtils;
import com.yang.springboot.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final RedisCache redisCache;

    @Autowired
    public JwtAuthenticationTokenFilter(RedisCache redisCache) {
        this.redisCache = redisCache;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader("token");
        if (StrUtil.isEmpty(token)) {
            // 如果为空 直接放行
            filterChain.doFilter(request, response);
            // 因为没有token 所以不进行下面的解析操作 直接返回
            return;
        }

        // 解析token
        String userId = null;
        try {
            Claims claims = JwtUtils.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 如果token解析成功 从redis中获取用户信息
        // 如果token解析失败 则返回错误
        String redisKey = "UserLogin: " + userId;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        // 存放用户信息到SecurityContextHolder
        // setAuthentication需要一个Authentication对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 放行到下一个过滤器
        filterChain.doFilter(request, response);
    }
}
