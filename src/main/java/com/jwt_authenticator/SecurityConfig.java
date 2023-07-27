package com.jwt_authenticator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	/**
	 * TO PERFORM THE SECURITY CONFIG
	 * 
	 */

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}

	// definition of roles and users
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.inMemoryAuthentication()
				.withUser("user1")
				.password("{noop}user1") // {noop} is set so as not to force the use of an encryption mechanism
				.roles("USER")
				.and()
				.withUser("admin")
				.password("{noop}admin")
				.roles("USER", "ADMIN");

		/*
		 * the following would be for the case that
		 * we would like to encrypt the password:
		 * String pw1=new BCryptPasswordEncoder().encode("user1");
		 * System.out.println(pw1);
		 * auth
		 * .inMemoryAuthentication()
		 * .withUser("user1")
		 * .password("{bcrypt}"+pw1)
		 * //.password(pw1)
		 * .roles("USER")
		 * .and()
		 * .withUser("admin")
		 * .password(new BCryptPasswordEncoder().encode("admin"))
		 * .roles("USER", "ADMIN");
		 */

		/*
		 * the following configuration will be for the case of
		 * users in a database
		 * auth.jdbcAuthentication().dataSource(dataSource)
		 * .usersByUsernameQuery("select username, password, enabled"
		 * + " from users where username=?")
		 * .authoritiesByUsernameQuery("select username, authority "
		 * + "from authorities where username=?");
		 */
	}

	// definition of security policies
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				// only the members of the administrator role can register
				// and to see the list of people they will have to be authenticated
				.antMatchers(HttpMethod.GET, "/api/persons").hasRole("ADMIN")
				.antMatchers("/api/persons").authenticated()
				.and()
				.addFilter(new JWTAuthorizationFilter(authenticationManager()));

	}
}
