package com.UCH.UAContentHub.bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/avatars/**")
                .addResourceLocations("file:src/main/resources/static/avatars/");
        registry.addResourceHandler("/postPhotos/**")
                .addResourceLocations("file:src/main/resources/static/postPhotos/");
    }
}