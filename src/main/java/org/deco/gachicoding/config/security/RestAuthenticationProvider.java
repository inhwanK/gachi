package org.deco.gachicoding.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@RequiredArgsConstructor
public class RestAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String loginId = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        log.info("loginId - {}", loginId);
        log.info("password - {}", password);

        UserDetails userDto = userDetailsService.loadUserByUsername(loginId);

        log.info("{}", userDto.toString());

        if (!passwordEncoder.matches(password, userDto.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new RestAuthenticationToken(userDto.getUsername(), null, userDto.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(RestAuthenticationToken.class);
    }


}
