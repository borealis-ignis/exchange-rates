package com.borealis.erates.supplier.nbrb.parser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.supplier.exception.RatesProcessingException;
import com.borealis.erates.util.DateService;
import com.borealis.erates.util.ExchangeRatesUtil;

/**
 * @author Kastalski Sergey
 */
@Service
public class NbrbExchangeRatesParser {
	
	private final static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
	
	private DateService dateService;
	
	public NbrbExchangeRatesParser(final DateService dateService) {
		this.dateService = dateService;
	}
	
	
	public List<ExchangeRateDto> parse(final String content, final List<CurrencyDto> currencies) throws RatesProcessingException {
		final List<ExchangeRateDto> exchangeRates = new ArrayList<>();
		
		final Document html = Jsoup.parse(content);
		
		final Elements sections = html.select("div.stats-content dl.js-stats-item");
		if (sections == null) {
			return Collections.emptyList();
		}
		
		final Element exchangeRatesSection = findExchangeRatesPod(sections);
		if (exchangeRatesSection == null) {
			return Collections.emptyList();
		}
		
		final Elements dataElements = exchangeRatesSection.select("dd table tr");
		if (dataElements.size() < 2) {
			return Collections.emptyList();
		}
		
		final int ratesPodIndex = findIndex(dataElements.get(0).select("th"));
		if (ratesPodIndex < 0) {
			return Collections.emptyList();
		}
		
		final LocalDateTime updateDate = dateService.currentLocalDateTime();
		
		currencies.forEach(currency -> {
			final Element ratesPod = findRatesPodByCurrency(dataElements, currency);
			if (ratesPod == null) {
				return;
			}
			
			final Elements ratesElements = ratesPod.select("td");
			if ((ratesPodIndex + 1) > ratesElements.size()) {
				return;
			}
			
			final Element rateElement = ratesElements.get(ratesPodIndex);
			if (rateElement == null) {
				return;
			}
			
			final String rateString = StringUtils.trim(StringUtils.replace(rateElement.text(), ",", "."));
			final BigDecimal exchangeRateNumber = ExchangeRatesUtil.recalcRate(new BigDecimal(rateString), currency.getCode());
			
			final ExchangeRateDto exchangeRate = new ExchangeRateDto();
			exchangeRate.setUpdateDate(updateDate);
			exchangeRate.setCurrency(currency);
			exchangeRate.setBuyRate(exchangeRateNumber);
			exchangeRate.setSellRate(exchangeRateNumber);
			
			exchangeRates.add(exchangeRate);
		});
		
		return exchangeRates;
	}
	
	private Element findRatesPodByCurrency(final Elements dataElements, final CurrencyDto currency) {
		for (int index = 1; index < dataElements.size(); index++) {
			final Element currencyPod = dataElements.get(index);
			final Element currencyCaption = currencyPod.selectFirst("td");
			if (currencyCaption == null) {
				continue;
			}
			final String captionString = currencyCaption.text();
			if (captionString != null && captionString.contains(currency.getCode())) {
				return currencyPod;
			}
		}
		return null;
	}
	
	private Element findExchangeRatesPod(final Elements sections) {
		for (final Element section : sections) {
			if ("Курсы валют".equals(StringUtils.trim(section.select("dt a").text()))) {
				return section;
			}
		}
		return null;
	}
	
	private int findIndex(final Elements datesPod) {
		final LocalDate today = dateService.currentLocalDate();
		
		for (int index = 0; index < datesPod.size(); index++) {
			final Element datePod = datesPod.get(index);
			final String dateString = StringUtils.trim(datePod.text());
			if (StringUtils.isBlank(dateString)) {
				continue;
			}
			
			final LocalDate currentDate = LocalDate.parse(dateString, dateFormatter);
			if (today.isEqual(currentDate)) {
				return index;
			}
		}
		return -1;
	}
	
}
