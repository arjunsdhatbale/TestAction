package com.main.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.model.Users;

public interface UserRepo extends JpaRepository<Users, Integer>{

	Users findByUsername(String username);
	 
	 
	
}
