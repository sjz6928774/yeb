package com.xxxx.server.config.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken工具类
 */
@Component
public class JwtTokenUtil {

    // 用户名的KEY
    private static final String CLAIM_KEY_USERNAME = "sub";
    // 创建时间的KEY
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 根据用户信息生成token
     *
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        // 准备一个荷载
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 从token中获取登录用户名
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token){
        String username;
        try {
            // 根据token拿荷载
            Claims claims = getClaimsFromToken(token);
            // 拿到荷载
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 判断token是否有效
     * 1. token是否过期
     * 2.token的username与userDetails的username是否一致
     * @param token
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails){
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否可以被刷新
     * @return
     */
    public boolean canRefresh(String token){
        // 失效了刷新，没失效不刷新
        return isTokenExpired(token);
    }

    /**
     * 刷新token
     * @param token
     * @return
     */
    public String refreshToken(String token){
        // 改创建时间即可
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        // 生成新token
        return generateToken(claims);
    }

    /**
     * 判断token是否失效
     * true：失效
     * false：有效
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        // 时间在当前时间的前面，说明失效
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取失效时间
     * @return
     */
    private Date getExpiredDateFromToken(String token) {
        // 从token中获取荷载
        Claims claims = getClaimsFromToken(token);
        // 从荷载中获取失效时间
        return claims.getExpiration();
    }

    /**
     * 从token中获取荷载
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    // 签名放进去
                    .setSigningKey(secret)
                    // 将token转claim（荷载）
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 根据荷载生成JWT TOKEN
     *
     * @param claims
     * @return
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                // 荷载
                .setClaims(claims)
                // 失效时间
                .setExpiration(generateExpirationDate())
                // 签名 密钥
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成token失效时间，失效时间转换 Long->Date
     *
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

}
