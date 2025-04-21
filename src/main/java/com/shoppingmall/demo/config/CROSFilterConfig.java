package com.shoppingmall.demo.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Component
public class CROSFilterConfig {
    @Bean
    public FilterRegistrationBean<CorsFilter> myCorsFilter() {

        //创建 Corsconfiguration 对象
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        //设置访问源地址
        config.addAllowedOriginPattern("*");
        //设置访问源请求头
        config.addAllowedHeader("*");
        //设置访问源请求方法
        config.addAllowedMethod("*");
        //创建 UrlBasedcorsConfigurationsource 对象
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(new CorsFilter(source));
        // 设置优先级，越小优先级越高
        registrationBean.setOrder(0);
        return registrationBean;

    }
}
