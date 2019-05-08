package com.borealis.erates.repository.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.borealis.erates.TestDataContainer;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.repository.converter.impl.ExchangeRateConverter;
import com.borealis.erates.repository.model.dbo.ExchangeRateDbo;

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
	
	@Test
	public void exchangeRateListDtoToDboTest() {
		final ExchangeRateDto dto = TestDataContainer.getExchangeRateDto(true);
		final List<ExchangeRateDto> dtos = new ArrayList<>();
		dtos.add(dto);
		
		final ExchangeRateDbo dbo = TestDataContainer.getExchangeRateDbo(true);
		final List<ExchangeRateDbo> dbos = new ArrayList<>();
		dbos.add(dbo);
		
		dbos.forEach(d1 -> {
			converter.convertDtos(dtos).forEach(d2 -> {
				assertThat(d1).isEqualToComparingFieldByFieldRecursively(d2);
			});
		});
	}
	
	@Test
	public void exchangeRateListDboToDtoTest() {
		final ExchangeRateDbo dbo = TestDataContainer.getExchangeRateDbo(true);
		final List<ExchangeRateDbo> dbos = new ArrayList<>();
		dbos.add(dbo);
		
		final ExchangeRateDto dto = TestDataContainer.getExchangeRateDto(true);
		final List<ExchangeRateDto> dtos = new ArrayList<>();
		dtos.add(dto);
		
		dtos.forEach(t1 -> {
			converter.convertDbos(dbos).forEach(t2 -> {
				assertThat(t1).isEqualToComparingFieldByFieldRecursively(t2);
			});
		});
	}
	
}
