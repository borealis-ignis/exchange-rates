package com.borealis.erates.updater;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.borealis.erates.supplier.BankProcessor;
import com.borealis.erates.supplier.exception.RatesProcessingException;

/**
 * @author Kastalski Sergey
 */
@Service
public class ExchangeRatesUpdaterService {
	
	private static Logger logger = LoggerFactory.getLogger(ExchangeRatesUpdaterService.class);
	
	private List<BankProcessor> processors;
	
	public ExchangeRatesUpdaterService(final List<BankProcessor> processors) {
		this.processors = processors;
	}
	
	@Scheduled(cron = "${cron.update.exchangerates}")
	public void updateExchangeRates() {
		processors.forEach(p -> {
			try {
				p.process();
			} catch (final RatesProcessingException e) {
				logger.error("Processing was failed for " + p, e);
			}
		});
	}
	
}
