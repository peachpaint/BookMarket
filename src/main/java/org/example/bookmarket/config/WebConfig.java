package org.example.bookmarket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration//@Configuration : Spring에게 이 클래스가 설정 클래스임을 알려줌
public class WebConfig implements WebMvcConfigurer {
//implements WebMvcConfigurer : Spring MVC 설정을 커스터마이징할 수 있게 해주는 인터페이스
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/images/**")
        .addResourceLocations("file:C:/dev/BookMarket/uploads/");
  }
}
