package com.borealis.erates.supplier.belapb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.supplier.AbstractBankProcessor;
import com.borealis.erates.supplier.Bank;
import com.borealis.erates.supplier.belapb.parser.BelapbExchangeRatesParser;
import com.borealis.erates.supplier.exception.RatesProcessingException;
import com.borealis.erates.transport.HttpResponse;
import com.borealis.erates.transport.HttpTransport;

import javafx.util.Pair;

/**
 * @author Kastalski Sergey
 */
@Component
public class BelapbBank extends AbstractBankProcessor implements Bank {
	
	private String name;
	
	private String code;
	
	private String usdUrl;
	
	private String eurUrl;
	
	private String rubUrl;
	
	private HttpTransport transport;
	
	private BelapbExchangeRatesParser parser;
	
	private final static String dayToken = "{d}";
	
	private final static String monthToken = "{m}";
	
	private final static String yearToken = "{y}";
	
	public BelapbBank(
			@Value("${bank.belapb.name:}") final String bankName,
			@Value("${bank.belapb.code:}") final String bankCode,
			@Value("${bank.belapb.usd.rates.url:}") final String usdUrl,
			@Value("${bank.belapb.eur.rates.url:}") final String eurUrl,
			@Value("${bank.belapb.rub.rates.url:}") final String rubUrl,
			final HttpTransport transport,
			final BelapbExchangeRatesParser parser) {
		this.name = bankName;
		this.code = bankCode;
		this.usdUrl = usdUrl;
		this.eurUrl = eurUrl;
		this.rubUrl = rubUrl;
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
		final LocalDate today = LocalDate.now();
		
		final List<Pair<String, HttpResponse>> responses = new ArrayList<>();
		responses.add(new Pair<String, HttpResponse>("USD", transport.sendGet(prepareUrl(usdUrl, today))));
		responses.add(new Pair<String, HttpResponse>("EUR", transport.sendGet(prepareUrl(eurUrl, today))));
		responses.add(new Pair<String, HttpResponse>("RUB", transport.sendGet(prepareUrl(rubUrl, today))));
		
		return prepareResponse(responses);
	}
	
	private String prepareUrl(final String url, final LocalDate today) {
		return StringUtils.replaceEach(url,
				new String[] { dayToken, monthToken, yearToken },
				new String[] { Integer.toString(today.getDayOfMonth()), Integer.toString(today.getMonth().getValue()), Integer.toString(today.getYear()) });
	}
	
	private HttpResponse prepareResponse(final List<Pair<String, HttpResponse>> responses) throws RatesProcessingException {
		if (responses == null || responses.size() == 0) {
			throw new RatesProcessingException("Responses list for Belapb bank is empty");
		}
		
		HttpResponse resultResponse = null;
		
		final StringBuilder body = new StringBuilder();
		for (final Pair<String, HttpResponse> responsePair : responses) {
			final HttpResponse httpResponse = responsePair.getValue();
			if (httpResponse.getStatus() == 200) {
				if (resultResponse == null) {
					resultResponse = httpResponse;
				}
				
				body.append("<item class=\"");
				body.append(responsePair.getKey());
				body.append("\">");
				body.append(httpResponse.getBody());
				body.append("</item>");
			} else {
				throw new RatesProcessingException("Getting data for Belapb bank is failed (smth wrong with sending requests)");
			}
		}
		
		resultResponse.setBody(body.toString());
		
		return resultResponse;
	}
	
	@Override
	protected List<ExchangeRateDto> parse(final String content, final List<CurrencyDto> currencies) throws RatesProcessingException {
		return parser.parse(content, currencies);
	}
	
}
