package com.borealis.erates.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.borealis.erates.model.dto.ExchangeRateDto;
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
	
	@GetMapping("/")
	public List<ExchangeRateDto> getExchangeRates(@RequestParam(required = false, name = "currencyId") final Long currencyId) {
		final LocalDateTime defaultFromDate = LocalDateTime.now().minusMonths(1l);
		return eratesService.getExchangeRates(defaultFromDate, currencyId);
	}
	
}
