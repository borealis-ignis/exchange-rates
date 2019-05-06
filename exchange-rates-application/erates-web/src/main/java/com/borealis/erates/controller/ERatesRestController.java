package com.borealis.erates.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import com.borealis.erates.service.ExchangeRatesService;

/**
 * @author Kastalski Sergey
 */
@RestController("/exchangerates")
public class ERatesRestController {
	
	private static Logger logger = LoggerFactory.getLogger(ERatesRestController.class);
	
	private ExchangeRatesService eratesService;
	
	
	public ERatesRestController(final ExchangeRatesService eratesService) {
		this.eratesService = eratesService;
	}
	
	public void getBanks() {
		
	}
	
}
