package com.borealis.erates.repository.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.borealis.erates.TestDataContainer;
import com.borealis.erates.repository.converter.impl.BankConverter;
import com.borealis.erates.repository.model.dbo.BankDbo;
import com.borealis.erates.repository.model.dto.BankDto;

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
	
}
