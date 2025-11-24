package org.example.bookmarket.config;

import org.example.bookmarket.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Bean
  protected PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }//PasswordEncoder : 사용자 비밀번호의 단방향 암호화를 지원하는 인터페이스

  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authorizeRequests -> authorizeRequests
                .requestMatchers("/books/add").hasRole("ADMIN")
                // 로그인 필요
                .requestMatchers("/cart/**", "/memberInfo").authenticated()
                // 홈 페이지,도서 목록, 도서 상세 인증 없이 접근 가능
                .requestMatchers(
                    "/signup",
                    "/member",
                    "/home",          // 홈 페이지 허용
                    "/books",         // 도서 목록 허용
                    "/books/**",      // 도서 상세 허용
                    "/css/**",
                    "/js/**",
                    "/uploads/**"
                ).permitAll()  // 인증 없이 접근 가능
                .anyRequest().authenticated()  // 나머지는 인증 필요
        )
        .formLogin(
            formLogin -> formLogin
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home")// 로그인 성공 시 홈으로
                .failureUrl("/loginfailed")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()  // 로그인 페이지 접근 허용
        )
        .logout(
            logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home")  // 로그아웃 후 홈으로
                .permitAll()
        )
        .userDetailsService(customUserDetailsService);  // DB 사용자 인증
    return http.build();
  }//ADMIN권한이 있는 사용자만 /books/add 경로에 접근할 수 있도록 하는 인터페이스
}
