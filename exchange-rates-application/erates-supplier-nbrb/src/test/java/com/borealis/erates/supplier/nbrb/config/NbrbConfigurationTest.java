package com.borealis.erates.supplier.nbrb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.borealis.erates.supplier.nbrb.MockDateService;
import com.borealis.erates.util.DateService;

/**
 * @author Kastalski Sergey
 */
@Configuration
@Profile("test")
public class NbrbConfigurationTest {
	
	@Bean
	@Primary
	public DateService getDateService() {
		return new MockDateService();
	}
	
}
