package org.deco.gachicoding.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// api에도 시큐리티 접근 권한 설정이 먹힘
// 시큐리티 설정 관련 자료 : https://velog.io/@seongwon97/Spring-Security-Filter%EB%9E%80
@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /*
     * 레퍼런스에서 권장하는 인코더 생성 방법
     * PasswordEncoderFactories.createDelegatingPasswordEncoder()
     * {bcrypt}1234 방식으로 비밀번호를 저장해야 함. 인코딩방식이 변화되어도 계속해서 사용할 수 있음.
     */
    @Bean
    public BCryptPasswordEncoder encoderPassword() {
        return new BCryptPasswordEncoder();
    }

    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests().antMatchers()
                .authenticated().anyRequest()
                .permitAll();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
