package com.asinc.jrdemo.report.engine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.asinc.jrdemo.report.ReportExportType;
import com.asinc.jrdemo.report.ReportGenerationProperties;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.AbstractXlsReportConfiguration;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 * The engine for report generation.
 */
@Component
@ConfigurationProperties(prefix = "report.engine")

/** The Constant log. */
@Slf4j
public class ReportEngine {

	/** The file path separator. */
	private static final String FILE_PATH_SEPARATOR = "/";

	/** The file extension separator. */
	private static final String FILE_EXTENSION_SEPARATOR = ".";

	/** The template directory path. */

	/**
	 * Sets the template path.
	 *
	 * @param templatePath the new template path
	 */
	@Setter
	private String templatePath;

	/** The output directory path. */

	/**
	 * Sets the output directory path.
	 *
	 * @param outputDirectoryPath the new output directory path
	 */
	@Setter
	private String outputDirectoryPath;

	/**
	 * Runs a report.
	 *
	 * @param data                 the report data
	 * @param generationProperties the report generation properties
	 * @param exportType           the export type
	 * @return the file
	 * @throws JRException the JR exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public File runReport(Document data, ReportGenerationProperties generationProperties, ReportExportType exportType)
			throws JRException, IOException {
		ExporterInput exporterInput = populateData(data, generationProperties);
		File outputFile = initOutputFile(generationProperties, exportType);
		switch (exportType) {
		case MS_EXCEL_2003:
			exportToXlsx(generationProperties, exporterInput, outputFile);
			break;
		case PDF:
			exportToPdf(generationProperties, exporterInput, outputFile);
			break;
		default:
			throw new IllegalArgumentException(String.format("Export type [%s] not yet supported!", exportType));
		}

		return outputFile;
	}

	/**
	 * Populates the data to the report.
	 *
	 * @param data                 the report data
	 * @param generationProperties the report generation properties
	 * @return the exporter input
	 * @throws JRException the JR exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private ExporterInput populateData(Document data, ReportGenerationProperties generationProperties)
			throws JRException, IOException {
		JasperReport template = getTemplate(generationProperties);
		JRDataSource dataSource = new JRXmlDataSource(data, generationProperties.getDataSelectExpression());
		Map<String, Object> parameters = new HashMap<>();
		JasperPrint jasperPrint = JasperFillManager.fillReport(template, parameters, dataSource);
		SimpleExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
		return exporterInput;
	}

	/**
	 * Gets the template.
	 *
	 * @param generationProperties the report generation properties
	 * @return the template object
	 * @throws JRException the JR exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private JasperReport getTemplate(ReportGenerationProperties generationProperties) throws JRException, IOException {
		String templateFilePath = this.templatePath + FILE_PATH_SEPARATOR + generationProperties.getTemplateFileName();
		log.debug("Template file path: " + templateFilePath);
		try (InputStream templateStream = getClass().getResourceAsStream(templateFilePath)) {
			return (JasperReport) JRLoader.loadObject(templateStream);
		}
	}

	/**
	 * Initializes the output file.
	 *
	 * @param generationProperties the report generation properties
	 * @param exportType           the export type
	 * @return the file object; the file name will be appended with a timestamp
	 */
	private File initOutputFile(ReportGenerationProperties generationProperties, ReportExportType exportType) {
		return new File(this.outputDirectoryPath + FILE_PATH_SEPARATOR
				+ String.format("%s_%s", generationProperties.getOutputFileName(), getTimestampForFileName())
				+ FILE_EXTENSION_SEPARATOR + exportType.getFileExtension());
	}

	/**
	 * Gets the timestamp to use as the suffix of the output file name.
	 *
	 * @return the timestamp string in {@code yyyyMMdd-HHmmss}
	 */
	private String getTimestampForFileName() {
		Clock clock = Clock.systemDefaultZone();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(clock.instant(), clock.getZone());
		return DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(localDateTime);
	}

	/**
	 * Initializes the report exporter output.
	 *
	 * @param file the output file
	 * @return the output stream exporter output
	 */
	private SimpleOutputStreamExporterOutput initExporterOutput(File file) {
		return new SimpleOutputStreamExporterOutput(file);
	}

	/**
	 * Exports the report to a PDF file.
	 *
	 * @param generationProperties the report generation properties
	 * @param exporterInput        the exporter input
	 * @param outputFile           the data object representing the output file;
	 *                             expects a PDF file
	 * @throws JRException the JR exception
	 */
	private void exportToPdf(ReportGenerationProperties generationProperties, ExporterInput exporterInput,
			File outputFile) throws JRException {
		OutputStreamExporterOutput exporterOutput = initExporterOutput(outputFile);
		try {
			SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
			reportConfig.setSizePageToContent(false);
			reportConfig.setForceLineBreakPolicy(false);

			SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
			exportConfig.setMetadataTitle(generationProperties.getOutputFileTitle());
			exportConfig.setMetadataCreator(generationProperties.getOutputFileCreator());
			exportConfig.setMetadataAuthor(generationProperties.getOutputFileCreator());
			exportConfig.setAllowedPermissionsHint("PRINTING");

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setConfiguration(reportConfig);
			exporter.setConfiguration(exportConfig);

			exporter.setExporterInput(exporterInput);
			exporter.setExporterOutput(exporterOutput);
			exporter.exportReport();
		} finally {
			exporterOutput.close();
		}
	}

	/**
	 * Exports the report to a MS Excel 2003 file.
	 *
	 * @param generationProperties the report generation properties
	 * @param exporterInput        the exporter input
	 * @param outputFile           the data object representing the output file;
	 *                             expects an XLSX file
	 * @throws JRException the JR exception
	 */
	private void exportToXlsx(ReportGenerationProperties generationProperties, ExporterInput exporterInput,
			File outputFile) throws JRException {
		OutputStreamExporterOutput exporterOutput = initExporterOutput(outputFile);
		try {
			SimpleXlsxReportConfiguration reportConfig = initXlsReportConfiguration(
					SimpleXlsxReportConfiguration.class);
			reportConfig.setSheetNames(new String[] { "DATA" });

			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setConfiguration(reportConfig);

			exporter.setExporterInput(exporterInput);
			exporter.setExporterOutput(exporterOutput);
			exporter.exportReport();
		} finally {
			exporterOutput.close();
		}
	}

	/**
	 * Initializes an MS Excel report configuration.
	 * <p>
	 * The normal practice would be simply calling the constructor of the actual
	 * class; this method demonstrates the relationship between the XLS report
	 * configuration class types.
	 *
	 * @param <T>              the report configuration type to initialize
	 * @param reportConfigType the class of the report configuration type to
	 *                         initialize
	 * @return a new instance of the report configuration
	 */
	private <T extends AbstractXlsReportConfiguration> T initXlsReportConfiguration(Class<T> reportConfigType) {
		try {
			T config = reportConfigType.getDeclaredConstructor().newInstance();
			config.setSheetNames(new String[] { "DATA" });
			return config;
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			throw new IllegalArgumentException(String.format(
					"Tried to initialize a report config of unsupported type [%s].", reportConfigType.getName()), e);
		} catch (IllegalArgumentException | SecurityException e) {
			throw e;
		}
	}

}
