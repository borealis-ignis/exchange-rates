package com.borealis.erates.updater;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.borealis.erates.model.dto.BankDto;
import com.borealis.erates.model.dto.CurrencyDto;
import com.borealis.erates.model.dto.ExchangeRateDto;
import com.borealis.erates.repository.BanksDAO;
import com.borealis.erates.repository.CurrenciesDAO;
import com.borealis.erates.repository.ExchangeRatesDAO;
import com.borealis.erates.repository.converter.impl.BankConverter;
import com.borealis.erates.repository.converter.impl.CurrencyConverter;
import com.borealis.erates.repository.converter.impl.ExchangeRateConverter;
import com.borealis.erates.repository.model.dbo.BankDbo;
import com.borealis.erates.repository.model.dbo.ExchangeRateDbo;
import com.borealis.erates.supplier.AbstractBankProcessor;
import com.borealis.erates.supplier.exception.RatesProcessingException;

/**
 * @author Kastalski Sergey
 */
@Service
public class ExchangeRatesUpdaterService {
	
	private static Logger logger = LoggerFactory.getLogger(ExchangeRatesUpdaterService.class);
	
	private List<AbstractBankProcessor> processors;
	
	private CurrenciesDAO currenciesDAO;
	
	private CurrencyConverter currencyConverter;
	
	private BanksDAO banksDAO;
	
	private BankConverter bankConverter;
	
	private ExchangeRatesDAO exchangeRatesDAO;
	
	private ExchangeRateConverter exchangeRateConverter;
	
	
	private Map<String, BankDto> banksMap = new HashMap<>();
	
	
	public ExchangeRatesUpdaterService(
			final List<AbstractBankProcessor> processors,
			final CurrenciesDAO currenciesDAO,
			final CurrencyConverter currencyConverter,
			final BanksDAO banksDAO,
			final BankConverter bankConverter,
			final ExchangeRatesDAO exchangeRatesDAO,
			final ExchangeRateConverter exchangeRateConverter) {
		this.processors = processors;
		this.currenciesDAO = currenciesDAO;
		this.currencyConverter = currencyConverter;
		this.banksDAO = banksDAO;
		this.bankConverter = bankConverter;
		this.exchangeRatesDAO = exchangeRatesDAO;
		this.exchangeRateConverter = exchangeRateConverter;
	}
	
	@Scheduled(cron = "${cron.update.exchangerates}")
	public void updateExchangeRates() {
		final List<CurrencyDto> currencies = currencyConverter.convertDbos(currenciesDAO.findAll());
		if (currencies.isEmpty()) {
			logger.warn("Currencies not found");
			return;
		}
		
		final List<ExchangeRateDto> exchangeRates = new ArrayList<>();
		processors.forEach(p -> {
			final BankDto bank = getBank(p.getBankCode());
			if (bank == null || BooleanUtils.isFalse(bank.getActive())) {
				return;
			}
			
			final List<ExchangeRateDto> currentExchangeRates;
			try {
				currentExchangeRates = p.process(currencies);
			} catch (final RatesProcessingException | RuntimeException e) {
				logger.error("Processing was failed for " + p, e);
				return;
			}
			
			if (currentExchangeRates.isEmpty()) {
				logger.warn("Exchange Rates wasn't updated for " + p);
				return;
			}
			
			final List<LocalDateTime> sortedUpdateDates = exchangeRatesDAO.findDateTimeListByBankCode(bank.getCode(), PageRequest.of(0, 1));
			final LocalDateTime lastUpdateDate = sortedUpdateDates.stream().findFirst().orElse(null);
			
			final Iterator<ExchangeRateDto> erateIterator = currentExchangeRates.iterator();
			while (erateIterator.hasNext()) {
				final ExchangeRateDto erate = erateIterator.next();
				if (erate.getBuyRate() != null &&
						erate.getSellRate() != null &&
						erate.getCurrency() != null &&
						erate.getUpdateDate() != null &&
						(lastUpdateDate == null || erate.getUpdateDate().isAfter(lastUpdateDate))) {
					final ExchangeRateDbo lastErate = getLastExchangeRate(bank.getId(), erate.getCurrency().getId());
					if (lastErate == null || erate.getBuyRate().compareTo(lastErate.getBuyRate()) != 0 || erate.getSellRate().compareTo(lastErate.getSellRate()) != 0) {
						erate.setBank(bank);
						continue;
					}
				}
				erateIterator.remove();
			}
			
			exchangeRates.addAll(currentExchangeRates);
		});
		
		if (!exchangeRates.isEmpty()) {
			final List<ExchangeRateDbo> savedExchangeRates = exchangeRatesDAO.saveAll(exchangeRateConverter.convertDtos(exchangeRates));
			if (savedExchangeRates.isEmpty()) {
				logger.warn("New exchange rates weren't saved");
				return;
			}
			exchangeRatesDAO.flush();
		}
	}
	
	private ExchangeRateDbo getLastExchangeRate(final long bankId, final long currencyId) {
		final List<ExchangeRateDbo> lastErates = exchangeRatesDAO.lastExchangeRate(bankId, currencyId, PageRequest.of(0, 1));
		if (lastErates.isEmpty()) {
			return null;
		}
		
		return lastErates.get(0);
	}
	
	private BankDto getBank(final String bankCode) {
		BankDto bank = banksMap.get(bankCode);
		if (bank == null) {
			final BankDbo bankDbo = banksDAO.findByCode(bankCode);
			if (bankDbo == null) {
				logger.warn("Bank with code \"" + bankCode + "\" is not found in DB");
				return null;
			}
			bank = bankConverter.convertDbo(bankDbo);
			banksMap.put(bankCode, bank);
		}
		return bank;
	}
	
}
