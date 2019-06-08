package com.borealis.erates.supplier.bps.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.borealis.erates.TestDataContainer;
import com.borealis.erates.TestExchangeRatesApplication;
import com.borealis.erates.TestIOUtil;
import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.parsers.ParserChecksUtil;
import com.borealis.erates.supplier.bps.parser.BpsExchangeRatesParser;
import com.borealis.erates.supplier.exception.RatesProcessingException;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestExchangeRatesApplication.class)
@ActiveProfiles("test")
public class BpsExchangeRatesParserTest {
	
	@Autowired
	private BpsExchangeRatesParser parser;
	
	
	@Test
	public void okBpsParsingTest() throws RatesProcessingException {
		final List<CurrencyDto> currencies = new ArrayList<>();
		currencies.add(TestDataContainer.getUSDCurrencyDto(true));
		currencies.add(TestDataContainer.getEURCurrencyDto(true));
		currencies.add(TestDataContainer.getRUBCurrencyDto(true));
		
		final String content = TestIOUtil.readFile("com/borealis/erates/supplier/bps/parser/correct_response.json");
		
		final List<ExchangeRateDto> rates = parser.parse(content, currencies);
		
		ParserChecksUtil.checkOkResults(rates, currencies);
	}
	
}
