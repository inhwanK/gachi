package org.deco.gachicoding.config;

import lombok.AllArgsConstructor;
import org.deco.gachicoding.config.jwt.JwtAuthenticationFilter;
import org.deco.gachicoding.config.jwt.JwtTokenProvider;
import org.deco.gachicoding.domain.user.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// api에도 시큐리티 접근 권한 설정이 먹힘
@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final JwtTokenProvider jwtTokenProvider;

    // 정적인 파일에 대한 요청들
    // 우리한텐 필요 없을 듯
//    private static final String[] AUTH_WHITELIST = {
//            // -- swagger ui
//            "/v2/api-docs",
//            "/v3/api-docs/**",
//            "/configuration/ui",
//            "/swagger-resources/**",
//            "/configuration/security",
//            "/swagger-ui.html",
//            "/webjars/**",
//            "/file/**",
//            "/image/**",
//            "/swagger/**",
//            "/swagger-ui/**",
//            // other public endpoints of your API may be appended to this array
//            "/h2/**"
//    };

    // 회원가입 시 비밀번호 암호화에 사용할 Encode 빈 등록
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

//    public void configure(WebSecurity web) throws Exception {
//        // 정적인 파일 요청에 대해 무시
//        web.ignoring().antMatchers(AUTH_WHITELIST);
//    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
