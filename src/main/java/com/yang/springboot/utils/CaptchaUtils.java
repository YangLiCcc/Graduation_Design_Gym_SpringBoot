package com.yang.springboot.utils;

import cn.hutool.core.util.StrUtil;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CaptchaUtils {
    @Autowired
    private Producer captchaProducer;

    // 生成验证码
    public void setCaptchaCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        //生成验证码
        String capText=captchaProducer.createText();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY,capText);
        //向客户端写出
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    // 将从前端获取的参数转为String类型
    public static String getCaptchaCode(HttpServletRequest request, String key) {
        try {
            String result = request.getParameter(key);
            if (StrUtil.isNotEmpty(result)) {
                result = result.trim();
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 校验验证码
    public static boolean checkCaptchaCode(HttpServletRequest request) {
        // 获取生成的验证码
        String verifyCodeExpected = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        System.out.println("生成的验证码为: "+verifyCodeExpected);
        // 获取用户输入的验证码
        String verifyCodeActual = CaptchaUtils.getCaptchaCode(request, "verifyCodeActual");
        System.out.println("用户输入的验证码: " + verifyCodeActual);
        return verifyCodeActual != null && verifyCodeActual.equals(verifyCodeExpected);
    }

}
