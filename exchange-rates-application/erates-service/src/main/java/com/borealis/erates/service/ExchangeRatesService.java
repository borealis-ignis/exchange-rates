package com.borealis.erates.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import com.borealis.erates.model.dto.BankDto;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.repository.BanksDAO;
import com.borealis.erates.repository.CurrenciesDAO;
import com.borealis.erates.repository.ExchangeRatesDAO;
import com.borealis.erates.repository.converter.impl.BankConverter;
import com.borealis.erates.supplier.Bank;

/**
 * @author Kastalski Sergey
 */
@Service
public class ExchangeRatesService {
	
	private List<Bank> banksList;
	
	private ExchangeRatesDAO exchangeRatesDAO;
	
	private BanksDAO banksDAO;
	
	private CurrenciesDAO currenciesDAO;
	
	private BankConverter bankConverter;
	
	public ExchangeRatesService(
			final List<Bank> banksList,
			final ExchangeRatesDAO exchangeRatesDAO,
			final BanksDAO banksDAO,
			final CurrenciesDAO currenciesDAO,
			final BankConverter bankConverter) {
		this.banksList = banksList;
		this.exchangeRatesDAO = exchangeRatesDAO;
		this.banksDAO = banksDAO;
		this.currenciesDAO = currenciesDAO;
		this.bankConverter = bankConverter;
	}
	
	public List<ExchangeRateDto> getExchangeRates() {
		return null;
	}
	
	public List<Bank> getBanks() {
		final List<Bank> banks = new ArrayList<>();
		banksList.forEach(bank -> {
			if (isActiveBank(bank)) {
				banks.add(bank);
			}
		});
		return banks;
	}
	
	private boolean isActiveBank(final Bank bank) {
		if (bank.getBankCode() == null) {
			return false;
		}
		
		final List<BankDto> banksDto = bankConverter.convertDbos(banksDAO.findAll());
		if (banksDto.isEmpty()) {
			return false;
		}
		
		for (final BankDto bankDto : banksDto) {
			if (bank.getBankCode().equals(bankDto.getCode())) {
				return BooleanUtils.isTrue(bankDto.getActive());
			}
		}
		
		return false;
	}
	
}
