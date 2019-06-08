package com.borealis.erates.supplier.bps.parser;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
public class BpsExchangeRatesParser {
	
	private static Logger logger = LoggerFactory.getLogger(BpsExchangeRatesParser.class);
	
	
	public List<ExchangeRateDto> parse(final String content, final List<CurrencyDto> currencies) throws RatesProcessingException {
		final List<ExchangeRateDto> exchangeRates = new ArrayList<>();
		
		try {
			final JSONObject json = new JSONObject(content);
			
			final long timestamp = json.getLong("date");
			
			final LocalDateTime updateDate = new Timestamp(timestamp).toLocalDateTime();
			
			final JSONObject ratesPodJson = json.getJSONObject("rates");
			if (ratesPodJson == null) {
				return Collections.emptyList();
			}
			
			final JSONArray ratesJsonArray = ratesPodJson.getJSONArray("list");
			for (final Object rateItemObject : ratesJsonArray) {
				final JSONObject rateItem = (JSONObject) rateItemObject;
				
				final CurrencyDto currency = ExchangeRatesUtil.findCurrency(rateItem.getString("iso"), currencies);
				if (currency == null) {
					break;
				}
				
				final BigDecimal buyRate = rateItem.getBigDecimal("buy");
				final BigDecimal sellRate = rateItem.getBigDecimal("sale");
				if (buyRate == null || sellRate == null) {
					break;
				}
				
				final ExchangeRateDto exchangeRate = new ExchangeRateDto();
				exchangeRate.setBuyRate(ExchangeRatesUtil.recalcRate(buyRate, currency.getCode()));
				exchangeRate.setSellRate(ExchangeRatesUtil.recalcRate(sellRate, currency.getCode()));
				exchangeRate.setCurrency(currency);
				exchangeRate.setUpdateDate(updateDate);
				
				exchangeRates.add(exchangeRate);
			}
		} catch (final JSONException ex) {
			logger.error("BPS Bank parsing is failed", ex);
			return Collections.emptyList();
		}
		
		return exchangeRates;
	}
	
}
