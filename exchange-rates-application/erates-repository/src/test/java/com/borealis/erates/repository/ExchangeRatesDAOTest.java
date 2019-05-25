package com.borealis.erates.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.borealis.erates.TestExchangeRatesApplication;
import com.borealis.erates.repository.model.dbo.BankDbo;
import com.borealis.erates.repository.model.dbo.CurrencyDbo;
import com.borealis.erates.repository.model.dbo.ExchangeRateDbo;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@DataJpaTest(showSql = true, properties = { 
		"spring.jpa.properties.hibernate.jdbc.batch_size=20", 
		"spring.jpa.properties.hibernate.order_inserts=true"
		})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ContextConfiguration(classes = TestExchangeRatesApplication.class)
@EnableAutoConfiguration
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
	
	private LocalDateTime updateDate;
	
	@Before
	public void setUp() {
		final List<ExchangeRateDbo> erates = new ArrayList<>();
		updateDate = LocalDateTime.now();
		
		usd = currenciesDAO.saveAndFlush(DBTestUtil.createCurrency("USD"));
		eur = currenciesDAO.saveAndFlush(DBTestUtil.createCurrency("EUR"));
		prior = banksDAO.saveAndFlush(DBTestUtil.createBank("priorb", true));
		
		erates.add(DBTestUtil.createExchangeRate("2.1000", "2.1511", usd, prior, updateDate));
		erates.add(DBTestUtil.createExchangeRate("2.4101", "2.4712", eur, prior, updateDate.minusHours(1l)));
		
		savedERates = exchangeRatesDAO.saveAll(erates);
		exchangeRatesDAO.flush();
	}
	
	@Test
	public void findAllExchangeRatesTest() {
		final List<ExchangeRateDbo> foundRates = exchangeRatesDAO.findAll();
		
		assertThat(foundRates).isNotEmpty().containsAll(savedERates);
	}
	
	@Test
	public void findDateTimeListByBankCodeTest() {
		final List<LocalDateTime> sortedUpdateDates = exchangeRatesDAO.findDateTimeListByBankCode(prior.getCode(), PageRequest.of(0, 1));
		final LocalDateTime lastUpdateDate = sortedUpdateDates.stream().findFirst().orElse(null);
		
		assertThat(lastUpdateDate).isNotNull().isEqualTo(updateDate);
	}
	
	@Test
	public void findNoDateTimeListByBankCodeTest() {
		exchangeRatesDAO.deleteAllInBatch();
		
		final List<LocalDateTime> sortedUpdateDates = exchangeRatesDAO.findDateTimeListByBankCode(prior.getCode(), PageRequest.of(0, 1));
		final LocalDateTime lastUpdateDate = sortedUpdateDates.stream().findFirst().orElse(null);
		
		assertThat(lastUpdateDate).isNull();
	}
	
	@Test
	public void findExchangeRatesByFromDateTest() {
		final LocalDateTime updateDateMinus20m = updateDate.minusMinutes(20l);
		final List<ExchangeRateDbo> foundRates = exchangeRatesDAO.findAllByDate(updateDateMinus20m);
		
		assertThat(foundRates).isNotEmpty().allMatch(r -> r.getUpdateDate().isAfter(updateDateMinus20m));
	}
	
	@Test
	public void findExchangeRatesByFromDateAndCurrencyTest() {
		final LocalDateTime updateDateMinus20m = updateDate.minusMinutes(20l);
		final Long currencyId = usd.getId();
		final List<ExchangeRateDbo> foundRates = exchangeRatesDAO.findAllByDateAndCurrency(updateDateMinus20m, currencyId);
		
		assertThat(foundRates).isNotEmpty().allMatch(r -> r.getUpdateDate().isAfter(updateDateMinus20m));
		assertThat(foundRates).allMatch(rate -> usd.getCode().equals(rate.getCurrency().getCode()));
	}
	
	@Test
	public void saveNewExchangeRatesTest() {
		final List<ExchangeRateDbo> newExchangeRates = new ArrayList<>();
		newExchangeRates.add(DBTestUtil.createExchangeRate("2.1111", "2.1611", usd, prior, LocalDateTime.now().plusMinutes(30)));
		newExchangeRates.add(DBTestUtil.createExchangeRate("2.3111", "2.3611", eur, prior, LocalDateTime.now().plusMinutes(30)));
		
		final List<ExchangeRateDbo> savedNewExchangeRates = exchangeRatesDAO.saveAll(newExchangeRates);
		exchangeRatesDAO.flush();
		
		Assert.assertEquals(newExchangeRates.size(), savedNewExchangeRates.size());
		assertThat(savedNewExchangeRates).extracting("id").isNotNull();
		
		final List<ExchangeRateDbo> foundExchangeRates = exchangeRatesDAO.findAll();
		assertThat(foundExchangeRates).isNotEmpty().containsAll(savedNewExchangeRates);
	}
	
	@Test
	public void saveNewExchangeRateTest() {
		final ExchangeRateDbo newExchangeRate = DBTestUtil.createExchangeRate("2.1111", "2.1611", usd, prior, LocalDateTime.now().plusMinutes(30));
		
		exchangeRatesDAO.saveAndFlush(newExchangeRate);
		
		final List<ExchangeRateDbo> foundExchangeRates = exchangeRatesDAO.findAll();
		assertThat(foundExchangeRates).isNotEmpty().contains(newExchangeRate);
	}
	
}
