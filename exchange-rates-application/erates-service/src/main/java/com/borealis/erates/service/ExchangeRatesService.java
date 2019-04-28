package com.borealis.erates.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.borealis.erates.repository.model.dto.BankDto;
import com.borealis.erates.supplier.Bank;

/**
 * @author Kastalski Sergey
 */
@Service
public class ExchangeRatesService {
	
	private List<Bank> banksList;
	
	public ExchangeRatesService(final List<Bank> banksList) {
		this.banksList = banksList;
	}
	
	public void getExchangeRates() {
		
	}
	
	public List<BankDto> getBanks() {
		final List<BankDto> bankDtos = new ArrayList<>();
		banksList.forEach(bank -> {
			final BankDto dto = new BankDto();
			dto.setCode(bank.getBankCode());
			dto.setName(bank.getBankName());
			bankDtos.add(dto);
		});
		return bankDtos;
	}
	
}
