package com.ahajri.heaven.calendar.security;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtTokenProvider {

    private static final Logger LOG = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationInSecond}")
    private String expirationInSecond;

    @Value("${jwt.issuedBy}")
    private String issuedBy;

    @Autowired
    private HCUserDetailsService userDetailsService;

    /**
     * Crétion de jeton JWT
     *
     * @param authentication
     * @return jeton JWT crée
     */
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = new UserPrincipal((String) authentication.getPrincipal(), (String) authentication.getCredentials());
        String token = null;
        try {
            Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
            Date expiry = Date.from(LocalDateTime.now().plusSeconds(Long.parseLong(expirationInSecond))
                    .atZone(ZoneId.systemDefault()).toInstant());
            token = JWT.create()
                    .withIssuer(issuedBy)
                    .withIssuedAt(now)
                    .withExpiresAt(expiry)
                    .withSubject(userPrincipal.getUsername())
                    .sign(Algorithm.HMAC512(jwtSecret));
        } catch (JWTCreationException | UnsupportedEncodingException exception) {
            LOG.error("JWT token generation failed with an error ", exception.getMessage());
        }

        return token;

    }

    /**
     * @param token
     * @return UserPrincipal
     */
    public UserPrincipal getAuthenticatedUser(String token) {
        return (UserPrincipal) userDetailsService
                .loadUserByUsername(JWT.decode(token).getSubject());
    }

    public boolean verifyToken(String token, String username) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(jwtSecret))
                    .withIssuer(issuedBy)
                    .withSubject(username)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            Date now = new Date();
            if (jwt.getIssuedAt().after(now) || jwt.getExpiresAt().before(now)) {
                return false;
            }

        } catch (JWTVerificationException | UnsupportedEncodingException exception) {
            LOG.error(String.format("JWT token verification failed for username %s because of %s",
                    username, exception.getMessage()));
            return false;
        }
        return true;
    }


    public String getUsernameFromJWT(String token) {
        return JWT.decode(token).getSubject();
    }
}
