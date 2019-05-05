package com.borealis.erates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.borealis.erates.supplier.Bank;

/**
 * @author Kastalski Sergey
 */
@SpringBootApplication
public class TestExchangeRatesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestExchangeRatesApplication.class, args);
	}
	
	@Bean
	public Bank priorBank() {
		return new Bank() {
			
			@Override
			public String getBankName() {
				return "Prior Bank";
			}
			
			@Override
			public String getBankCode() {
				return "priorb";
			}
		};
	}
	
}
