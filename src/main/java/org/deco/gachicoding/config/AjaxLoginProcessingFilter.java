package org.deco.gachicoding.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.deco.gachicoding.user.application.UserAuthenticationDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public AjaxLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        // 우리 프로젝트에서는 사실 필요없는 로직
        // 폼 로그인을 처리할 웹 서버가 없기 때문
        // provider가 만들어지고 api가 정리되면 삭제될 예정
        if (!isRestApiRequest(request)) {
            throw new IllegalStateException("Authentication is not supported");
        }

        UserAuthenticationDto userDto = objectMapper.readValue(request.getReader(), UserAuthenticationDto.class);
        if(StringUtils.hasText(userDto.getUsername()) || StringUtils.hasText(userDto.getPassword())) {
            throw new IllegalArgumentException("Username or Password is empty");
        }

        AjaxAuthenticationToken ajaxAuthenticationToken = new AjaxAuthenticationToken(userDto.getUsername(), userDto.getPassword());

        return getAuthenticationManager().authenticate(ajaxAuthenticationToken);
    }

    private boolean isRestApiRequest(HttpServletRequest request) {
        if("XMLHttpRequest".equals(request.getHeader("X-Request-With"))) {
            return true;
        }
        return false;
    }
}
