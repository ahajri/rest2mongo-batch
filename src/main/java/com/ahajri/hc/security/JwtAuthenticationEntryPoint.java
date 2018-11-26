package com.ahajri.hc.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.ahajri.hc.enums.ErrorMessageEnum;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static Logger LOG = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        LOG.error("Responding with unauthorized error. Message - {}", ex.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                ErrorMessageEnum.UNAUTHORIZED_RESOURCE.getMessage());
    }


}
