package com.yang.springboot.utils;

import cn.hutool.core.lang.UUID;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

/**
 * JWT工具类
 */
public class JwtUtils {

    private static final Long JWT_TTL = 60 * 60 * 1000L; // JWT有效期 60 * 60 * 1000 一个小时
    private static final String JWT_KEY = "YangLiCcc"; // JWT秘钥明文

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成jwt
     * @param subject token中存放的数据 JSON格式
     * @return
     */
    public static String createJWT(String subject) {
        JwtBuilder jwtBuilder = getJwtBuilder(subject, null, getUUID());
        return jwtBuilder.compact();
    }

    /**
     * 生成jwt
     * @param subject token中存放的数据 JSON格式
     * @param ttlMillis token超时时间
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder jwtBuilder = getJwtBuilder(subject, ttlMillis, getUUID());
        return jwtBuilder.compact();
    }

    /**
     * 生成jwt
     * @param id 唯一标识
     * @param subject token中存放的数据 JSON格式
     * @param ttlMillis token超时时间
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder jwtBuilder = getJwtBuilder(subject, ttlMillis, id);
        return jwtBuilder.compact();
    }

    public static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtils.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);

        return Jwts.builder()
                .setId(uuid) // 唯一标识id
                .setSubject(subject) // 主题 可以是JSON数据
                .setIssuer("YangLiCcc") // 签发者
                .setIssuedAt(now) // 签发时间
                .signWith(signatureAlgorithm, secretKey) // 使用HS256对称加密算法前面 第二个参数是秘钥
                .setExpiration(expDate); // 有效期
    }

    /**
     * 生成加密后的秘钥
     * @return secretKey
     */
    public static SecretKey generalKey() {
        byte[] encodeKey = Base64.getDecoder().decode(JwtUtils.JWT_KEY);
        return new SecretKeySpec(encodeKey, 0, encodeKey.length, "AES");
    }

    /**
     * 解析token
     * @param jwt token
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

}
