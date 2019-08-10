package com.asinc.jrdemo.report.service;

import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.asinc.jrdemo.report.ReportExportType;
import com.asinc.jrdemo.report.ReportGenerationProperties;
import com.asinc.jrdemo.report.engine.ReportEngine;

import net.sf.jasperreports.engine.JRException;

/**
 * The service class for report generation.
 */
@Service
public class ReportGenerationService {

	/** The report generation properties. */
	@Autowired
	private ReportGenerationProperties reportGenerationProperties;

	/** The report engine. */
	@Autowired
	private ReportEngine reportEngine;

	/**
	 * Runs the report.
	 *
	 * @param data       the report data
	 * @param exportType the export type
	 * @return the output file
	 * @throws JRException the JR exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public File runReport(Document data, ReportExportType exportType) throws JRException, IOException {
		return this.reportEngine.runReport(data, this.reportGenerationProperties, exportType);
	}

}
