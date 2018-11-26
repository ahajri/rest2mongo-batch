package com.ahajri.hc.config;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.ahajri.hc.security.JwtAuthenticationEntryPoint;
import com.ahajri.hc.security.JwtAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final RequestMatcher SECURITY_EXCLUSION_MATCHER;

    static {
        String[] urls = new String[]{
                "/login",
                "/status",
                "/swagger-ui.html",
                "/webjars/**"
        };

        LinkedList<RequestMatcher> matcherList = new LinkedList<>();

        for (String url : urls) {
            matcherList.add(new AntPathRequestMatcher(url));
        }

        //Link Matchers in "OR" config.
        SECURITY_EXCLUSION_MATCHER = new OrRequestMatcher(matcherList);
    }
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;


    @Override
    public void configure(HttpSecurity  http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .addFilterAfter(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                .permitAll()
                .antMatchers("/events/**")
                .authenticated()
                .antMatchers("/user/**")
                .authenticated();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/webjars/**","/login", "/swagger-ui.html", "/status")
                .requestMatchers(SECURITY_EXCLUSION_MATCHER);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
