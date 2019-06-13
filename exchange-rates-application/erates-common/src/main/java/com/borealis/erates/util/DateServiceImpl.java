package com.borealis.erates.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

/**
 * @author Kastalski Sergey
 */
@Service
public class DateServiceImpl implements DateService {
	
	@Override
	public LocalDate currentLocalDate() {
		return LocalDate.now();
	}

	@Override
	public LocalDateTime currentLocalDateTime() {
		return LocalDateTime.now();
	}
	
}
