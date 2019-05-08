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
import com.borealis.erates.model.dto.BankDto;
import com.borealis.erates.repository.converter.impl.BankConverter;
import com.borealis.erates.repository.model.dbo.BankDbo;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BankConverterTest {
	
	@Autowired
	private BankConverter converter;
	
	@Test
	public void bankDtoToDboTest() {
		final BankDto dto = TestDataContainer.getBankDto(true);
		
		assertThat(TestDataContainer.getBankDbo(true)).isEqualToComparingFieldByField(converter.convertDto(dto));
	}
	
	@Test
	public void bankDboToDtoTest() {
		final BankDbo dbo = TestDataContainer.getBankDbo(true);
		
		assertThat(TestDataContainer.getBankDto(true)).isEqualToComparingFieldByField(converter.convertDbo(dbo));
	}
	
	@Test
	public void bankListDtoToDboTest() {
		final BankDto dto = TestDataContainer.getBankDto(true);
		final List<BankDto> dtos = new ArrayList<>();
		dtos.add(dto);
		
		final BankDbo dbo = TestDataContainer.getBankDbo(true);
		final List<BankDbo> dbos = new ArrayList<>();
		dbos.add(dbo);
		
		dbos.forEach(d1 -> {
			converter.convertDtos(dtos).forEach(d2 -> {
				assertThat(d1).isEqualToComparingFieldByField(d2);
			});
		});
	}
	
	@Test
	public void bankListDboToDtoTest() {
		final BankDbo dbo = TestDataContainer.getBankDbo(true);
		final List<BankDbo> dbos = new ArrayList<>();
		dbos.add(dbo);
		
		final BankDto dto = TestDataContainer.getBankDto(true);
		final List<BankDto> dtos = new ArrayList<>();
		dtos.add(dto);
		
		dtos.forEach(t1 -> {
			converter.convertDbos(dbos).forEach(t2 -> {
				assertThat(t1).isEqualToComparingFieldByField(t2);
			});
		});
	}
}
