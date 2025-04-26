package com.shoppingmall.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingmall.demo.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter jsonConverter) {
                ObjectMapper objectMapper = new ObjectMapper();
                jsonConverter.setObjectMapper(objectMapper);
                jsonConverter.setSupportedMediaTypes(List.of(
                        org.springframework.http.MediaType.APPLICATION_JSON,
                        org.springframework.http.MediaType.APPLICATION_JSON_UTF8
                ));
            }
        }
    }

    /**
     * 注册拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns(
                        "/upload", "/user/hello"
                ).order(2);
    }

}
