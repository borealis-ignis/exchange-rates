package com.borealis.erates.service.mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.borealis.erates.TestDataContainer;
import com.borealis.erates.repository.ExchangeRatesDAO;
import com.borealis.erates.repository.converter.impl.ExchangeRateConverter;
import com.borealis.erates.repository.model.dbo.ExchangeRateDbo;

/**
 * @author Kastalski Sergey
 */
@Component
@Profile("test")
public class ExchangeRatesMockDAO implements ExchangeRatesDAO {
	
	@Autowired
	private ExchangeRateConverter converter;
	
	
	private List<ExchangeRateDbo> exchangeRatesStore = new ArrayList<>();
	
	private LocalDateTime updateDate;
	
	private boolean returnResultOnSave = true;
	
	public List<ExchangeRateDbo> getExchangeRatesStore() {
		return exchangeRatesStore;
	}
	
	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}
	
	public void setReturnResultOnSave(boolean returnResultOnSave) {
		this.returnResultOnSave = returnResultOnSave;
	}
	
	
	@Override
	public <S extends ExchangeRateDbo> Optional<S> findOne(Example<S> arg0) {
		return null;
	}
	
	@Override
	public <S extends ExchangeRateDbo> Page<S> findAll(Example<S> arg0, Pageable arg1) {
		return null;
	}
	
	@Override
	public <S extends ExchangeRateDbo> boolean exists(Example<S> arg0) {
		return false;
	}
	
	@Override
	public <S extends ExchangeRateDbo> long count(Example<S> arg0) {
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <S extends ExchangeRateDbo> S save(S rate) {
		final ExchangeRateDbo dbo = converter.convertDto(converter.convertDbo(rate));
		dbo.setId((long) exchangeRatesStore.size() + 1);
		exchangeRatesStore.add(dbo);
		return (S) dbo;
	}
	
	@Override
	public Optional<ExchangeRateDbo> findById(Long arg0) {
		return null;
	}
	
	@Override
	public boolean existsById(Long arg0) {
		return false;
	}
	
	@Override
	public void deleteById(Long arg0) {
	}
	
	@Override
	public void deleteAll(Iterable<? extends ExchangeRateDbo> arg0) {
	}
	
	@Override
	public void deleteAll() {
	}
	
	@Override
	public void delete(ExchangeRateDbo arg0) {
	}
	
	@Override
	public long count() {
		return 0;
	}
	
	@Override
	public Page<ExchangeRateDbo> findAll(Pageable arg0) {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <S extends ExchangeRateDbo> S saveAndFlush(S rate) {
		final ExchangeRateDbo dbo = save(rate);
		this.flush();
		return (S) dbo;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <S extends ExchangeRateDbo> List<S> saveAll(Iterable<S> exchangeRates) {
		if (!returnResultOnSave) {
			return Collections.emptyList();
		}
		
		final List<ExchangeRateDbo> result = new ArrayList<>();
		
		exchangeRates.forEach(rate -> {
			final ExchangeRateDbo dbo = save(rate);
			result.add(dbo);
		}); 
		
		return (List<S>) result;
	}
	
	@Override
	public ExchangeRateDbo getOne(Long arg0) {
		return null;
	}
	
	@Override
	public void flush() {
	}
	
	@Override
	public List<ExchangeRateDbo> findAllById(Iterable<Long> arg0) {
		return null;
	}
	
	@Override
	public <S extends ExchangeRateDbo> List<S> findAll(Example<S> arg0, Sort arg1) {
		return null;
	}
	
	@Override
	public <S extends ExchangeRateDbo> List<S> findAll(Example<S> arg0) {
		return null;
	}
	
	@Override
	public List<ExchangeRateDbo> findAll(Sort arg0) {
		return null;
	}
	
	@Override
	public List<ExchangeRateDbo> findAll() {
		return null;
	}
	
	@Override
	public void deleteInBatch(Iterable<ExchangeRateDbo> arg0) {
	}
	
	@Override
	public void deleteAllInBatch() {
	}
	
	@Override
	public List<LocalDateTime> findDateTimeListByBankCode(String bankCode, Pageable pageable) {
		final List<LocalDateTime> updataDates = new ArrayList<>();
		updataDates.add(updateDate);
		return updataDates;
	}

	@Override
	public List<ExchangeRateDbo> findAllByDate(LocalDateTime from) {
		return null;
	}

	@Override
	public List<ExchangeRateDbo> findAllByDateAndCurrency(LocalDateTime from, Long currencyId) {
		return null;
	}

	@Override
	public List<ExchangeRateDbo> lastExchangeRate(Long bankId, Long currencyId, Pageable pageable) {
		final ExchangeRateDbo dbo = TestDataContainer.getExchangeRateDbo(true);
		dbo.getBank().setId(bankId);
		dbo.getCurrency().setId(currencyId);
		
		final List<ExchangeRateDbo> rates = new ArrayList<>();
		rates.add(dbo);
		return rates;
	}

}
