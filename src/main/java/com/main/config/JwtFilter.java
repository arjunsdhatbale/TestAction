package com.main.config;

import java.io.IOException;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.main.service.JWTService;
import com.main.service.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	
	ApplicationContext  applicationContext;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 // Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYWhlc2giLCJpYXQiOjE3MzIxMTg4OTMsImV4cCI6MTczMjExOTAwMX0.-Oaqs41NsNhCyFaLWKdo-HGVUOdLzbo-d23A8rjUYn0 
		
		String authHeader = request.getHeader("Authorization");
		String token = null; 
		String username = null; 
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7); 
			username = jwtService.extractUserName(token);
		}
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			//UserDetails userDetails = ((Object) applicationContext).getBean(MyUserDetailsService.class).loadUserByUsername(username);
			//UserDetails userDetails = applicationContext.getBean(MyUserDetailsService.class).loadUserByUsername(username);
			UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
			if(jwtService.validateToken(token,userDetails)) {
				UsernamePasswordAuthenticationToken  authToken = 
						new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
				
			}
		}
		filterChain.doFilter(request, response);
	}

}













