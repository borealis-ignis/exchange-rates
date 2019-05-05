package com.borealis.erates.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
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
	
	@Test
	public void getNoBanksTest() {
		final List<BankDbo> dboBanks = new ArrayList<>();
		dboBanks.add(TestDataContainer.getBankDbo(true));
		
		when(banksDAO.findAll()).thenReturn(Collections.emptyList());
		
		final List<Bank> banks = service.getBanks();
		
		assertThat(banks).isEmpty();
	}
	
	@Test
	public void getNoBanksDueToNullCodeTest() {
		final List<Bank> serviceBanks = getBanksListFromService();
		assertThat(serviceBanks).isNotEmpty();
		serviceBanks.clear();
		serviceBanks.add(new Bank() {
			
			@Override
			public String getBankName() {
				return "Mock Bank";
			}
			
			@Override
			public String getBankCode() {
				return null;
			}
		});
		
		final List<BankDbo> dboBanks = new ArrayList<>();
		dboBanks.add(TestDataContainer.getBankDbo(true));
		
		when(banksDAO.findAll()).thenReturn(dboBanks);
		
		final List<Bank> banks = service.getBanks();
		
		assertThat(banks).isEmpty();
	}
	
	@Test
	public void getNoBanksDueToUnknownBankTest() {
		final List<Bank> serviceBanks = getBanksListFromService();
		assertThat(serviceBanks).isNotEmpty();
		serviceBanks.clear();
		serviceBanks.add(new Bank() {
			
			@Override
			public String getBankName() {
				return "Mock Bank";
			}
			
			@Override
			public String getBankCode() {
				return "mockb";
			}
		});
		
		final List<BankDbo> dboBanks = new ArrayList<>();
		dboBanks.add(TestDataContainer.getBankDbo(true));
		
		when(banksDAO.findAll()).thenReturn(dboBanks);
		
		final List<Bank> banks = service.getBanks();
		
		assertThat(banks).isEmpty();
	}
	
	@SuppressWarnings("unchecked")
	private List<Bank> getBanksListFromService() {
		try {
			final Field field = service.getClass().getDeclaredField("banksList");
			if (Modifier.isPrivate(field.getModifiers())) {
				field.setAccessible(true);
				return (List<Bank>) field.get(service);
			}
		} catch (final NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new AssertionError("Can't find banks in serivce", e);
		}
		throw new AssertionError("Can't find banks in serivce");
	}
	
}
