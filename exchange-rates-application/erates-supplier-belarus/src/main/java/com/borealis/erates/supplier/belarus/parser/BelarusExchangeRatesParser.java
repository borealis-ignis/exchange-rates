package com.borealis.erates.supplier.belarus.parser;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.supplier.exception.RatesProcessingException;
import com.borealis.erates.util.ExchangeRatesUtil;

/**
 * @author Kastalski Sergey
 */
@Service
public class BelarusExchangeRatesParser {
	
	private final static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy на HH:mm");
	
	
	public List<ExchangeRateDto> parse(final String content, final List<CurrencyDto> currencies) throws RatesProcessingException {
		final List<ExchangeRateDto> exchangeRates = new ArrayList<>();
		
		final Document html = Jsoup.parse(content);
		html.select("section.local-page-section").forEach(section -> {
			final Element title = section.selectFirst("h3.local-header__title a");
			if (title != null && "Курсы валют".equals(title.text())) {
				final Element timePod = section.selectFirst("header div.local-header__aside");
				
				section.select("div.local-page-section__body table.currency-table tr").forEach(currencyPod -> {
					final Element currencyElem = currencyPod.selectFirst("td.currency-table__cell-curr strong");
					if (currencyElem == null) {
						return;
					}
					
					final CurrencyDto currency = determineCurrency(currencies, currencyElem.text());
					if (currency == null) {
						return;
					}
					
					final List<Element> exchangeRateElems = currencyPod.select("td.currency-table__cell-value");
					if (exchangeRateElems.size() != 2) {
						return;
					}
					
					final BigDecimal buyRate = new BigDecimal(exchangeRateElems.get(0).text().trim());
					final BigDecimal sellRate = new BigDecimal(exchangeRateElems.get(1).text().trim());
					
					final ExchangeRateDto exchangeRate = new ExchangeRateDto();
					exchangeRate.setBuyRate(ExchangeRatesUtil.recalcRate(buyRate, currency.getCode()));
					exchangeRate.setSellRate(ExchangeRatesUtil.recalcRate(sellRate, currency.getCode()));
					exchangeRate.setCurrency(currency);
					exchangeRate.setUpdateDate(getUpdateDate(timePod));
					
					exchangeRates.add(exchangeRate);
				});
			}
		});
		
		return exchangeRates;
	}
	
	private CurrencyDto determineCurrency(final List<CurrencyDto> currencies, final String currencyLine) {
		String code = null;
		if (StringUtils.contains(currencyLine, "доллар")) {
			code = "USD";
		} else if (StringUtils.contains(currencyLine, "евро")) {
			code = "EUR";
		} else if (StringUtils.contains(currencyLine, "российских")) {
			code = "RUB";
		} else {
			return null;
		}
		
		for (final CurrencyDto currencyDto : currencies) {
			if (code.equals(currencyDto.getCode())) {
				return currencyDto;
			}
		}
		return null;
	}
	
	private LocalDateTime getUpdateDate(final Element timePod) {
		if (timePod != null) {
			final String updateDateString = StringUtils.trim(StringUtils.substringBefore(timePod.html(), "<br>"));
			return LocalDateTime.parse(updateDateString, dateFormatter);
		}
		return null;
	}
	
}
