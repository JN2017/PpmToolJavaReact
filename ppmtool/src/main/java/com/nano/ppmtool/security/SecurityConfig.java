package com.nano.ppmtool.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configurable
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	http.cors().and().csrf().disable()
					.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and() //
							.sessionManagement()
							.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
							.and()
							.headers().frameOptions().sameOrigin()
							.and()
							.authorizeRequests()
							.antMatchers(
									"/",
									"/favicon.ico",
									"/**/*.png",
									"/**/*.gif",
									"/**/*.svg",
									"/**/*.jpg",
									"/**/*.html",
									"/**/*.css",
									"/**/*.js"
							).permitAll()
							 .antMatchers("/api/users/**").permitAll() // allowing this all users urls
							 .anyRequest().authenticated();
	
	}
	
	
}
