package org.deco.gachicoding.config;

import org.deco.gachicoding.config.security.RestAuthenticationProvider;
import org.deco.gachicoding.config.security.RestLoginAuthenticationEntryPoint;
import org.deco.gachicoding.config.security.CustomLoginProcessingFilter;
import org.deco.gachicoding.config.security.handler.CustomAccessDeniedHandler;
import org.deco.gachicoding.config.security.handler.CustomAuthenticationFailureHandler;
import org.deco.gachicoding.config.security.handler.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
@Order(2)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${key.value}")
    private String webServerAddress;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(restAuthenticationProvider());
    }

    @Bean
    public AuthenticationProvider restAuthenticationProvider() {
        return new RestAuthenticationProvider(passwordEncoder(), userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .addFilter(customLoginProcessingFilter());

        http
                .exceptionHandling()
                .authenticationEntryPoint(new RestLoginAuthenticationEntryPoint())
                .accessDeniedHandler(restAccessDeniedHandler());

        http
                .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("http://localhost:3000")
                .addLogoutHandler(new SecurityContextLogoutHandler());
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CustomLoginProcessingFilter customLoginProcessingFilter() throws Exception {
        CustomLoginProcessingFilter customLoginProcessingFilter = new CustomLoginProcessingFilter();
        customLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
        customLoginProcessingFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        customLoginProcessingFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        return customLoginProcessingFilter;
    }

    @Bean
    public AccessDeniedHandler restAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:3001", webServerAddress));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Accept", "X-Requested-With", "remember-me", "accesss-token", "Set-Cookie"));
        configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}