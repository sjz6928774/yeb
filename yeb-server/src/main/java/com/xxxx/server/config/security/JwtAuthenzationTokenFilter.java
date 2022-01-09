package com.xxxx.server.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import springfox.documentation.spi.service.contexts.SecurityContext;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT登录授权过滤器
 */
public class JwtAuthenzationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 前端和后端的头要对上，要验证的头
        // 通过token key拿到token value
        String authHeader = request.getHeader(tokenHeader);
        // 存在token 正常系
        // token value 是否以tokenHead开头
        if (null != authHeader && authHeader.startsWith(tokenHead)) {
            String authToken = authHeader.substring(tokenHead.length());
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            // 能拿到用户名，从spring security拿不到用户对象。即用户没登录
            // token存在用户名，但未登录
            if (null != username && null == SecurityContextHolder.getContext().getAuthentication()) {
                // 让他登录 SecurityConfig中重写了userDetailsService
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // 验证token是否有效，重新设置用户对象
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    // 重新设置用户对象
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null,
                                    userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        // 放行
        filterChain.doFilter(request, response);
    }
}
