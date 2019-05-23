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
			@RequestParam(required = false, name = "bankCode") String bankCode) {
		
		final List<Bank> banks = eratesService.getBanks();
		if (StringUtils.isBlank(bankCode)) {
			bankCode = banks.stream().filter(b -> "priorb".equals(b.getBankCode())).findFirst()
				.orElse(banks.stream().findFirst().orElse(new NoBank())).getBankCode();
		}
		
		final List<CurrencyDto> currencies = eratesService.getCurrencies();
		final String currencyCode = currencies.stream().filter(c -> "USD".equals(c.getCode())).findFirst()
							.orElse(currencies.stream().findFirst().orElse(new CurrencyDto())).getCode();
		
		model.put("banks", banks);
		model.put("bankCode", bankCode);
		model.put("currencies", currencies);
		model.put("currencyCode", currencyCode);
		return "erates";
	}
	
	@ExceptionHandler(Exception.class)
	public String handleError(final Exception ex) {
		logger.error("Internal server error", ex);
		return "error";
	}
	
}
