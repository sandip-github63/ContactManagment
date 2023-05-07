package com.contact.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class configure  extends WebSecurityConfigurerAdapter {
	
	@Bean
	public  UserDetailsService getUserDetailService() {		
		return new userDetailsServiceimp();		
	}
	
	//use to encrpt passsword 
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//authuntication done by data base 
	
	@Bean  
	public DaoAuthenticationProvider authenticationProvider() {		 
		//DaoAuthenticationProvider represent that authentication is done by 
		//database
		
		DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();		
		daoAuthenticationProvider.setUserDetailsService(getUserDetailService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
		
		
	}

	//configure method 
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {		
		auth.authenticationProvider(authenticationProvider());		
	}

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/users/**").hasRole("USER")
		.antMatchers("/admin/**").hasRole("ADMIN")
		.antMatchers("/**").permitAll().and().formLogin()
		.loginPage("/login")
		.loginProcessingUrl("/loginProcess")
		.defaultSuccessUrl("/users/index")
		.and().csrf().disable();
		
	}
	

}
