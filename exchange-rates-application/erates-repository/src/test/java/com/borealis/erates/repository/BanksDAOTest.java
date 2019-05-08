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

import com.borealis.erates.repository.model.dbo.BankDbo;


/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BanksDAOTest {
	
	@Autowired
	private BanksDAO banksDAO;
	
	private List<BankDbo> savedBanks;
	
	@Before
	public void setUp() {
		final List<BankDbo> banks = new ArrayList<>();
		banks.add(DBTestUtil.createBank("priorb", true));
		banks.add(DBTestUtil.createBank("btab", true));
		
		savedBanks = banksDAO.saveAll(banks);
		banksDAO.flush();
	}
	
	@Test
	public void findAllBanksTest() {
		final List<BankDbo> foundBanks = banksDAO.findAll();
		
		assertThat(foundBanks).isNotEmpty().containsAll(savedBanks);
	}
	
	@Test
	public void findBankByCodeTest() {
		final BankDbo savedBank = savedBanks.get(0);
		
		final BankDbo foundBank = banksDAO.findByCode(savedBank.getCode());
		
		assertThat(foundBank).isEqualToComparingFieldByFieldRecursively(savedBank);
	}
	
}
