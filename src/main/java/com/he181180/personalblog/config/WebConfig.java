package com.he181180.personalblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded images from project's static/img directory
        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/", "file:" + System.getProperty("user.dir") + "/uploads/img/");

        // Also serve other static resources
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");
    }

}
