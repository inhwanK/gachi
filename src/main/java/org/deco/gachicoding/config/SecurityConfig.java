package org.deco.gachicoding.config;

import org.deco.gachicoding.user.application.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

// 시큐리티 설정 관련 자료 : https://velog.io/@seongwon97/Spring-Security-Filter%EB%9E%80
// 백기선 시큐리티 강의 : https://youtu.be/fG21HKnYt6g
@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${key.value}")
    private String webServerAddress;

    @Autowired
    private UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors().configurationSource(corsConfigurationSource())
        .and()
                .csrf().disable()
                .headers().frameOptions().disable()
        .and()
                .authorizeRequests().antMatchers()
                .authenticated().anyRequest()
                .permitAll()
        .and()
                .addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // 향 후 내가 원하는 인증 Dto에 맞게 provider를 구현하면,
        // 내가 원하는 authentication 클래스 또는 객체를 구현할 수 있고,
        // 그러한 authentication을 파라미터로 받는 authenticationManager를 만들 수도 있을 듯
        AuthenticationManager authenticationManager = super.authenticationManagerBean();
        return authenticationManager;
    }

    @Bean
    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
        AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
        return ajaxLoginProcessingFilter;
    }

    @Bean
    public PasswordEncoder encoderPassword() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
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