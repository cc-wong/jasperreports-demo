package com.asinc.jrdemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * The Spring runner test on the application.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class JrdemoReportgenApplicationTests {

	/**
	 * Tests loading the application context.
	 */
	@Test
	public void contextLoads() {
	}

}
