package com.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtFilter jwtFilter;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private UserDetailsService userDetailsService;

	

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

	    return httpSecurity
	            .csrf(customizer -> customizer.disable())
	            .authorizeHttpRequests(request -> request
	                    .requestMatchers("register", "login","signin").permitAll()
	                    .anyRequest().authenticated())
	            .oauth2Client(Customizer.withDefaults())
	            .httpBasic(Customizer.withDefaults())
	            .sessionManagement(session -> session
	                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
	            .build();
	}

//		
//		UserDetails user1 = User
//				.withDefaultPasswordEncoder()

//	@Bean
//	public UserDetailsService userDetailsService() {
//				.username("vikas")
//				.password("v@123")
//				.roles("USER")
//				.build();
//		
//		UserDetails user2 = User
//				.withDefaultPasswordEncoder()
//				.username("navnath")
//				.password("n@123")
//				.roles("USER")
//				.build();
//		return new InMemoryUserDetailsManager(user1,user2);
//	}

	@Bean
	public AuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//		daoAuthenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		return daoAuthenticationProvider;

	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {

		return authenticationConfiguration.getAuthenticationManager();
	}

}
