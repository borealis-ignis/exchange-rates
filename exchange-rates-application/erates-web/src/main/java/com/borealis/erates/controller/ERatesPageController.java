package com.borealis.erates.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String getSingleRecipePage(
			final Map<String, Object> model,
			@RequestParam(required = false, name = "bankCode") String bankCode) {
		final List<Bank> banks = eratesService.getBanks();
		if (StringUtils.isBlank(bankCode)) {
			final Optional<Bank> bankOpt = banks.stream().findFirst();
			bankCode = bankOpt.orElse(new NoBank()).getBankCode();
		}
		model.put("banks", banks);
		model.put("bankCode", bankCode);
		return "erates";
	}
	
}
