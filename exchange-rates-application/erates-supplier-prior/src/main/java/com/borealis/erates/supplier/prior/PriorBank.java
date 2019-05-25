package com.borealis.erates.supplier.prior;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.supplier.Bank;
import com.borealis.erates.supplier.AbstractBankProcessor;
import com.borealis.erates.supplier.exception.RatesProcessingException;
import com.borealis.erates.supplier.prior.parser.PriorExchangeRatesParser;
import com.borealis.erates.transport.HttpResponse;
import com.borealis.erates.transport.HttpTransport;

/**
 * @author Kastalski Sergey
 */
@Component
public class PriorBank extends AbstractBankProcessor implements Bank {
	
	private String name;
	
	private String code;
	
	private String url;
	
	private HttpTransport transport;
	
	private PriorExchangeRatesParser parser;
	
	
	public PriorBank(
			@Value("${bank.prior.name:}") final String bankName,
			@Value("${bank.prior.code:}") final String bankCode,
			@Value("${bank.prior.rates.url:}") final String url,
			final HttpTransport transport,
			final PriorExchangeRatesParser parser) {
		this.name = bankName;
		this.code = bankCode;
		this.url = url;
		this.transport = transport;
		this.parser = parser;
	}
	
	@Override
	public String getBankName() {
		return name;
	}
	
	@Override
	public String getBankCode() {
		return code;
	}
	
	@Override
	protected HttpResponse sendRequest() throws RatesProcessingException {
		return transport.sendPost(url);
	}

	@Override
	protected List<ExchangeRateDto> parse(final String content, final List<CurrencyDto> currencies) throws RatesProcessingException {
		return parser.parse(content, currencies);
	}
	
}
