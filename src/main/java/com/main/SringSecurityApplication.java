package com.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.main")
public class SringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SringSecurityApplication.class, args);
	 	
	}

	@Bean
    public Logger logger() {
        return LoggerFactory.getLogger("MyAppLogger");
    }
	
}
