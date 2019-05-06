package com.borealis.erates.controller;

import org.springframework.web.bind.annotation.RestController;

import com.borealis.erates.service.ExchangeRatesService;

/**
 * @author Kastalski Sergey
 */
@RestController("/exchangerates")
public class ERatesRestController {
	
	private ExchangeRatesService eratesService;
	
	
	public ERatesRestController(final ExchangeRatesService eratesService) {
		this.eratesService = eratesService;
	}
	
	public void getBanks() {
		
	}
	
}
