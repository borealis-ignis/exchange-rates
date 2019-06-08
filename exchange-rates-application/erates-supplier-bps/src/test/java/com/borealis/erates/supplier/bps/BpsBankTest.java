package com.borealis.erates.supplier.bps;

import static org.assertj.core.api.Assertions.assertThat;

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
import com.borealis.erates.supplier.Bank;
import com.borealis.erates.supplier.bps.BpsBank;
import com.borealis.erates.supplier.exception.RatesProcessingException;
import com.borealis.erates.transport.HttpResponse;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestExchangeRatesApplication.class)
@ActiveProfiles("test")
public class BpsBankTest {
	
	@Autowired
	private BpsBank bpsBank;
	
	@Test
	public void bpsBankDataTest() {
		final Bank bpsBankData = (Bank) bpsBank;
		
		assertThat(bpsBankData.getBankCode()).isEqualTo("bps");
		assertThat(bpsBankData.getBankName()).isEqualTo("Bps-Sberbank");
	}
	
	@Test
	public void bpsBankSendRequestTest() throws RatesProcessingException {
		final HttpResponse response = bpsBank.sendRequest();
		
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getBody()).isNotBlank();
	}
	
	@Test
	public void bpsBankParseTest() throws RatesProcessingException {
		final List<CurrencyDto> currencies = new ArrayList<>();
		currencies.add(TestDataContainer.getUSDCurrencyDto(true));
		currencies.add(TestDataContainer.getEURCurrencyDto(true));
		
		final String content = TestIOUtil.readFile("com/borealis/erates/supplier/bps/parser/correct_response.json");
		
		final List<ExchangeRateDto> rates = bpsBank.parse(content, currencies);
		
		ParserChecksUtil.checkOkResults(rates, currencies);
	}
	
}
