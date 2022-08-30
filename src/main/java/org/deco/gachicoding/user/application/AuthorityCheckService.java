package org.deco.gachicoding.user.application;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

public class AuthorityCheckService {
    // 유저가 해당 리소스에 권한이
        // 있을경우 true
        // 없을경우 false

    public boolean isUserWithAuthorityToResource() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//        AuthenticationTrustResolverImpl 클래스 고려해보기
        // AccessDecisionManager 고려 해보기
        if(authentication instanceof AnonymousAuthenticationToken) {
            // 어나니머스가 가능한지 테스트하는 메소드?
        }
        //
        //
        return false;
    }
}
