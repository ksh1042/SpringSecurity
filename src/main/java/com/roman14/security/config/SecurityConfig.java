package com.roman14.security.config;

import com.roman14.security.oauth2.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // 스프링 시큐리티 필터가 스프링 필터체인에 등록되도록 하는 어노테이션
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig
{
  private final PrincipalOauth2UserService principalOauth2UserService;

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder()
  {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception
  {
    return security
      .csrf().disable()
      .cors().disable()
      .authorizeRequests(request -> request
        .antMatchers("/user/**").authenticated()
        .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
        .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
        .anyRequest().permitAll()
      )
      .formLogin(login -> login
        .loginPage("/login/form").permitAll()
        .loginProcessingUrl("/login")
        .defaultSuccessUrl("/")
      )
      .oauth2Login(oauth2 -> {oauth2
        .loginPage("/login/form").permitAll()
        .userInfoEndpoint(endPoint -> { endPoint
          .userService(this.principalOauth2UserService);
        });
      })
      .build();
  }
}
