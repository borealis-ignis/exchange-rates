package com.borealis.erates.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.borealis.erates.TestDataContainer;
import com.borealis.erates.repository.BanksDAO;
import com.borealis.erates.repository.model.dbo.BankDbo;
import com.borealis.erates.supplier.Bank;


/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExchangeRatesServiceTest {
	
	@Autowired
	private ExchangeRatesService service;
	
	@Autowired
	private Bank priorBank;
	
	@MockBean
	private BanksDAO banksDAO;
	
	
	@Test
	public void getBanksTest() {
		final List<BankDbo> dboBanks = new ArrayList<>();
		dboBanks.add(TestDataContainer.getBankDbo(true));
		
		when(banksDAO.findAll()).thenReturn(dboBanks);
		
		final List<Bank> banks = service.getBanks();
		
		assertThat(banks).isNotEmpty();
		assertThat(banks).containsOnly(priorBank);
	}
	
}
