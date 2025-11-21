package org.example.bookmarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  protected PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }//PasswordEncoder : 사용자 비밀번호의 단방향 암호화를 지원하는 인터페이스

  @Bean
  protected UserDetailsService useR() {
    UserDetails admin = User.builder()
        .username("Admin")//이름 : Admin
        .password(passwordEncoder().encode("Admin1234"))//비밀번호 : Admin1234
        .roles("ADMIN")//역할 : ADMIN
        .build();
    return new InMemoryUserDetailsManager(admin);
  }
  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authorizeRequests -> authorizeRequests
                .requestMatchers("/books/add").hasRole("ADMIN")
                .anyRequest().permitAll()
        )
        .formLogin(
            formLogin->formLogin
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/books/add")
                .failureUrl("/loginfailed")
                .usernameParameter("username")
                .passwordParameter("password")
        )
        .logout(
            logout->logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
        );
    return http.build();
  }//ADMIN권한이 있는 사용자만 /books/add 경로에 접근할 수 있도록 하는 인터페이스
}
