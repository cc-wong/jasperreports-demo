package com.asinc.jrdemo;

import java.io.File;
import java.time.Clock;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.asinc.jrdemo.report.ReportExportType;
import com.asinc.jrdemo.report.service.ReportDataService;
import com.asinc.jrdemo.report.service.ReportGenerationService;

import lombok.extern.slf4j.Slf4j;

/**
 * The application runner.
 */
@Component
@Slf4j
public class ApplicationRunner implements CommandLineRunner {

	/** The report data service. */
	@Autowired
	private ReportDataService reportDataService;

	/** The report generation service. */
	@Autowired
	private ReportGenerationService reportGenerationService;

	/**
	 * The system clock.
	 *
	 * @return the system clock
	 */
	@Bean
	public Clock systemClock() {
		return Clock.systemDefaultZone();
	}

	/**
	 * Runs the application from the command line.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	@Override
	public void run(String... args) throws Exception {
		try {
			Document reportData = this.reportDataService.getReportData();
			File output = this.reportGenerationService.runReport(reportData, getExportType(args));
			log.info("Report generated to: " + output);
		} catch (Exception e) {
			log.error("Exception thrown.", e);
		}
	}

	/**
	 * Gets the export type from the command line argument(s).
	 *
	 * @param args the arguments
	 * @return the export type defined by the argument; if an invalid type or none
	 *         is provided, the default - PDF - will be returned
	 */
	private ReportExportType getExportType(String... args) {
		String exportTypeArg = Arrays.stream(args).findFirst().orElse("");
		log.debug("Export type from argument: " + exportTypeArg);
		try {
			return ReportExportType.valueOf(exportTypeArg);
		} catch (IllegalArgumentException e) {
			log.debug("Invalid type or none provided; use default.");
			return ReportExportType.PDF;
		}
	}

}
