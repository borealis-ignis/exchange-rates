package com.borealis.erates.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.borealis.erates.TestDataContainer;
import com.borealis.erates.TestExchangeRatesApplication;
import com.borealis.erates.TestExchangeRatesConfiguration.TestBankProcessor;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.repository.BanksDAO;
import com.borealis.erates.repository.CurrenciesDAO;
import com.borealis.erates.repository.converter.impl.BankConverter;
import com.borealis.erates.repository.converter.impl.CurrencyConverter;
import com.borealis.erates.repository.converter.impl.ExchangeRateConverter;
import com.borealis.erates.repository.model.dbo.BankDbo;
import com.borealis.erates.repository.model.dbo.CurrencyDbo;
import com.borealis.erates.repository.model.dbo.ExchangeRateDbo;
import com.borealis.erates.service.mock.ExchangeRatesMockDAO;
import com.borealis.erates.supplier.AbstractBankProcessor;
import com.borealis.erates.updater.ExchangeRatesUpdaterService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestExchangeRatesApplication.class)
@ActiveProfiles("test")
public class ExchangeRatesUpdaterServiceTest {
	
	private ExchangeRatesUpdaterService erateService;
	
	@Autowired
	private TestBankProcessor processor;
	
	@MockBean
	private BanksDAO banksDAO;
	
	@MockBean
	private CurrenciesDAO currenciesDAO;
	
	@Autowired
	private CurrencyConverter currencyConverter;
	
	@Autowired
	private BankConverter bankConverter;
	
	@Autowired
	private ExchangeRateConverter exchangeRateConverter;
	
	@Autowired
	private ExchangeRatesMockDAO exchangeRatesDAO;
	
	
	@Before
	public void setUp() {
		final List<AbstractBankProcessor> processors = new ArrayList<>();
		processors.add(processor);
		erateService = new ExchangeRatesUpdaterService(processors, currenciesDAO, currencyConverter, banksDAO, bankConverter, exchangeRatesDAO, exchangeRateConverter);
	}
	
	private void resetTestData() {
		exchangeRatesDAO.getExchangeRatesStore().clear();
		exchangeRatesDAO.setReturnResultOnSave(true);
		processor.setOkSendRequest(true);
	}
	
	@Test
	public void noCurrenciesTest() {
		resetTestData();
		when(currenciesDAO.findAll()).thenReturn(Collections.emptyList());
		
		erateService.updateExchangeRates();
		
		final List<ExchangeRateDbo> storedRates = exchangeRatesDAO.getExchangeRatesStore();
		assertThat(storedRates).isEmpty();
	}
	
	@Test
	public void processorExceptionTest() {
		resetTestData();
		final List<CurrencyDbo> currencies = new ArrayList<>();
		final CurrencyDbo currency = TestDataContainer.getCurrencyDbo(true);
		currencies.add(currency);
		
		processor.setOkSendRequest(false);
		
		when(currenciesDAO.findAll()).thenReturn(currencies);
		
		
		erateService.updateExchangeRates();
		
		final List<ExchangeRateDbo> storedRates = exchangeRatesDAO.getExchangeRatesStore();
		assertThat(storedRates).isEmpty();
	}
	
	@Test
	public void noResultsFromProcessorTest() {
		resetTestData();
		final List<CurrencyDbo> currencies = new ArrayList<>();
		final CurrencyDbo currency = TestDataContainer.getCurrencyDbo(true);
		currencies.add(currency);
		
		processor.setOutputRates(Collections.emptyList());
		
		when(currenciesDAO.findAll()).thenReturn(currencies);
		
		
		erateService.updateExchangeRates();
		
		final List<ExchangeRateDbo> storedRates = exchangeRatesDAO.getExchangeRatesStore();
		assertThat(storedRates).isEmpty();
	}
	
