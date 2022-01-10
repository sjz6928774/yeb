package com.xxxx.server.config.security;

import com.xxxx.server.pojo.Admin;
import com.xxxx.server.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security配置类
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private RestAuthoricationEntryPoint restAuthoricationEntryPoint;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    /**
     * 配置重写的方法，走自定义的userDetailsService逻辑方法
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 密码匹配走BCryptPasswordEncoder
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }


    /**
     * 重写userDetailService以及根据用户名获取admin的方法
     *
     * @return
     */
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        return username -> {
            Admin admin = adminService.getAdminByUsername(username);
            if (null != admin) {
                return admin;
            }
            return null;
        };
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/login",
                "/logout",
                "/css/**",
                "/js/**",
                "/index.html",
                "favicon.ico",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/kaptcha"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 使用jwt，不需要csrf
        http.csrf()
                .disable()
                // 基于token，不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 允许登录访问 configure(WebSecurity web)做了
//                .antMatchers("/login", "/logout")
//                .permitAll()
                // 除了上面，所有请求都要认证
                .anyRequest()
                .authenticated()
                .and()
                .headers()
                // 禁用缓存
                .cacheControl();

        // 配置jwt登录授权拦截器
        http.addFilterBefore(jwtAuthenzationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        // 添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthoricationEntryPoint);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenzationTokenFilter jwtAuthenzationTokenFilter() {
        return new JwtAuthenzationTokenFilter();
    }
}
