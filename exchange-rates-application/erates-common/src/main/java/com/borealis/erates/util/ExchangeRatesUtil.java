package com.borealis.erates.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.borealis.erates.model.dto.CurrencyDto;

/**
 * @author Kastalski Sergey
 */
public abstract class ExchangeRatesUtil {
	
	public static BigDecimal recalcRate(final BigDecimal rate, final String currencyCode) {
		if ("RUB".equals(currencyCode)) {
			return rate.divide(BigDecimal.valueOf(100)).setScale(4, RoundingMode.DOWN);
		}
		return rate.setScale(4, RoundingMode.DOWN);
	}
	
	public static CurrencyDto findCurrency(final String currencyCode, final List<CurrencyDto> currencies) {
		if (StringUtils.isBlank(currencyCode)) {
			return null;
		}
		
		for (final CurrencyDto currency : currencies) {
			if (currencyCode.equals(currency.getCode())) {
				return currency;
			}
		}
		return null;
	}
	
}
