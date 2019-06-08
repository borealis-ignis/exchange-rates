package com.borealis.erates.supplier.bsb.parser;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.supplier.exception.RatesProcessingException;
import com.borealis.erates.util.ExchangeRatesUtil;

/**
 * @author Kastalski Sergey
 */
@Service
public class BelswissExchangeRatesParser {
	
	private static Logger logger = LoggerFactory.getLogger(BelswissExchangeRatesParser.class);
	
	private final static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm Головной офис");
	
	
	public List<ExchangeRateDto> parse(final String content, final List<CurrencyDto> currencies) throws RatesProcessingException {
		final List<ExchangeRateDto> exchangeRates = new ArrayList<>();
		
		final Document html = Jsoup.parse(content);
		final Elements sections = html.select("div.subsection div.currency-rates");
		if (sections == null) {
			return Collections.emptyList();
		}
		
		final Element section = findRatesSection(sections);
		final LocalDateTime updateDate = LocalDateTime.parse(section.selectFirst("div.title").text(), dateFormatter);
		
		final Elements ratePods = section.select("div.currency-tab tr");
		ratePods.forEach(ratePod -> {
			final Element currencyPod = ratePod.selectFirst("span.exchange-scale");
			if (currencyPod == null) {
				return;
			}
			
			final CurrencyDto currency = ExchangeRatesUtil.findCurrency(currencyPod.text(), currencies);
			if (currency == null) {
				return;
			}
			
			final Elements buySellRates = ratePod.select("td");
			if (buySellRates == null || buySellRates.size() != 2) {
				return;
			}
			
			try {
				final BigDecimal buyRate = new BigDecimal(buySellRates.get(0).text());
				final BigDecimal sellRate = new BigDecimal(buySellRates.get(1).text());
				
				final ExchangeRateDto exchangeRate = new ExchangeRateDto();
				exchangeRate.setUpdateDate(updateDate);
				exchangeRate.setCurrency(currency);
				exchangeRate.setBuyRate(ExchangeRatesUtil.recalcRate(buyRate, currency.getCode()));
				exchangeRate.setSellRate(ExchangeRatesUtil.recalcRate(sellRate, currency.getCode()));
				
				exchangeRates.add(exchangeRate);
			} catch (final NumberFormatException ex) {
				logger.error("Rates (numbers) parsing is failed", ex);
			}
		});
		
		return exchangeRates;
	}
	
	private Element findRatesSection(final Elements sections) {
		final List<Element> sectionsList = sections.stream().filter(
				section -> section.selectFirst("div.title") != null && 
				StringUtils.contains(section.selectFirst("div.title").text(), "Головной офис")
			).collect(Collectors.toList());
		
		if (sectionsList.size() == 1) {
			return sectionsList.get(0);
		}
		return null;
	}
	
}
