package com.borealis.erates.repository.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.Test;

import com.borealis.erates.TestDataContainer;
import com.borealis.erates.repository.converter.impl.LocalDateTimeAttributeConverter;

/**
 * @author Kastalski Sergey
 */
public class LocalDateTimeAttributeConverterTest {
	
	private LocalDateTimeAttributeConverter converter = new LocalDateTimeAttributeConverter();
	
	@Test
	public void convertToTimestampTest() {
		final Timestamp timestamp = converter.convertToDatabaseColumn(TestDataContainer.getLocalDateTime());
		assertThat(timestamp).isEqualTo(TestDataContainer.getTimestamp());
	}
	
	@Test
	public void convertToLocalDateTimeTest() {
		final LocalDateTime localDateTime = converter.convertToEntityAttribute(TestDataContainer.getTimestamp());
		assertThat(localDateTime).isEqualTo(TestDataContainer.getLocalDateTime());
	}
	
}
