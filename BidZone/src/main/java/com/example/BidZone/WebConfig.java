package com.example.BidZone;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Adjust this path based on your file system structure
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:C:/Users/Public/uploads/");
    }
}
