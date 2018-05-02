package com.ahajri.heaven.calendar.security.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

//@Component
public class SpringSecurityConfig /*extends WebSecurityConfigurerAdapter */{

//	@Autowired
//	private AuthenticationEntryPoint authEntryPoint;
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf()
//		.disable().authorizeRequests().
//		anyRequest().authenticated().and().httpBasic()
//				.authenticationEntryPoint(authEntryPoint);
//	}
//
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
//		.withUser("admin").password("admin")
//		.roles("USER");
//	}
}
