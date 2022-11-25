package org.deco.gachicoding.config;

import org.deco.gachicoding.config.filter.CustomAuthenticationProvider;
import org.deco.gachicoding.config.filter.handler.CustomLoginAuthenticationEntryPoint;
import org.deco.gachicoding.config.filter.CustomLoginConfigurer;
import org.deco.gachicoding.config.filter.handler.CustomAccessDeniedHandler;
import org.deco.gachicoding.config.filter.handler.CustomAuthenticationFailureHandler;
import org.deco.gachicoding.config.filter.handler.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

// 인가 정보 반환 기능 https://offbyone.tistory.com/217
// 시큐리티 설정 관련 자료 : https://velog.io/@seongwon97/Spring-Security-Filter%EB%9E%80
// 백기선 시큐리티 강의 : https://youtu.be/fG21HKnYt6g
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Value("${key.value}")
//    private String webServerAddress;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .anyRequest().permitAll();

        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .headers().frameOptions().disable();

        http
                .apply(new CustomLoginConfigurer())
                .successHandlerCustom(customAuthenticationSuccessHandler())
                .failureHandlerCustom(customAuthenticationFailureHandler())
                .setAuthenticationManager(authenticationManagerBean())
                .loginProcessingUrl("/api/login");

        http
                .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("http://localhost:3000")
                .addLogoutHandler(new SecurityContextLogoutHandler());

        http
                .exceptionHandling()
                .authenticationEntryPoint(new CustomLoginAuthenticationEntryPoint())
                .accessDeniedHandler(customAccessDeniedHandler());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:3001"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Accept", "X-Requested-With", "remember-me", "accesss-token", "Set-Cookie"));
        configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}