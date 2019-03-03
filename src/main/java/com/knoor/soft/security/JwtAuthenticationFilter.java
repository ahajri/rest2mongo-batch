package com.knoor.soft.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {


   @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

			JwtTokenProvider jwtTokenProvider = WebApplicationContextUtils.
                    getRequiredWebApplicationContext(request.getServletContext()).
                    getBean(JwtTokenProvider.class);

            HCUserDetailsService sigsUserDetailsService =  WebApplicationContextUtils.
                    getRequiredWebApplicationContext(request.getServletContext()).
                    getBean(HCUserDetailsService.class);

            String jwt = getJwtFromRequest(request);
            String username = null;
            if(jwt !=null) {
            	 username =  jwtTokenProvider.getUsernameFromJWT(jwt);
            }
            

            if (StringUtils.hasText(jwt) && jwtTokenProvider.verifyToken(jwt, username)) {

                UserDetails userDetails = sigsUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }


    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
