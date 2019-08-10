package com.asinc.jrdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

/**
 * Initializes the Spring application for tests.
 */
@SpringBootApplication
@Profile("test")
public class ApplicationTest {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ApplicationTest.class);
		app.setLogStartupInfo(false);
		app.run(args);
	}

}
