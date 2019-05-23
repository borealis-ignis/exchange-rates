package com.borealis.erates.supplier.belarus.parser;

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
import com.borealis.erates.parsers.ParserChecksUtil;
import com.borealis.erates.supplier.exception.RatesProcessingException;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BelarusExchangeRatesParserTest {
	
	@Autowired
	private BelarusExchangeRatesParser parser;
	
	
	@Test
	public void okBelarusParsingTest() throws RatesProcessingException {
		final List<CurrencyDto> currencies = new ArrayList<>();
		currencies.add(TestDataContainer.getUSDCurrencyDto(true));
		currencies.add(TestDataContainer.getEURCurrencyDto(true));
		currencies.add(TestDataContainer.getRUBCurrencyDto(true));
		
		final String content = TestIOUtil.readFile("com/borealis/erates/supplier/belarus/parser/correct_response.html");
		
		final List<ExchangeRateDto> rates = parser.parse(content, currencies);
		
		ParserChecksUtil.checkOkResults(rates, currencies);
	}
	
}
