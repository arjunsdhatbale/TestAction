package com.main.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.digester.ArrayStack;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.main.model.Student;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class StudentController {

	@Autowired
	private Logger logger; 
	
	private List<Student> students = new ArrayList<>(
			List.of(new Student(1, "Arjun", 59), new Student(2, "Vishal", 94), new Student(3, "Navnath", 23)

			));

	@GetMapping("/students")
	public List<Student> getStudent() {
		logger.info("Request Receive to get All Students...");
		
		return students;
	}

	@GetMapping("/csrf-token")
	public CsrfToken getCsrfToken(HttpServletRequest httpServletRequest) {
		return (CsrfToken) httpServletRequest.getAttribute("_csrf");
		
	}
	
	
	
	@PostMapping("/students")
	public Student addStudent(@RequestBody Student student) {
		logger.info("Request receive to Save student...");
		students.add(student);
		return student;
 	}
	
	
}
