package org.deco.gachicoding.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class RestLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public RestLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        log.info("이것이 유저 이메일이다. > {}", request.getAttribute("user_email"));
        log.info("이것이 유저 이메일이다2. > {}", request.getParameter("user_email"));

        UserAuthenticationDto userDto = objectMapper.readValue(request.getReader(), UserAuthenticationDto.class);
        log.info("{}", userDto.toString());
        if (!StringUtils.hasText(userDto.getUsername()) || !StringUtils.hasText(userDto.getPassword())) {
            throw new IllegalArgumentException("Username or Password is empty");
        }

        RestAuthenticationToken restAuthenticationToken = new RestAuthenticationToken(userDto.getUsername(), userDto.getPassword());

        log.info("{}", restAuthenticationToken.toString());
        return getAuthenticationManager().authenticate(restAuthenticationToken);
    }
}
