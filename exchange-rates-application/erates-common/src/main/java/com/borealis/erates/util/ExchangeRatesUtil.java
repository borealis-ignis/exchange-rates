package com.borealis.erates.util;

import java.math.BigDecimal;

/**
 * @author Kastalski Sergey
 */
public abstract class ExchangeRatesUtil {
	
	public static BigDecimal recalcRate(final BigDecimal rate, final String currencyCode) {
		if ("RUB".equals(currencyCode)) {
			return rate.divide(BigDecimal.valueOf(100));
		}
		return rate;
	}
	
}
