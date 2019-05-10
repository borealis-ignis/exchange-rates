package com.borealis.erates;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.borealis.erates.model.dto.BankDto;
import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.repository.model.dbo.BankDbo;
import com.borealis.erates.repository.model.dbo.CurrencyDbo;
import com.borealis.erates.repository.model.dbo.ExchangeRateDbo;

/**
 * @author Kastalski Sergey
 */
public abstract class TestDataContainer {
	
	private static LocalDateTime updateDate = LocalDateTime.now();
	
	private static Timestamp timestampDate = Timestamp.valueOf(updateDate);
	
	private static BigDecimal buyRate = new BigDecimal("2.0900");
	
	private static BigDecimal sellRate = new BigDecimal("2.1170");
	
	
	public static BankDto getBankDto(final boolean withId) {
		final BankDto dto = new BankDto();
		if (withId) {
			dto.setId(1l);
		}
		dto.setCode("priorb");
		dto.setActive(true);
		
		return dto;
	}
	
	public static BankDbo getBankDbo(final boolean withId) {
		final BankDbo dbo = new BankDbo();
		if (withId) {
			dbo.setId(1l);
		}
		dbo.setCode("priorb");
		dbo.setActive(true);
		
		return dbo;
	}
	
	public static CurrencyDto getUSDCurrencyDto(final boolean withId) {
		final CurrencyDto dto = new CurrencyDto();
		if (withId) {
			dto.setId(1l);
		}
		dto.setCode("USD");
		
		return dto;
	}
	
	public static CurrencyDto getEURCurrencyDto(final boolean withId) {
		final CurrencyDto dto = new CurrencyDto();
		if (withId) {
			dto.setId(2l);
		}
		dto.setCode("EUR");
		
		return dto;
	}
	
	public static CurrencyDto getRUBCurrencyDto(final boolean withId) {
		final CurrencyDto dto = new CurrencyDto();
		if (withId) {
			dto.setId(3l);
		}
		dto.setCode("RUB");
		
		return dto;
	}
	
	public static CurrencyDbo getCurrencyDbo(final boolean withId) {
		final CurrencyDbo dbo = new CurrencyDbo();
		if (withId) {
			dbo.setId(1l);
		}
		dbo.setCode("USD");
		
		return dbo;
	}
	
	public static ExchangeRateDto getExchangeRateDto(final boolean withId) {
		final ExchangeRateDto dto = new ExchangeRateDto();
		if (withId) {
			dto.setId(1l);
		}
		dto.setBuyRate(buyRate);
		dto.setSellRate(sellRate);
		dto.setUpdateDate(updateDate);
		dto.setBank(getBankDto(withId));
		dto.setCurrency(getUSDCurrencyDto(withId));
		
		return dto;
	}
	
	public static ExchangeRateDbo getExchangeRateDbo(final boolean withId) {
		final ExchangeRateDbo dbo = new ExchangeRateDbo();
		if (withId) {
			dbo.setId(1l);
		}
		dbo.setBuyRate(buyRate);
		dbo.setSellRate(sellRate);
		dbo.setUpdateDate(updateDate);
		dbo.setBank(getBankDbo(withId));
		dbo.setCurrency(getCurrencyDbo(withId));
		
		return dbo;
	}
	
	public static LocalDateTime getLocalDateTime() {
		return updateDate;
	}
	
	public static Timestamp getTimestamp() {
		return timestampDate;
	}
	
}
