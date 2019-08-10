package com.asinc.jrdemo.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The report export types.
 */

/**
 * Instantiates a new report export type.
 *
 * @param fileExtension the file extension
 */
@AllArgsConstructor
public enum ReportExportType {

	/** PDF. */
	PDF("pdf"),

	/** MS Office 97. */
	MS_EXCEL_97("xls"),

	/** MS Office 2003. */
	MS_EXCEL_2003("xlsx");

	/** The file extension of outputs of this type. */

	/**
	 * Gets the file extension.
	 *
	 * @return the file extension
	 */
	@Getter
	private String fileExtension;

}
