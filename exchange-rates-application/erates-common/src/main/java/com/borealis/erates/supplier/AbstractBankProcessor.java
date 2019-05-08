package com.borealis.erates.supplier;

import java.util.List;

import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.supplier.exception.RatesProcessingException;

/**
 * @author Kastalski Sergey
 */
public abstract class AbstractBankProcessor {
	
	protected abstract String sendRequest() throws RatesProcessingException;
	
	protected abstract List<ExchangeRateDto> parse(final String content, final List<CurrencyDto> currencyCodes) throws RatesProcessingException;
	
	public abstract String getBankCode();
	
	public List<ExchangeRateDto> process(final List<CurrencyDto> currencyCodes) throws RatesProcessingException {
		final String content;
		try {
			content = sendRequest();
		} catch (final RatesProcessingException e) {
			throw e;
		} catch (final Exception e) {
			throw new RatesProcessingException("Failed Transport processing", e);
		}
		
		try {
			return parse(content, currencyCodes);
		} catch (final RatesProcessingException e) {
			throw e;
		} catch (final Exception e) {
			throw new RatesProcessingException("Failed Parsing", e);
		}
	}
	
}
