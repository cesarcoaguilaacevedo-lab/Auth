package com.cibertec.authservice.security;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
  @Bean SecurityFilterChain security(HttpSecurity http,JwtFilter filter)throws Exception {
    return http.csrf(csrf->csrf.disable())
      .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(a->a.requestMatchers("/api/auth/login").permitAll()
        .requestMatchers(org.springframework.http.HttpMethod.GET,"/api/usuarios/*").authenticated()
        .requestMatchers("/api/usuarios/**").hasRole("ADMINISTRADOR")
        .anyRequest().authenticated())
      .addFilterBefore(filter,UsernamePasswordAuthenticationFilter.class)
      .build();
  }
  @Bean PasswordEncoder encoder(){return new BCryptPasswordEncoder();}
}