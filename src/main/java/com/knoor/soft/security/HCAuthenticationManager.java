package com.knoor.soft.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.knoor.soft.enums.ErrorMessageEnum;

@Component(BeanIds.AUTHENTICATION_MANAGER)
public class HCAuthenticationManager implements AuthenticationManager {

    @Autowired
    private HCUserDetailsService sigsUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String password = (String) authentication.getCredentials();
        String username = (String) authentication.getPrincipal();

        if (!sigsUserDetailsService.verifyCredentials(new UserPrincipal(username, password))) {
            throw new AuthenticationCredentialsNotFoundException(ErrorMessageEnum.AUTHENTIFICATION_KO.getMessage());
        }

        UserPrincipal principal = (UserPrincipal) sigsUserDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(principal.getUsername(), principal.getPassword(), principal.getAuthorities());
    }
}
