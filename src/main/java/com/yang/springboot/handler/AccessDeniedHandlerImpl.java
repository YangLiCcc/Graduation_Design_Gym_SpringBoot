package com.yang.springboot.handler;

import com.alibaba.fastjson.JSON;
import com.yang.springboot.common.lang.Result;
import com.yang.springboot.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 异常处理
        WebUtils.renderString(response, JSON.toJSONString(new Result(HttpStatus.FORBIDDEN.value(), "权限不足!", null)));
    }
}
