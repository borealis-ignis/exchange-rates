package com.borealis.erates.supplier.belapb.parser;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.supplier.exception.RatesProcessingException;
import com.borealis.erates.util.ExchangeRatesUtil;

/**
 * @author Kastalski Sergey
 */
@Service
public class BelapbExchangeRatesParser {
	
	private final static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
	
	
	public List<ExchangeRateDto> parse(final String content, final List<CurrencyDto> currencies) throws RatesProcessingException {
		final List<ExchangeRateDto> exchangeRates = new ArrayList<>();
		
		final Document html = Jsoup.parse(content);
		
		currencies.forEach(currency -> {
			final Element ratesPod = html.selectFirst("item." + currency.getCode() + " table.table3cols table.table-tarif");
			if (ratesPod == null) {
				return;
			}
			
			final Elements dataItems = ratesPod.select("tr");
			if (dataItems == null || dataItems.size() < 4) {
				return;
			}
			
			final Element dateItem = dataItems.get(0);
			final Element dateTimeElement = dateItem.selectFirst("td strong");
			if (dateTimeElement == null) {
				return;
			}
			final LocalDateTime updateDate = LocalDateTime.parse(dateTimeElement.text(), dateFormatter);
			
			final Element ratesItem = dataItems.get(3);
			final Elements ratesElements = ratesItem.select("td");
			if (ratesElements == null || ratesElements.size() < 2) {
				return;
			}
			
			final BigDecimal buyRate = new BigDecimal(ratesElements.get(0).text().trim());
			final BigDecimal sellRate = new BigDecimal(ratesElements.get(1).text().trim());
			
			final ExchangeRateDto exchangeRate = new ExchangeRateDto();
			exchangeRate.setBuyRate(ExchangeRatesUtil.recalcRate(buyRate, currency.getCode()));
			exchangeRate.setSellRate(ExchangeRatesUtil.recalcRate(sellRate, currency.getCode()));
			exchangeRate.setCurrency(currency);
			exchangeRate.setUpdateDate(updateDate);
			
			exchangeRates.add(exchangeRate);
		});
		
		return exchangeRates;
	}
	
}
