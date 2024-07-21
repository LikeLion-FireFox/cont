package com.firewolf.cont.global.config;

import com.firewolf.cont.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/js/**", "/css/**", "/images/**","/error","/favicon.ico",
                        "/loginPage/**","/mainPage","/mainPage/logout",
                        "/swagger-ui/**", "/swagger-resources/**","/v3/api-docs/**","/api-docs/**",
                        "/webjars/**");
    }
}