	@Test
	public void okUpdateTest() {
		resetTestData();
		final List<CurrencyDbo> currencies = new ArrayList<>();
		final CurrencyDbo currency = TestDataContainer.getCurrencyDbo(true);
		currencies.add(currency);
		
		final List<ExchangeRateDto> dtoRates = new ArrayList<>();
		final ExchangeRateDto erateDto = TestDataContainer.getExchangeRateDto(true);
		erateDto.setId(null);
		erateDto.getBank().setCode(processor.getBankCode());
		dtoRates.add(erateDto);
		
		final BankDbo bankDbo = TestDataContainer.getBankDbo(true);
		bankDbo.setCode(processor.getBankCode());
		
		processor.setOutputRates(dtoRates);
		
		when(currenciesDAO.findAll()).thenReturn(currencies);
		when(banksDAO.findByCode(processor.getBankCode())).thenReturn(bankDbo);
		
		exchangeRatesDAO.setUpdateDate(LocalDateTime.now().minusDays(1L));
		
		
		erateService.updateExchangeRates();
		
		
		final List<ExchangeRateDbo> storedRates = exchangeRatesDAO.getExchangeRatesStore();
		
		assertThat(storedRates.size()).isEqualTo(dtoRates.size());
		storedRates.forEach(storedRate -> {
			dtoRates.forEach(rate -> {
				assertThat(storedRate.getBuyRate()).isEqualTo(rate.getBuyRate());
				assertThat(storedRate.getSellRate()).isEqualTo(rate.getSellRate());
				assertThat(storedRate.getUpdateDate()).isEqualTo(rate.getUpdateDate());
				assertThat(storedRate.getCurrency().getCode()).isEqualTo(rate.getCurrency().getCode());
				assertThat(storedRate.getBank().getCode()).isEqualTo(rate.getBank().getCode());
			});
		});
	}
	
	@Test
	public void noUpdate_SameDataExistsTest() {
		resetTestData();
		final List<CurrencyDbo> currencies = new ArrayList<>();
		final CurrencyDbo currency = TestDataContainer.getCurrencyDbo(true);
		currencies.add(currency);
		
		final List<ExchangeRateDto> dtoRates = new ArrayList<>();
		final ExchangeRateDto erateDto = TestDataContainer.getExchangeRateDto(false);
		erateDto.getBank().setCode(processor.getBankCode());
		dtoRates.add(erateDto);
		
		final BankDbo bankDbo = TestDataContainer.getBankDbo(true);
		bankDbo.setCode(processor.getBankCode());
		
		processor.setOutputRates(dtoRates);
		
		when(currenciesDAO.findAll()).thenReturn(currencies);
		when(banksDAO.findByCode(processor.getBankCode())).thenReturn(bankDbo);
		
		exchangeRatesDAO.setUpdateDate(erateDto.getUpdateDate());
		
		
		erateService.updateExchangeRates();
		
		
		final List<ExchangeRateDbo> storedRates = exchangeRatesDAO.getExchangeRatesStore();
		
		assertThat(storedRates).isEmpty();
	}
	
	@Test
	public void noUpdate_SavingFailedTest() {
		resetTestData();
		final List<CurrencyDbo> currencies = new ArrayList<>();
		final CurrencyDbo currency = TestDataContainer.getCurrencyDbo(true);
		currencies.add(currency);
		
		final List<ExchangeRateDto> dtoRates = new ArrayList<>();
		final ExchangeRateDto erateDto = TestDataContainer.getExchangeRateDto(true);
		erateDto.setId(null);
		erateDto.getBank().setCode(processor.getBankCode());
		dtoRates.add(erateDto);
		
		final BankDbo bankDbo = TestDataContainer.getBankDbo(true);
		bankDbo.setCode(processor.getBankCode());
		
		processor.setOutputRates(dtoRates);
		
		when(currenciesDAO.findAll()).thenReturn(currencies);
		when(banksDAO.findByCode(processor.getBankCode())).thenReturn(bankDbo);
		
		exchangeRatesDAO.setUpdateDate(LocalDateTime.now().minusDays(1L));
		exchangeRatesDAO.setReturnResultOnSave(false);
		
		erateService.updateExchangeRates();
		
		
		final List<ExchangeRateDbo> storedRates = exchangeRatesDAO.getExchangeRatesStore();
		
		assertThat(storedRates).isEmpty();
	}
}
