package com.borealis.erates.supplier.prior.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.borealis.erates.TestDataContainer;
import com.borealis.erates.TestIOUtil;
import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.supplier.exception.RatesProcessingException;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PriorExchangeRatesParserTest {
	
	@Autowired
	private PriorExchangeRatesParser parser;
	
	
	@Test
	public void okPriorParsingTest() throws RatesProcessingException {
		final List<CurrencyDto> currencies = new ArrayList<>();
		currencies.add(TestDataContainer.getUSDCurrencyDto(true));
		currencies.add(TestDataContainer.getEURCurrencyDto(true));
		
		final String content = TestIOUtil.readFile("com/borealis/erates/supplier/prior/parser/correct_response.json");
		
		final List<ExchangeRateDto> rates = parser.parse(content, currencies);
		
		assertThat(rates).hasSameSizeAs(currencies)
			.allMatch(exchangeRate -> currencies.contains(exchangeRate.getCurrency()), "Unacceptable Currency")
			.allMatch(exchangeRate -> exchangeRate.getBuyRate() != null, "BuyRate can't be null")
			.allMatch(exchangeRate -> exchangeRate.getSellRate() != null, "SellRate can't be null")
			.allMatch(exchangeRate -> exchangeRate.getUpdateDate() != null, "UpdateDate can't be null");
	}
	
	@Test()
	public void emptyRatesAfterPriorParsingTest() throws RatesProcessingException {
		final List<CurrencyDto> currencies = new ArrayList<>();
		currencies.add(TestDataContainer.getEURCurrencyDto(true));
		
		final String content = TestIOUtil.readFile("com/borealis/erates/supplier/prior/parser/response_with_bad_titles.json");
		
		final List<ExchangeRateDto> rates = parser.parse(content, currencies);
		assertThat(rates).isEmpty();
	}
	
	@Test()
	public void unknownRequestedCurrencyTest() throws RatesProcessingException {
		final List<CurrencyDto> currencies = new ArrayList<>();
		final CurrencyDto currency = TestDataContainer.getEURCurrencyDto(true);
		currency.setCode("Unknown");
		currencies.add(currency);
		
		final String content = TestIOUtil.readFile("com/borealis/erates/supplier/prior/parser/correct_response.json");
		
		final List<ExchangeRateDto> rates = parser.parse(content, currencies);
		assertThat(rates).isEmpty();
	}
	
	@Test()
	public void noSellRatesTest() throws RatesProcessingException {
		final List<CurrencyDto> currencies = new ArrayList<>();
		currencies.add(TestDataContainer.getEURCurrencyDto(true));
		
		final String content = TestIOUtil.readFile("com/borealis/erates/supplier/prior/parser/response_without_sellrates.json");
		
		final List<ExchangeRateDto> rates = parser.parse(content, currencies);
		assertThat(rates).isEmpty();
	}
	
	@Test(expected = RatesProcessingException.class)
	public void failedPriorParsingTest() throws RatesProcessingException {
		final List<CurrencyDto> currencies = new ArrayList<>();
		currencies.add(TestDataContainer.getEURCurrencyDto(true));
		
		final String content = TestIOUtil.readFile("com/borealis/erates/supplier/prior/parser/bad_response.json");
		
		parser.parse(content, currencies);
	}
	
}
