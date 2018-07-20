package com.ahajri.heaven.calendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@EnableScheduling
@SpringBootApplication
// @EnableOAuth2Sso
public class Application /*
							 * extends WebSecurityConfigurerAdapter extends SpringBootServletInitializer
							 */
{

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setSessionAttributeName("_csrf");
		return repository;
	}

	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	// /*
	// * http.antMatcher("/**").authorizeRequests().antMatchers("/**",
	// * "/auth**", "/webjars/**").permitAll() .anyRequest().authenticated();
	// */
	// http.csrf().disable()
	// .authorizeRequests().antMatchers("/").permitAll();
	//
	// }

}
