package com.borealis.erates.supplier.prior;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.borealis.erates.TestDataContainer;
import com.borealis.erates.TestIOUtil;
import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.supplier.Bank;
import com.borealis.erates.supplier.exception.RatesProcessingException;
import com.borealis.erates.transport.HttpResponse;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PriorBankTest {
	
	@Autowired
	private PriorBank priorBank;
	
	@Test
	public void priorBankDataTest() {
		final Bank priorBankData = (Bank) priorBank;
		
		assertThat(priorBankData.getBankCode()).isEqualTo("priorb");
		assertThat(priorBankData.getBankName()).isEqualTo("Prior Bank");
	}
	
	@Test
	public void priorBankSendRequestTest() throws RatesProcessingException {
		final HttpResponse response = priorBank.sendRequest();
		
		final Header expectedHeader = new BasicHeader("Content-Type", "json;charset=utf-8");
		
		final Header[] headers = response.getHeaders();
		boolean headerFound = false;
		for (final Header header : headers) {
			if (expectedHeader.getName().equals(header.getName())) {
				headerFound = true;
				assertThat(header.getValue()).isEqualTo(expectedHeader.getValue());
			}
		}
		assertThat(headerFound).isTrue();
		assertThat(headers).isNotEmpty();
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getBody()).isNotBlank();
	}
	
	@Test
	public void priorBankParseTest() throws RatesProcessingException {
		final List<CurrencyDto> currencies = new ArrayList<>();
		currencies.add(TestDataContainer.getUSDCurrencyDto(true));
		currencies.add(TestDataContainer.getEURCurrencyDto(true));
		
		final String content = TestIOUtil.readFile("com/borealis/erates/supplier/prior/parser/correct_response.json");
		
		final List<ExchangeRateDto> rates = priorBank.parse(content, currencies);
		
		assertThat(rates).hasSameSizeAs(currencies)
			.allMatch(exchangeRate -> currencies.contains(exchangeRate.getCurrency()), "Unacceptable Currency")
			.allMatch(exchangeRate -> exchangeRate.getBuyRate() != null, "BuyRate can't be null")
			.allMatch(exchangeRate -> exchangeRate.getSellRate() != null, "SellRate can't be null")
			.allMatch(exchangeRate -> exchangeRate.getUpdateDate() != null, "UpdateDate can't be null");
	}
}
