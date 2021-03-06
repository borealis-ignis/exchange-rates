package com.borealis.erates.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.service.ExchangeRatesService;
import com.borealis.erates.supplier.Bank;
import com.borealis.erates.supplier.impl.NoBank;

/**
 * @author Kastalski Sergey
 */
@Controller
public class ERatesPageController {
	
	private static Logger logger = LoggerFactory.getLogger(ERatesPageController.class);
	
	private ExchangeRatesService eratesService;
	
	public ERatesPageController(final ExchangeRatesService eratesService) {
		this.eratesService = eratesService;
	}
	
	@GetMapping(path="/erates")
	public String getExchangeRatesPage(
			final Map<String, Object> model,
			@RequestParam(required = false, name = "bankCode") String bankCode,
			@RequestParam(required = false, name = "months") Integer months) {
		
		final List<Bank> banks = eratesService.getBanks();
		if (StringUtils.isBlank(bankCode)) {
			bankCode = banks.stream().filter(b -> "priorb".equals(b.getBankCode())).findFirst()
				.orElse(banks.stream().findFirst().orElse(new NoBank())).getBankCode();
		}
		
		if (months == null) {
			months = 1;
		}
		
		final List<CurrencyDto> currencies = eratesService.getCurrencies();
		final String currencyCode = eratesService.determineDefaultCurrencyCode(currencies);
		
		model.put("page", "erates");
		model.put("banks", banks);
		model.put("bankCode", bankCode);
		model.put("currencies", currencies);
		model.put("currencyCode", currencyCode);
		model.put("months", months);
		return "erates";
	}
	
	@GetMapping(path="/compare-erates")
	public String getCompareExchangeRatesPage(final Map<String, Object> model) {
		
		final List<Bank> banks = eratesService.getBanks();
		
		final List<CurrencyDto> currencies = eratesService.getCurrencies();
		final String currencyCode = eratesService.determineDefaultCurrencyCode(currencies);
		
		model.put("page", "compare-erates");
		model.put("banks", banks);
		model.put("currencies", currencies);
		model.put("currencyCode", currencyCode);
		model.put("months", 1);
		return "compare-erates";
	}
	
	@ExceptionHandler(Exception.class)
	public String handleError(final Exception ex) {
		logger.error("Internal server error", ex);
		return "error";
	}
	
}
