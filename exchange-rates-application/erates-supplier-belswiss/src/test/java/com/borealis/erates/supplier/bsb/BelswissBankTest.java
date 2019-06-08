package com.borealis.erates.supplier.bsb;

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
import com.borealis.erates.supplier.bsb.BelswissBank;
import com.borealis.erates.supplier.exception.RatesProcessingException;
import com.borealis.erates.transport.HttpResponse;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestExchangeRatesApplication.class)
@ActiveProfiles("test")
public class BelswissBankTest {
	
	@Autowired
	private BelswissBank bsbBank;
	
	@Test
	public void bsbBankDataTest() {
		final Bank bsbBankData = (Bank) bsbBank;
		
		assertThat(bsbBankData.getBankCode()).isEqualTo("bsb");
		assertThat(bsbBankData.getBankName()).isEqualTo("BelSwiss Bank");
	}
	
	@Test
	public void bsbBankSendRequestTest() throws RatesProcessingException {
		final HttpResponse response = bsbBank.sendRequest();
		
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getBody()).isNotBlank();
	}
	
	@Test
	public void bsbBankParseTest() throws RatesProcessingException {
		final List<CurrencyDto> currencies = new ArrayList<>();
		currencies.add(TestDataContainer.getUSDCurrencyDto(true));
		currencies.add(TestDataContainer.getEURCurrencyDto(true));
		
		final String content = TestIOUtil.readFile("com/borealis/erates/supplier/bsb/parser/correct_response.html");
		
		final List<ExchangeRateDto> rates = bsbBank.parse(content, currencies);
		
		ParserChecksUtil.checkOkResults(rates, currencies);
	}
	
}
