package com.asinc.jrdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

/**
 * The main class for the Spring Boot application.
 */
@SpringBootApplication
@Profile("!test")
public class JrdemoReportgenApplication {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(JrdemoReportgenApplication.class, args);
	}

}
