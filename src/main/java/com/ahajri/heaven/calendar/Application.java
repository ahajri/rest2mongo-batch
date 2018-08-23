package com.ahajri.heaven.calendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Application /*extends WebSecurityConfigurerAdapter*/

{

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//	private CsrfTokenRepository csrfTokenRepository() {
//		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//		repository.setSessionAttributeName("_csrf");
//		return repository;
//	}
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//
//		http.antMatcher("/**")
//		.authorizeRequests()
//		.antMatchers("/**", "/auth**", "/webjars/**").permitAll().anyRequest()
//				.permitAll();
//
//		http.csrf().disable().authorizeRequests().antMatchers("/").permitAll();
//
//	}

}
