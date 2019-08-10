package com.asinc.jrdemo;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.asinc.jrdemo.report.ReportExportType;
import com.asinc.jrdemo.report.service.ReportDataService;
import com.asinc.jrdemo.report.service.ReportGenerationService;

import lombok.extern.slf4j.Slf4j;

/**
 * The application runner that allows the application to be started from the
 * command line.
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
	 * Runs the application.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	@Override
	public void run(String... args) throws Exception {
		ReportExportType exportType = ReportExportType.PDF;

		try {
			Document reportData = this.reportDataService.getReportData();
			File output = this.reportGenerationService.runReport(reportData, exportType);
			log.info("Report generated to: " + output);
		} catch (Exception e) {
			log.error("Exception thrown.", e);
		}
	}

}
