package un.light.mafhh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableScheduling
@SpringBootApplication
@EnableOAuth2Sso
public class Application extends 	WebSecurityConfigurerAdapter /* extends SpringBootServletInitializer */ {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**")
		.authorizeRequests()
		.antMatchers("/", "/login**", "/webjars/**")
		.permitAll()
		.anyRequest()
				.authenticated();
	}

}
