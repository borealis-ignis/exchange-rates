package com.borealis.erates.repository.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.borealis.erates.repository.converter.DtoDboConverter;
import com.borealis.erates.repository.model.dbo.ExchangeRateDbo;
import com.borealis.erates.repository.model.dto.ExchangeRateDto;

/**
 * @author Kastalski Sergey
 */
@Service
public class ExchangeRateConverter implements DtoDboConverter<ExchangeRateDbo, ExchangeRateDto> {
	
	private CurrencyConverter currencyConverter;
	
	private BankConverter bankConverter;
	
	public ExchangeRateConverter(
			final CurrencyConverter currencyConverter,
			final BankConverter bankConverter) {
		this.currencyConverter = currencyConverter;
		this.bankConverter = bankConverter;
	}
	
	@Override
	public ExchangeRateDbo convertDto(final ExchangeRateDto dto) {
		final ExchangeRateDbo dbo = new ExchangeRateDbo();
		dbo.setId(dto.getId());
		dbo.setBuyRate(dto.getBuyRate());
		dbo.setSellRate(dto.getSellRate());
		dbo.setUpdateDate(dto.getUpdateDate());
		dbo.setCurrency(currencyConverter.convertDto(dto.getCurrency()));
		dbo.setBank(bankConverter.convertDto(dto.getBank()));
		return dbo;
	}
	
	@Override
	public ExchangeRateDto convertDbo(final ExchangeRateDbo dbo) {
		final ExchangeRateDto dto = new ExchangeRateDto();
		dto.setId(dbo.getId());
		dto.setBuyRate(dbo.getBuyRate());
		dto.setSellRate(dbo.getSellRate());
		dto.setUpdateDate(dbo.getUpdateDate());
		dto.setCurrency(currencyConverter.convertDbo(dbo.getCurrency()));
		dto.setBank(bankConverter.convertDbo(dbo.getBank()));
		return dto;
	}
	
	@Override
	public List<ExchangeRateDbo> convertDtos(final List<ExchangeRateDto> dtos) {
		final List<ExchangeRateDbo> dbos = new ArrayList<>();
		dtos.forEach(dto -> dbos.add(convertDto(dto)));
		return dbos;
	}
	
	@Override
	public List<ExchangeRateDto> convertDbos(final List<ExchangeRateDbo> dbos) {
		final List<ExchangeRateDto> dtos = new ArrayList<>();
		dbos.forEach(dbo -> dtos.add(convertDbo(dbo)));
		return dtos;
	}
	
}
