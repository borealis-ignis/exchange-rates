package com.borealis.erates.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.borealis.erates.repository.model.dbo.BankDbo;
import com.borealis.erates.repository.model.dbo.CurrencyDbo;
import com.borealis.erates.repository.model.dbo.ExchangeRateDbo;

/**
 * @author Kastalski Sergey
 */
class DBTestUtil {
	
	static BankDbo createBank(final String code, final boolean active) {
		return createBank(null, code, active);
	}
	
	static BankDbo createBank(final Long id, final String code, final boolean active) {
		final BankDbo bank = new BankDbo();
		if (id != null) {
			bank.setId(id);
		}
		bank.setCode(code);
		bank.setActive(active);
		return bank;
	}

	static CurrencyDbo createCurrency(final String code) {
		final CurrencyDbo currency = new CurrencyDbo();
		currency.setCode(code);
		return currency;
	}
	
	static ExchangeRateDbo createExchangeRate(
			final String buyRate, 
			final String sellRate, 
			final CurrencyDbo currency, 
			final BankDbo bank, 
			final LocalDateTime updateDate) {
		
		final ExchangeRateDbo erate = new ExchangeRateDbo();
		erate.setBuyRate(new BigDecimal(buyRate));
		erate.setSellRate(new BigDecimal(sellRate));
		erate.setCurrency(currency);
		erate.setBank(bank);
		erate.setUpdateDate(updateDate);
		
		return erate;
	}
}
