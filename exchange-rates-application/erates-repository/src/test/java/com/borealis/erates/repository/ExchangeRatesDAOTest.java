package com.borealis.erates.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
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

import com.borealis.erates.repository.model.dbo.BankDbo;
import com.borealis.erates.repository.model.dbo.CurrencyDbo;
import com.borealis.erates.repository.model.dbo.ExchangeRateDbo;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ExchangeRatesDAOTest {
	
	@Autowired
	private ExchangeRatesDAO exchangeRatesDAO;
	
	@Autowired
	private BanksDAO banksDAO;
	
	@Autowired
	private CurrenciesDAO currenciesDAO;
	
	
	private List<ExchangeRateDbo> savedERates;
	
	private CurrencyDbo usd;
	
	private CurrencyDbo eur;
	
	private BankDbo prior;
	
	
	@Before
	public void setUp() {
		final List<ExchangeRateDbo> erates = new ArrayList<>();
		final LocalDateTime updateDate = LocalDateTime.now();
		
		usd = currenciesDAO.saveAndFlush(DBTestUtil.createCurrency("USD"));
		eur = currenciesDAO.saveAndFlush(DBTestUtil.createCurrency("EUR"));
		prior = banksDAO.saveAndFlush(DBTestUtil.createBank("priorb", true));
		
		erates.add(DBTestUtil.createExchangeRate("2.1000", "2.1511", usd, prior, updateDate));
		erates.add(DBTestUtil.createExchangeRate("2.4101", "2.4712", eur, prior, updateDate));
		
		savedERates = exchangeRatesDAO.saveAll(erates);
		exchangeRatesDAO.flush();
	}
	
	@Test
	public void findAllExchangeRatesTest() {
		final List<ExchangeRateDbo> foundBanks = exchangeRatesDAO.findAll();
		
		assertThat(foundBanks).isNotEmpty().containsAll(savedERates);
	}
	
	@Test
	public void saveNewExchangeRateTest() {
		final ExchangeRateDbo newExchangeRate = DBTestUtil.createExchangeRate("2.1111", "2.1611", usd, prior, LocalDateTime.now().plusMinutes(30));
		
		exchangeRatesDAO.saveAndFlush(newExchangeRate);
		
		final List<ExchangeRateDbo> foundExchangeRates = exchangeRatesDAO.findAll();
		assertThat(foundExchangeRates).isNotEmpty().contains(newExchangeRate);
	}
	
}
