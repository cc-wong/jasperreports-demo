package com.asinc.jrdemo.report;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * The properties for report generation.
 */
@Component
@ConfigurationProperties(prefix = "report.generation.properties")
@Data
public class ReportGenerationProperties {

	/** The template file name. */
	private String templateFileName;

	/** The data select expression for the data source. */
	private String dataSelectExpression;

	/** The output file name without the extension. */
	private String outputFileName;

	/** The title of the output file. */
	private String outputFileTitle;

	/** The name of the output file creator. */
	private String outputFileCreator;

}
