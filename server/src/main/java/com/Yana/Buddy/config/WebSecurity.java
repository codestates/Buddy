package com.Yana.Buddy.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebSecurity implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://bucket-yana-buddy.s3-website.ap-northeast-2.amazonaws.com",
                        "https://yana-buddy.com",
                        "https://accounts.google.com",
                        "https://www.googleapis.com",
                        "https://kauth.kakao.com"
                )
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .exposedHeaders("typ", "JWT");
    }

}
