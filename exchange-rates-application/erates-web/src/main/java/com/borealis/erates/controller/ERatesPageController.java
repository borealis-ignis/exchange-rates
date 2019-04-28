package com.borealis.erates.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.borealis.erates.repository.model.dto.BankDto;
import com.borealis.erates.service.ExchangeRatesService;

/**
 * @author Kastalski Sergey
 */
@Controller
public class ERatesPageController {
	
	private ExchangeRatesService eratesService;
	
	public ERatesPageController(final ExchangeRatesService eratesService) {
		this.eratesService = eratesService;
	}
	
	@GetMapping(path="/erates")
	public String getSingleRecipePage(
			final Map<String, Object> model,
			@RequestParam(required = false, name = "bankCode") String bankCode) {
		final List<BankDto> banks = eratesService.getBanks();
		if (StringUtils.isBlank(bankCode)) {
			final Optional<BankDto> bankOpt = banks.stream().findFirst();
			bankCode = bankOpt.orElse(new BankDto()).getCode();
		}
		model.put("banks", banks);
		model.put("bankCode", bankCode);
		return "erates";
	}
	
}
