package com.yang.springboot.controller;

import cn.hutool.core.lang.UUID;
import com.google.code.kaptcha.Producer;
import com.yang.springboot.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

@RestController
public class CaptchaController {

    private final Producer captchaProducer;

    private final RedisCache redisCache;

    @Autowired
    public CaptchaController(Producer captchaProducer, RedisCache redisCache) {
        this.captchaProducer = captchaProducer;
        this.redisCache = redisCache;
    }

    @GetMapping("/captcha")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 禁止server端缓存
        response.setDateHeader("Expires", 0);
        // 设置标准的 HTTP/1.1 no-cache headers
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // 设置IE扩展 HTTP/1.1 no-cache headers (use addHeader)
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // 设置标准 HTTP/1.0 不缓存图片
        response.setHeader("Pragma", "no-cache");
        // 返回一个 JPEG图片 默认是text/html(输出文档的类型)
        response.setContentType("image/jpeg");
        // 为图片创建文本
        String capText = captchaProducer.createText();

        // 给每个用户生成uuid 防止并发访问时出现问题
        String uuid = String.valueOf(UUID.randomUUID());
        // 设置Redis缓存的过期时间为60*10s 即10min
        redisCache.setCacheObject(uuid, capText, 60 * 10, TimeUnit.SECONDS);
        Cookie cookie = new Cookie("captchaCode", uuid);
        response.addCookie(cookie);

        // 创建带有文本的图片
        BufferedImage bufferedImage = captchaProducer.createImage(capText);
        ServletOutputStream outputStream = response.getOutputStream();
        // 图片数据输出
        ImageIO.write(bufferedImage, "jpg", outputStream);
        try {
            outputStream.flush();
        } finally {
            outputStream.close();
        }
        return null;
    }

}
