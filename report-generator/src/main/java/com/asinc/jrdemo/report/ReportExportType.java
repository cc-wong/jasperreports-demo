package com.asinc.jrdemo.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The report export types.
 */
@AllArgsConstructor
public enum ReportExportType {

	/** PDF. */
	PDF("pdf");

	/** The file extension of outputs of this type. */
	@Getter
	private String fileExtension;

}
