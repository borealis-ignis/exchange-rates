package com.borealis.erates.supplier.nbrb;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.borealis.erates.util.DateService;

/**
 * @author Kastalski Sergey
 */
public class MockDateService implements DateService {
	
	@Override
	public LocalDate currentLocalDate() {
		return LocalDate.parse("2019-06-10");
	}
	
	@Override
	public LocalDateTime currentLocalDateTime() {
		return LocalDateTime.parse("2019-06-10T16:03:00");
	}
	
}
