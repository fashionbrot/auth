package com.github.fashionbrot.config;


import com.github.fashionbrot.interceptor.ExampleInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Resource
    private Environment environment;


    @Bean
    public ExampleInterceptor getExampleInterceptor(){
        return new ExampleInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(getExampleInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login")
        ;

    }


}
