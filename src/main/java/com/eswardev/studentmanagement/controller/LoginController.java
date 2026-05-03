package com.eswardev.studentmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	
	@GetMapping("/showLoginPage")
	public String showLoginPage() {
		logger.info("Successfully executed showLoginPage or operation related to it");
		return "login/login-form";
	}
	
	//authenticateTheUser is automatically done by spring boot
	
	@GetMapping("/access-denied")
	public String showAccessDenied() {
		logger.info("Successfully executed showAccessDenied or operation related to it");
		return "access-denied";
	}
}
