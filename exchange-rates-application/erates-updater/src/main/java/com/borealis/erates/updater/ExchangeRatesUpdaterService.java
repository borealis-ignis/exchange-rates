package com.borealis.erates.updater;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
			final List<ExchangeRateDto> currentExchangeRates;
			try {
				currentExchangeRates = p.process(currencies);
			} catch (final RatesProcessingException e) {
				logger.error("Processing was failed for " + p, e);
				return;
			}
			
			if (currentExchangeRates.isEmpty()) {
				logger.warn("Exchange Rates wasn't updated for " + p);
				return;
			}
			
			final String bankCode = p.getBankCode();
			final List<LocalDateTime> sortedUpdateDates = exchangeRatesDAO.findDateTimeListByBankCode(bankCode, PageRequest.of(0, 1));
			final LocalDateTime lastUpdateDate = sortedUpdateDates.stream().findFirst().orElse(null);
			
			final List<ExchangeRateDto> filteredExchangeRates = currentExchangeRates.stream().filter(erate -> 
				erate.getBuyRate() != null &&
				erate.getSellRate() != null &&
				erate.getCurrency() != null &&
				erate.getUpdateDate() != null &&
				erate.getUpdateDate().isAfter(lastUpdateDate))
			.collect(Collectors.toList());
			
			filteredExchangeRates.forEach(erate -> erate.setBank(getBank(bankCode)));
			
			exchangeRates.addAll(filteredExchangeRates);
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
	
	private BankDto getBank(final String bankCode) {
		BankDto bank = banksMap.get(bankCode);
		if (bank == null) {
			bank = bankConverter.convertDbo(banksDAO.findByCode(bankCode));
			banksMap.put(bankCode, bank);
		}
		return bank;
	}
	
}
