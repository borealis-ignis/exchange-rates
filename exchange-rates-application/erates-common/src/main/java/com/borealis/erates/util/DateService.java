package com.borealis.erates.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Kastalski Sergey
 */
public interface DateService {
	
	LocalDate currentLocalDate();
	
	LocalDateTime currentLocalDateTime();
	
}
