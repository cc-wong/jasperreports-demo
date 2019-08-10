package com.asinc.jrdemo.util;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The helper class for dates and times.
 */
@Component
public class DateAndTimeHelper {

	/** The system clock. */
	@Autowired
	private Clock systemClock;

	/**
	 * Formats the current system date and time.
	 *
	 * @param pattern the format pattern
	 * @return the string representation of the current system date and time
	 */
	public String formatCurrentSystemDateTime(String pattern) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(this.systemClock.instant(), this.systemClock.getZone());
		return DateTimeFormatter.ofPattern(pattern).format(localDateTime);
	}

}
