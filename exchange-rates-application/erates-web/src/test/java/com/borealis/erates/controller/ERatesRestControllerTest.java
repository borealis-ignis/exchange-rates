package com.borealis.erates.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.borealis.erates.service.ExchangeRatesService;

import net.minidev.json.JSONArray;


/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ERatesRestController.class)
public class ERatesRestControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ExchangeRatesService eratesService;
	
	
	@Test
	public void getExchangeRatesTest() throws Exception {
		final Long currencyId = 1l;
		
		mvc.perform(get("/exchangerates?currencyId=" + currencyId)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$", isA(JSONArray.class)))
			.andExpect(jsonPath("$.length()", is(0)));
	}
	
}
