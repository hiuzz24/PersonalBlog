package com.he181180.personalblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded images from project's static/img directory
        String uploadPath = "file:" + System.getProperty("user.dir") + "/src/main/resources/static/img/";
        registry.addResourceHandler("/img/**")
                .addResourceLocations(uploadPath);

        // Also serve other static resources
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");
    }

}
