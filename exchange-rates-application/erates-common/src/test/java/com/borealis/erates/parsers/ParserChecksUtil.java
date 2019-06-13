package com.borealis.erates.parsers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.model.dto.ExchangeRateDto;

/**
 * @author Kastalski Sergey
 */
public abstract class ParserChecksUtil {
	
	public static void checkOkResults(final List<ExchangeRateDto> rates, final List<CurrencyDto> currencies) {
		checkOkResults(rates, currencies, false);
	}
	
	public static void checkOkResults(final List<ExchangeRateDto> rates, final List<CurrencyDto> currencies, final boolean equalsRates) {
		assertThat(rates).hasSameSizeAs(currencies)
			.allMatch(exchangeRate -> currencies.contains(exchangeRate.getCurrency()), "Unacceptable Currency")
			.allMatch(exchangeRate -> exchangeRate.getBuyRate() != null, "BuyRate can't be null")
			.allMatch(exchangeRate -> exchangeRate.getSellRate() != null, "SellRate can't be null")
			.allMatch(exchangeRate -> exchangeRate.getUpdateDate() != null, "UpdateDate can't be null")
			.allSatisfy(rate -> {
				if (equalsRates) {
					assertThat(rate.getBuyRate()).isEqualTo(rate.getSellRate());
				} else {
					assertThat(rate.getBuyRate()).isLessThan(rate.getSellRate());
				}
			});
	}
	
}
