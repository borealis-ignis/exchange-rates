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
import com.borealis.erates.repository.converter.impl.CurrencyConverter;
import com.borealis.erates.repository.model.dbo.CurrencyDbo;
import com.borealis.erates.repository.model.dto.CurrencyDto;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CurrencyConverterTest {
	
	@Autowired
	private CurrencyConverter converter;
	
	@Test
	public void currencyDtoToDboTest() {
		final CurrencyDto dto = TestDataContainer.getCurrencyDto(true);
		
		assertThat(TestDataContainer.getCurrencyDbo(true)).isEqualToComparingFieldByField(converter.convertDto(dto));
	}
	
	@Test
	public void currencyDboToDtoTest() {
		final CurrencyDbo dbo = TestDataContainer.getCurrencyDbo(true);
		
		assertThat(TestDataContainer.getCurrencyDto(true)).isEqualToComparingFieldByField(converter.convertDbo(dbo));
	}
	
	@Test
	public void currencyListDtoToDboTest() {
		final CurrencyDto dto = TestDataContainer.getCurrencyDto(true);
		final List<CurrencyDto> dtos = new ArrayList<>();
		dtos.add(dto);
		
		final CurrencyDbo dbo = TestDataContainer.getCurrencyDbo(true);
		final List<CurrencyDbo> dbos = new ArrayList<>();
		dbos.add(dbo);
		
		dbos.forEach(d1 -> {
			converter.convertDtos(dtos).forEach(d2 -> {
				assertThat(d1).isEqualToComparingFieldByField(d2);
			});
		});
	}
	
	@Test
	public void currencyListDboToDtoTest() {
		final CurrencyDbo dbo = TestDataContainer.getCurrencyDbo(true);
		final List<CurrencyDbo> dbos = new ArrayList<>();
		dbos.add(dbo);
		
		final CurrencyDto dto = TestDataContainer.getCurrencyDto(true);
		final List<CurrencyDto> dtos = new ArrayList<>();
		dtos.add(dto);
		
		dtos.forEach(t1 -> {
			converter.convertDbos(dbos).forEach(t2 -> {
				assertThat(t1).isEqualToComparingFieldByField(t2);
			});
		});
	}
	
}