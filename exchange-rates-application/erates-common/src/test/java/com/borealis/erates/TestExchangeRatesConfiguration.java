package com.borealis.erates;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.supplier.AbstractBankProcessor;
import com.borealis.erates.supplier.Bank;
import com.borealis.erates.supplier.exception.RatesProcessingException;
import com.borealis.erates.transport.HttpResponse;

/**
 * @author Kastalski Sergey
 */
@Profile("test")
@Configuration
public class TestExchangeRatesConfiguration {
	
	@Bean
	public Bank testPriorBank() {
		return new Bank() {
			
			@Override
			public String getBankName() {
				return "Test Prior Bank";
			}
			
			@Override
			public String getBankCode() {
				return "priorb";
			}
		};
	}
	
	@Bean
	public TestBankProcessor testProcessor() {
		return new TestBankProcessor();
	}
	
	public class TestBankProcessor extends AbstractBankProcessor {
		
		private boolean okSendRequest = true;
		
		private List<ExchangeRateDto> rates;
		
		@Override
		protected HttpResponse sendRequest() throws RatesProcessingException {
			if (!okSendRequest) {
				throw new RatesProcessingException("Test exception");
			}
			final HttpResponse httpResponse = new HttpResponse();
			httpResponse.setBody("body");
			httpResponse.setStatus(200);
			httpResponse.setHeaders(new Header[] { new BasicHeader("Content-Type", "text/html;charset=UTF-8") });
			return httpResponse;
		}
		
		@Override
		protected List<ExchangeRateDto> parse(final String content, final List<CurrencyDto> currencies) throws RatesProcessingException {
			return rates;
		}
		
		@Override
		public String getBankCode() {
			return "testb";
		}
		
		
		public void setOkSendRequest(final boolean okSendRequest) {
			this.okSendRequest = okSendRequest;
		}
		
		public void setOutputRates(final List<ExchangeRateDto> rates) {
			this.rates = rates;
		}
	}
}
