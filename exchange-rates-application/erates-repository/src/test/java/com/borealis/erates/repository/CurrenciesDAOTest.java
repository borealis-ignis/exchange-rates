package com.borealis.erates.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.borealis.erates.repository.model.dbo.CurrencyDbo;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@DataJpaTest(showSql = true, properties = { 
		"spring.jpa.properties.hibernate.generate_statistics=true",
		"spring.jpa.properties.hibernate.jdbc.batch_size=1", 
		"spring.jpa.properties.hibernate.order_inserts=true"
		})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CurrenciesDAOTest {
	
	@Autowired
	private CurrenciesDAO currenciesDAO;
	
	private List<CurrencyDbo> savedCurrencies;
	
	@Before
	public void setUp() {
		final List<CurrencyDbo> currencies = new ArrayList<>();
		currencies.add(DBTestUtil.createCurrency("USD"));
		currencies.add(DBTestUtil.createCurrency("EUR"));
		
		savedCurrencies = currenciesDAO.saveAll(currencies);
		currenciesDAO.flush();
	}
	
	@Test
	public void findAllCurrenciesTest() {
		final List<CurrencyDbo> foundCurrencies = currenciesDAO.findAll();
		
		assertThat(foundCurrencies).isNotEmpty().containsAll(savedCurrencies);
	}
}
