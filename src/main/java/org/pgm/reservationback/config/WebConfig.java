package org.pgm.reservationback.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // C:/Project/KDigital/reservationBack/uploads 가 실제 저장 경로라고 가정할 때
        registry.addResourceHandler("/uploads/**")
               // .addResourceLocations("file:uploads/");
                .addResourceLocations("file:///C:/upload/uploads/");
    }
}
