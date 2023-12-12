package com.example.microservicetwo.intercepter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StarConfig implements WebMvcConfigurer {
    @Bean
    public ServiceTwoIntercepter serviceTwoIntercepter() {
        return new ServiceTwoIntercepter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(serviceTwoIntercepter());

    }

}

