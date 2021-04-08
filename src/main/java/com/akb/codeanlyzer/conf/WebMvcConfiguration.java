package com.akb.codeanlyzer.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/db/**").addResourceLocations("file:D:/storage/");
        registry.addResourceHandler("/ast/**").addResourceLocations("file:D:/storage/treegraph/");
        registry.addResourceHandler("/himg/**").addResourceLocations("file:D:/storage/hgraph/");
        super.addResourceHandlers(registry);
    }
}
