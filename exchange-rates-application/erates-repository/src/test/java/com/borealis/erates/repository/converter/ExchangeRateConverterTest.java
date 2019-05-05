package com.borealis.erates.repository.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.borealis.erates.TestDataContainer;
import com.borealis.erates.repository.converter.impl.ExchangeRateConverter;
import com.borealis.erates.repository.model.dbo.ExchangeRateDbo;
import com.borealis.erates.repository.model.dto.ExchangeRateDto;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExchangeRateConverterTest {
	
	@Autowired
	private ExchangeRateConverter converter;
	
	@Test
	public void exchangeRateDtoToDboTest() {
		final ExchangeRateDto dto = TestDataContainer.getExchangeRateDto(true);
		
		assertThat(TestDataContainer.getExchangeRateDbo(true)).isEqualToComparingFieldByFieldRecursively(converter.convertDto(dto));
	}
	
	@Test
	public void exchangeRateDboToDtoTest() {
		final ExchangeRateDbo dbo = TestDataContainer.getExchangeRateDbo(true);
		
		assertThat(TestDataContainer.getExchangeRateDto(true)).isEqualToComparingFieldByFieldRecursively(converter.convertDbo(dbo));
	}
	
}
