package com.borealis.erates.supplier.prior.parser;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
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
public class PriorExchangeRatesParser {
	
	private final static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm,dd.MM.yyyy");
	
	
	public List<ExchangeRateDto> parse(final String content, final List<CurrencyDto> currencies) throws RatesProcessingException {
		final List<ExchangeRateDto> exchangeRates = new ArrayList<>();
		
		final String xml;
		final String updateDateString;
		try {
			final JSONObject jsonContent = new JSONObject(content);
			xml = jsonContent.getString("resultEBank");
			updateDateString = jsonContent.getString("datetimeEBank");
		} catch (final JSONException e) {
			throw new RatesProcessingException("Prior json was parsed unsuccessfully", e);
		}
		
		final LocalDateTime updateDate = LocalDateTime.parse(updateDateString, dateFormatter);
		
		final Document html = Jsoup.parse(xml);
		
		html.select("div.homeModuleRow").forEach(ratesContainer -> {
			final Element title = ratesContainer.selectFirst("h2.title");
			if (title != null && "Покупка/продажа".equals(title.text())) {
				final Elements rateColumns = ratesContainer.select("div.homeModuleColumn");
				
				currencies.forEach(currency -> {
					final int rateIndex = findIndex(rateColumns.first(), currency.getCode());
					if (rateIndex == -1) {
						return;
					}
					
					BigDecimal buyRate = null;
					BigDecimal sellRate = null;
					for (int i = 1; i < rateColumns.size(); i++) {
						final Elements columnRows = rateColumns.get(i).select("p span.value");
						final String rateType = columnRows.first().text();
						final String rate = columnRows.get(rateIndex).text();
						
						if ("Покупка".equals(rateType)) {
							buyRate = new BigDecimal(rate);
						} else if ("Продажа".equals(rateType)) {
							sellRate = new BigDecimal(rate);
						}
					}
					
					if (buyRate == null || sellRate == null) {
						return;
					}
					
					final ExchangeRateDto exchangeRate = new ExchangeRateDto();
					exchangeRate.setBuyRate(ExchangeRatesUtil.recalcRate(buyRate, currency.getCode()));
					exchangeRate.setSellRate(ExchangeRatesUtil.recalcRate(sellRate, currency.getCode()));
					exchangeRate.setCurrency(currency);
					exchangeRate.setUpdateDate(updateDate);
					
					exchangeRates.add(exchangeRate);
				});
			}
		});
		
		return exchangeRates;
	}
	
	private int findIndex(final Element titlesColumn, final String currencyCode) {
		final Elements titles = titlesColumn.select("p span.value");
		if (titles == null || titles.isEmpty()) {
			return -1;
		}
		
		for (int index = 0; index < titles.size(); index++) {
			final String title = titles.get(index).ownText().trim();
			if (currencyCode.equals(title)) {
				return index;
			}
		}
		
		return -1;
	}
	
}
