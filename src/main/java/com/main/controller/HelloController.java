package com.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

	@RequestMapping(method = RequestMethod.GET,value = "/")
	public String great(HttpServletRequest httpServletRequest ) {
		System.out.println("This is Spring Security...");
		return "Spring Security..." + httpServletRequest.getSession().getId();
	}
}
