package com.asinc.jrdemo.report.service;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.DOMWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import lombok.extern.slf4j.Slf4j;

/**
 * The service class for report data retrieval.
 */
@Service
@Slf4j
public class ReportDataService {

	/** The report data. */
	@Value("${report.data}")
	private String reportData;

	/**
	 * Gets the report data from the application properties file(s).
	 *
	 * @return the report data
	 * @throws DocumentException    the document exception
	 * @throws TransformerException the transformer exception
	 * @throws IOException          Signals that an I/O exception has occurred.
	 */
	public Document getReportData() throws DocumentException, TransformerException, IOException {
		Document document = convertToStandardApi(DocumentHelper.parseText(reportData));
		log.debug("XML report data:\n" + prettyPrintXml(document));
		return document;
	}

	/**
	 * Converts a Dom4j XML document to standard Java SE API document.
	 *
	 * @param dom4jDocument the XML document to convert
	 * @return the converted document
	 * @throws DocumentException the document exception
	 * @see <a href=
	 *      "https://stackoverflow.com/questions/8925224/difference-between-org-w3c-dom-document-and-org-dom4j-document">
	 *      https://stackoverflow.com/questions/8925224/difference-between-org-w3c-dom-document-and-org-dom4j-document</a>
	 */
	private Document convertToStandardApi(org.dom4j.Document dom4jDocument) throws DocumentException {
		DOMWriter xmlWriter = new DOMWriter();
		return xmlWriter.write(dom4jDocument);
	}

	/**
	 * Pretty prints an XML document.
	 *
	 * @param document the XML document
	 * @return the pretty-print string representation of the XML document
	 * @throws TransformerException the transformer exception
	 * @throws IOException          Signals that an I/O exception has occurred.
	 * @see <a href=
	 *      "https://stackoverflow.com/questions/139076/how-to-pretty-print-xml-from-java">
	 *      https://stackoverflow.com/questions/139076/how-to-pretty-print-xml-from-java</a>
	 */
	private String prettyPrintXml(Document document) throws TransformerException, IOException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		try (StringWriter writer = new StringWriter()) {
			StreamResult result = new StreamResult(writer);
			DOMSource source = new DOMSource(document);
			transformer.transform(source, result);
			return result.getWriter().toString();
		}
	}

}
