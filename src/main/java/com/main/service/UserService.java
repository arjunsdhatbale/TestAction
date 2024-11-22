package com.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.main.model.Users;
import com.main.repo.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo; 

	@Autowired
	private JWTService jwtService; 
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
	 
	public Users register(Users user) {
	   
		
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		return userRepo.save(user);
		 
	}

	public String verify(Users user) {
 
		Authentication authentication = 
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		if(authentication.isAuthenticated()) {
			return  jwtService.generateToken(user.getUsername());
		}
		
		
		return "fail";
	}
	
}












