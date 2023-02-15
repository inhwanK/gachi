package org.deco.gachicoding.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.user.dto.request.authentication.LoginRequestDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public CustomLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        LoginRequestDto requestDto = objectMapper.readValue(request.getReader(), LoginRequestDto.class);

        if (!StringUtils.hasText(requestDto.getUserEmail()) || !StringUtils.hasText(requestDto.getPassword())) {
            throw new IllegalArgumentException("Username or Password is empty");
        }

        CustomAuthenticationToken customAuthenticationToken =
                new CustomAuthenticationToken(requestDto.getUserEmail(), requestDto.getPassword());

        return getAuthenticationManager().authenticate(customAuthenticationToken);
    }
}
