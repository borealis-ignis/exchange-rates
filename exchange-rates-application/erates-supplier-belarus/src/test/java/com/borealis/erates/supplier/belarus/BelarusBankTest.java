package com.borealis.erates.supplier.belarus;

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
import com.borealis.erates.parsers.ParserChecksUtil;
import com.borealis.erates.supplier.Bank;
import com.borealis.erates.supplier.exception.RatesProcessingException;
import com.borealis.erates.transport.HttpResponse;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BelarusBankTest {
	
	@Autowired
	private BelarusBank belarusBank;
	
	@Test
	public void belarusBankDataTest() {
		final Bank belarusBankData = (Bank) belarusBank;
		
		assertThat(belarusBankData.getBankCode()).isEqualTo("belarusb");
		assertThat(belarusBankData.getBankName()).isEqualTo("Belarus Bank");
	}
	
	@Test
	public void belarusBankSendRequestTest() throws RatesProcessingException {
		final HttpResponse response = belarusBank.sendRequest();
		
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getBody()).isNotBlank();
	}
	
	@Test
	public void belarusBankParseTest() throws RatesProcessingException {
		final List<CurrencyDto> currencies = new ArrayList<>();
		currencies.add(TestDataContainer.getUSDCurrencyDto(true));
		currencies.add(TestDataContainer.getEURCurrencyDto(true));
		
		final String content = TestIOUtil.readFile("com/borealis/erates/supplier/belarus/parser/correct_response.html");
		
		final List<ExchangeRateDto> rates = belarusBank.parse(content, currencies);
		
		ParserChecksUtil.checkOkResults(rates, currencies);
	}
	
}
