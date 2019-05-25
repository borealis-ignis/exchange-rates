package com.borealis.erates.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.borealis.erates.TestDataContainer;
import com.borealis.erates.TestExchangeRatesApplication;
import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.repository.BanksDAO;
import com.borealis.erates.repository.CurrenciesDAO;
import com.borealis.erates.repository.DBTestUtil;
import com.borealis.erates.repository.ExchangeRatesDAO;
import com.borealis.erates.repository.model.dbo.BankDbo;
import com.borealis.erates.repository.model.dbo.CurrencyDbo;
import com.borealis.erates.repository.model.dbo.ExchangeRateDbo;
import com.borealis.erates.supplier.Bank;


/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestExchangeRatesApplication.class)
@ActiveProfiles("test")
public class ExchangeRatesServiceTest {
	
	@Autowired
	private ExchangeRatesService service;
	
	@Autowired
	private Bank priorBank;
	
	@MockBean
	private BanksDAO banksDAO;
	
	@MockBean
	private CurrenciesDAO currenciesDAO;
	
	@MockBean
	private ExchangeRatesDAO exchangeRatesDAO;
	
	
	@Test
	public void getExchangeRatesByFromDateTest() {
		final LocalDateTime updateDate = LocalDateTime.now();
		final CurrencyDbo usd = DBTestUtil.createCurrency("USD");
		usd.setId(1l);
		final BankDbo prior = DBTestUtil.createBank(1l, "priorb", true);
		final List<ExchangeRateDbo> rates = new ArrayList<>();
		rates.add(DBTestUtil.createExchangeRate("2.1000", "2.1511", usd, prior, updateDate));
		rates.add(DBTestUtil.createExchangeRate("2.1000", "2.1511", usd, prior, updateDate.minusMinutes(5l)));
		rates.add(DBTestUtil.createExchangeRate("2.1000", "2.1511", usd, prior, updateDate.minusMinutes(10l)));
		rates.add(DBTestUtil.createExchangeRate("2.1000", "2.1511", usd, prior, updateDate.minusMinutes(14l)));
		
		final List<LocalDateTime> dates = new ArrayList<>();
		rates.forEach(r -> dates.add(r.getUpdateDate()));
		
		
		final LocalDateTime from = updateDate.minusMinutes(20l);
		
		when(exchangeRatesDAO.findAllByDate(from)).thenReturn(rates);
		
		final List<ExchangeRateDto> dtoRates = service.getExchangeRates(from, null);
		
		assertThat(dtoRates.size()).isEqualTo(rates.size());
		assertThat(dtoRates).extracting("updateDate", LocalDateTime.class).containsAnyElementsOf(dates);
	}
	
	@Test
	public void getExchangeRatesByFromDateAndCurrencyTest() {
		final LocalDateTime updateDate = LocalDateTime.now();
		final CurrencyDbo usd = DBTestUtil.createCurrency("USD");
		usd.setId(1l);
		final BankDbo prior = DBTestUtil.createBank(1l, "priorb", true);
		final List<ExchangeRateDbo> rates = new ArrayList<>();
		rates.add(DBTestUtil.createExchangeRate("2.1000", "2.1511", usd, prior, updateDate));
		rates.add(DBTestUtil.createExchangeRate("2.1000", "2.1511", usd, prior, updateDate.minusMinutes(5l)));
		rates.add(DBTestUtil.createExchangeRate("2.1000", "2.1511", usd, prior, updateDate.minusMinutes(10l)));
		rates.add(DBTestUtil.createExchangeRate("2.1000", "2.1511", usd, prior, updateDate.minusMinutes(14l)));
		
		final List<LocalDateTime> dates = new ArrayList<>();
		rates.forEach(r -> dates.add(r.getUpdateDate()));
		
		
		final LocalDateTime from = updateDate.minusMinutes(20l);
		
		when(exchangeRatesDAO.findAllByDateAndCurrency(from, usd.getId())).thenReturn(rates);
		
		final List<ExchangeRateDto> dtoRates = service.getExchangeRates(from, usd.getId());
		
		assertThat(dtoRates.size()).isEqualTo(rates.size());
		assertThat(dtoRates).extracting("updateDate", LocalDateTime.class).containsAnyElementsOf(dates);
	}
	
	@Test
	public void getCurrenciesTest() {
		final CurrencyDbo usd = DBTestUtil.createCurrency("USD");
		usd.setId(1l);
		final CurrencyDbo eur = DBTestUtil.createCurrency("EUR");
		eur.setId(2l);
		final List<CurrencyDbo> currencies = new ArrayList<>();
		currencies.add(usd);
		currencies.add(eur);
		
		final List<String> codes = new ArrayList<>();
		currencies.forEach(c -> codes.add(c.getCode()));
		
		when(currenciesDAO.findAll()).thenReturn(currencies);
		
		final List<CurrencyDto> dtoCurrencies = service.getCurrencies();
		
		assertThat(dtoCurrencies.size()).isEqualTo(currencies.size());
		assertThat(dtoCurrencies).extracting("code", String.class).containsAnyElementsOf(codes);
	}
	
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
