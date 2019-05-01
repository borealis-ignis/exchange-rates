package com.borealis.erates.repository.converter.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Kastalski Sergey
 */
@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
	
	@Override
	public Timestamp convertToDatabaseColumn(final LocalDateTime attribute) {
		return (attribute == null)? null : Timestamp.valueOf(attribute);
	}
	
	@Override
	public LocalDateTime convertToEntityAttribute(final Timestamp dbData) {
		return (dbData == null)? null : dbData.toLocalDateTime();
	}
	
}
