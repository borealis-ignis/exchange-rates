package com.borealis.erates.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.service.ExchangeRatesService;

/**
 * @author Kastalski Sergey
 */
@RestController
public class ERatesRestController {
	
	private static Logger logger = LoggerFactory.getLogger(ERatesRestController.class);
	
	private ExchangeRatesService eratesService;
	
	
	public ERatesRestController(final ExchangeRatesService eratesService) {
		this.eratesService = eratesService;
	}
	
	@GetMapping(path = "/exchangerates")
	public List<ExchangeRateDto> getExchangeRates(
			@RequestParam(required = true, name = "currencyId") final Long currencyId,
			@RequestParam(required = true, name = "months") Long months) {
		
		if (months == null) {
			months = 1l;
		}
		
		final LocalDateTime defaultFromDate = LocalDateTime.now().minusMonths(months);
		return eratesService.getExchangeRates(defaultFromDate, currencyId);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleError(final Exception ex) {
		logger.error("Internal server error", ex);
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
