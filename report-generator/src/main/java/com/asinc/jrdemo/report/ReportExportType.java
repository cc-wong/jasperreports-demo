package com.asinc.jrdemo.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The report export types.
 */
@AllArgsConstructor
public enum ReportExportType {

	/** PDF. */
	PDF("pdf"),

	/** MS Office 97. */
	MS_EXCEL_97("xls"),

	/** MS Office 2003. */
	MS_EXCEL_2003("xlsx"),

	/** HTML. */
	HTML("html");

	/** The file extension of outputs of this type. */
	@Getter
	private String fileExtension;

}
