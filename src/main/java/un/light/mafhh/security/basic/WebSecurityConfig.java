package un.light.mafhh.security.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@EnableWebSecurity
//@Configuration
public class WebSecurityConfig /*extends WebSecurityConfigurerAdapter */{
	@Autowired
	private AuthenticationEntryPoint authEntryPoint;
	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable().authorizeRequests()
//		.anyRequest().authenticated()
//		.and().httpBasic()
//		.authenticationEntryPoint(authEntryPoint);
//		http.authorizeRequests().anyRequest()
//		.fullyAuthenticated()
//		.and().httpBasic()
//		.and().csrf()
//		.disable();
//	}
}
