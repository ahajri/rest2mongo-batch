package com.ahajri.heaven.calendar.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
		.antMatchers("/v2/api-docs", "/configuration/ui", 
				"/swagger-resources", "/configuration/security",
				"/swagger-ui.html", "/webjars/**","/login*");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Note:
		// Use this to enable the tomcat basic authentication (tomcat popup rather than
		// spring login page)
		// Note that the CSRf token is disabled for all requests
		log.info("Disabling CSRF, enabling basic authentication...");

		http.csrf().disable()
		.authorizeRequests()
		.antMatchers("/**").permitAll();
		
//		.antMatchers("/login**").permitAll()
//		.and()
//		.authorizeRequests()
//		.antMatchers("/", "/csrf", "/v2/api-docs", "/swagger-resources/configuration/ui", 
//				 "/configuration/ui", "/swagger-resources", "/swagger-resources/configuration/security", 
//				 "/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
//		.and()
//		.authorizeRequests()
//		.anyRequest().authenticated()
//		.and()
//		.httpBasic()
//		.and()
//		.sessionManagement().disable();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
