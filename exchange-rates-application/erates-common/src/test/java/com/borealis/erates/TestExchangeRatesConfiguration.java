package com.borealis.erates;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.borealis.erates.supplier.Bank;

/**
 * @author Kastalski Sergey
 */
@Profile("test")
@Configuration
public class TestExchangeRatesConfiguration {
	
	@Bean
	public Bank testPriorBank() {
		return new Bank() {
			
			@Override
			public String getBankName() {
				return "Test Prior Bank";
			}
			
			@Override
			public String getBankCode() {
				return "priorb";
			}
		};
	}
	
}
