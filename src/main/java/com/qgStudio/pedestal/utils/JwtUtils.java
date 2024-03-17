package com.qgStudio.pedestal.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2023/11/16
 */
@Slf4j
public class JwtUtils {


    private static final String SIGNKEY = "qweqe";

    private static final long EXPIRE = 604800000000L;

    /**
     * 永不过期
     * 生成JWT令牌
     *
     * @param claims JWT第二部分载荷payload中存储的内容
     * @return 生成的JWT令牌
     */
    public static String createJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, SIGNKEY)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .compact();
    }

    /**
     * 解析JWT令牌
     *
     * @param jwt JWT令牌
     * @return JWT第二部分载荷payload中存储的内容
     */
    public static Claims parseJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(SIGNKEY)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }


}
