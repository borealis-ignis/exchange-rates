package com.borealis.erates.transport;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.borealis.erates.TestExchangeRatesApplication;
import com.borealis.erates.supplier.exception.RatesProcessingException;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestExchangeRatesApplication.class)
@ActiveProfiles("test")
public class HttpTransportTest {
	
	@Autowired
	private HttpTransport httpTransport;
	
	@Test
	public void sendPostRequestTest() throws RatesProcessingException {
		final HttpResponse httpResponse = httpTransport.sendGet("http://google.com");
		
		assertThat(httpResponse.getStatus()).isEqualTo(200);
		assertThat(httpResponse.getBody()).isNotBlank();
		assertThat(httpResponse.getHeaders()).isNotEmpty();
	}
	
	@Test
	public void sendGetRequestTest() throws RatesProcessingException {
		final HttpResponse httpResponse = httpTransport.sendPost("https://www.priorbank.by/main?p_p_id=ExchangeRates_INSTANCE_ExchangeRatesCalculatorView&p_p_lifecycle=2&p_p_state=normal&p_p_mode=view&p_p_resource_id=ajaxMainPageRatesGetRates&p_p_cacheability=cacheLevelPage");
		
		assertThat(httpResponse.getStatus()).isEqualTo(200);
		assertThat(httpResponse.getBody()).isNotBlank();
		assertThat(httpResponse.getHeaders()).isNotEmpty();
	}
	
}
