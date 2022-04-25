package com.yang.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.yang.springboot.common.Constants;
import com.yang.springboot.common.lang.Result;
import com.yang.springboot.utils.AliOosUtils;
import com.yang.springboot.utils.JwtUtils;
import com.yang.springboot.utils.RedisCache;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/upload")
public class UploadController {

    private final AliOosUtils aliOosUtils;

    private final RedisCache redisCache;

    @Autowired
    public UploadController(AliOosUtils aliOosUtils, RedisCache redisCache) {
        this.aliOosUtils = aliOosUtils;
        this.redisCache = redisCache;
    }

    @ApiOperation("阿里云OOS文件上传")
    @PostMapping("/{type}")
    public Result upload(@PathVariable String type, @RequestParam MultipartFile file, @RequestHeader String token) throws Exception {
        String uploadUrl = aliOosUtils.upload(type, file);
        if (StrUtil.isNotEmpty(uploadUrl)) {
            Claims claims = JwtUtils.parseJWT(token);
            String userId = claims.getSubject();
            String key = type + ": ";
            // 设置Redis过期时间为10min
            redisCache.setCacheObject(key + userId, uploadUrl, 10, TimeUnit.MINUTES);
            return new Result(Constants.CODE_200, "文件上传成功!", uploadUrl);
        }
        return new Result(Constants.CODE_500, "文件上传失败!", null);
    }

    @PostMapping("/test/{type}")
    public Result uploadTest(@PathVariable String type, @RequestParam MultipartFile file) {
        String uploadUrl = aliOosUtils.upload(type, file);
        if (StrUtil.isNotEmpty(uploadUrl)) {
            String key = "UploadTest" + type + ": ";
            // 设置Redis过期时间为10min
            redisCache.setCacheObject(key, uploadUrl, 10, TimeUnit.MINUTES);
            return new Result(Constants.CODE_200, "文件上传成功!", uploadUrl);
        }
        return new Result(Constants.CODE_500, "文件上传失败!", null);
    }

}
