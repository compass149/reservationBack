package org.pgm.reservationback.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // API 엔드포인트에 대해 CORS 허용
                .allowedOrigins("http://localhost:3000") // 허용된 프론트엔드 도메인
                .allowedMethods("GET", "POST", "OPTIONS") // 허용된 HTTP 메서드
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true); // 인증 정보 포함 허용
    }
}
