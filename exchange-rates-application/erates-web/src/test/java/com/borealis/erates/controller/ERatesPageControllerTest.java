package com.borealis.erates.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.borealis.erates.TestDataContainer;
import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.service.ExchangeRatesService;
import com.borealis.erates.supplier.Bank;


/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ERatesPageController.class)
public class ERatesPageControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ExchangeRatesService eratesService;
	
	private Bank prior = new Bank() {
		
		@Override
		public String getBankName() {
			return "Prior Bank";
		}
		
		@Override
		public String getBankCode() {
			return "priorb";
		}
	};
	
	
	@Test
	public void getExchangeRatesPageTest() throws Exception {
		final List<Bank> banks = new ArrayList<>();
		banks.add(prior);
		
		final List<CurrencyDto> currencies = new ArrayList<>();
		currencies.add(TestDataContainer.getUSDCurrencyDto(true));
		currencies.add(TestDataContainer.getRUBCurrencyDto(true));
		currencies.add(TestDataContainer.getEURCurrencyDto(true));
		
		when(eratesService.getBanks()).thenReturn(banks);
		when(eratesService.getCurrencies()).thenReturn(currencies);
		when(eratesService.determineDefaultCurrencyCode(currencies)).thenReturn("USD");
		
		mvc.perform(get("/erates").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("banks", IsCollectionWithSize.hasSize(1)))
			.andExpect(model().attribute("currencies", IsCollectionWithSize.hasSize(3)))
			.andExpect(model().attribute("bankCode", is("priorb")))
			.andExpect(model().attribute("currencyCode", is("USD")))
			.andExpect(view().name("erates"));
	}
	
	@Test
	public void getCompareExchangeRatesPageTest() throws Exception {
		final List<Bank> banks = new ArrayList<>();
		banks.add(prior);
		
		final List<CurrencyDto> currencies = new ArrayList<>();
		currencies.add(TestDataContainer.getUSDCurrencyDto(true));
		currencies.add(TestDataContainer.getRUBCurrencyDto(true));
		currencies.add(TestDataContainer.getEURCurrencyDto(true));
		
		when(eratesService.getBanks()).thenReturn(banks);
		when(eratesService.getCurrencies()).thenReturn(currencies);
		when(eratesService.determineDefaultCurrencyCode(currencies)).thenReturn("USD");
		
		mvc.perform(get("/compare-erates").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("banks", IsCollectionWithSize.hasSize(1)))
			.andExpect(model().attribute("currencies", IsCollectionWithSize.hasSize(3)))
			.andExpect(model().attribute("currencyCode", is("USD")))
			.andExpect(view().name("compare-erates"));
	}
	
	@Test
	public void getExchangeRatesPageNoUSDTest() throws Exception {
		final List<Bank> banks = new ArrayList<>();
		banks.add(prior);
		
		final List<CurrencyDto> currencies = new ArrayList<>();
		currencies.add(TestDataContainer.getRUBCurrencyDto(true));
		currencies.add(TestDataContainer.getEURCurrencyDto(true));
		
		when(eratesService.getBanks()).thenReturn(banks);
		when(eratesService.getCurrencies()).thenReturn(currencies);
		when(eratesService.determineDefaultCurrencyCode(currencies)).thenReturn("RUB");
		
		mvc.perform(get("/erates").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("banks", IsCollectionWithSize.hasSize(1)))
			.andExpect(model().attribute("currencies", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("bankCode", is("priorb")))
			.andExpect(model().attribute("currencyCode", is("RUB")))
			.andExpect(view().name("erates"));
	}
	
	@Test
	public void getExchangeRatesPageNoCurrenciesTest() throws Exception {
		final List<Bank> banks = new ArrayList<>();
		banks.add(prior);
		
		final List<CurrencyDto> currencies = new ArrayList<>();
		
		when(eratesService.getBanks()).thenReturn(banks);
		when(eratesService.getCurrencies()).thenReturn(currencies);
		
		mvc.perform(get("/erates").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("banks", IsCollectionWithSize.hasSize(1)))
			.andExpect(model().attribute("currencies", IsCollectionWithSize.hasSize(0)))
			.andExpect(model().attribute("bankCode", is("priorb")))
			.andExpect(model().attribute("currencyCode", IsNull.nullValue()))
			.andExpect(view().name("erates"));
	}
	
	@Test
	public void getExchangeRatesPageNoBanksTest() throws Exception {
		final List<Bank> banks = new ArrayList<>();
		
		final List<CurrencyDto> currencies = new ArrayList<>();
		currencies.add(TestDataContainer.getUSDCurrencyDto(true));
		currencies.add(TestDataContainer.getRUBCurrencyDto(true));
		currencies.add(TestDataContainer.getEURCurrencyDto(true));
		
		when(eratesService.getBanks()).thenReturn(banks);
		when(eratesService.getCurrencies()).thenReturn(currencies);
		when(eratesService.determineDefaultCurrencyCode(currencies)).thenReturn("USD");
		
		mvc.perform(get("/erates").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("banks", IsCollectionWithSize.hasSize(0)))
			.andExpect(model().attribute("currencies", IsCollectionWithSize.hasSize(3)))
			.andExpect(model().attribute("bankCode", IsNull.nullValue()))
			.andExpect(model().attribute("currencyCode", is("USD")))
			.andExpect(view().name("erates"));
	}
	
	@Test
	public void getExchangeRatesPageExceptionTest() throws Exception {
		final List<Bank> banks = new ArrayList<>();
		
		when(eratesService.getBanks()).thenReturn(banks);
		when(eratesService.getCurrencies()).thenThrow(new NullPointerException());
		
		mvc.perform(get("/erates").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("banks", IsNull.nullValue()))
			.andExpect(model().attribute("currencies", IsNull.nullValue()))
			.andExpect(model().attribute("bankCode", IsNull.nullValue()))
			.andExpect(model().attribute("currencyCode", IsNull.nullValue()))
			.andExpect(view().name("error"));
	}
	
}
