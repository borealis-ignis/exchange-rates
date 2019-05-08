package com.borealis.erates.supplier.prior;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.repository.BanksDAO;
import com.borealis.erates.repository.converter.impl.BankConverter;
import com.borealis.erates.supplier.Bank;
import com.borealis.erates.supplier.HttpTransport;
import com.borealis.erates.supplier.AbstractBankProcessor;
import com.borealis.erates.supplier.exception.RatesProcessingException;
import com.borealis.erates.supplier.prior.parser.PriorExchangeRatesParser;

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
			@Value("${bank.prior.name}") final String bankName,
			@Value("${bank.prior.code}") final String bankCode,
			@Value("${bank.prior.rates.url}") final String url,
			final HttpTransport transport,
			final PriorExchangeRatesParser parser,
			final BanksDAO banksDAO,
			final BankConverter bankConverter) {
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
	protected String sendRequest() throws RatesProcessingException {
		return transport.sendPost(url);
	}

	@Override
	protected List<ExchangeRateDto> parse(final String content, final List<CurrencyDto> currencyCodes) throws RatesProcessingException {
		return parser.parse(content, currencyCodes);
	}
	
}
