package com.borealis.erates.repository.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.borealis.erates.repository.converter.DtoDboConverter;
import com.borealis.erates.repository.model.dbo.CurrencyDbo;
import com.borealis.erates.repository.model.dto.CurrencyDto;

/**
 * @author Kastalski Sergey
 */
@Service
public class CurrencyConverter implements DtoDboConverter<CurrencyDbo, CurrencyDto> {
	
	@Override
	public CurrencyDbo convertDto(final CurrencyDto dto) {
		final CurrencyDbo dbo = new CurrencyDbo();
		dbo.setId(dto.getId());
		dbo.setCode(dto.getCode());
		return dbo;
	}
	
	@Override
	public CurrencyDto convertDbo(final CurrencyDbo dbo) {
		final CurrencyDto dto = new CurrencyDto();
		dto.setId(dbo.getId());
		dto.setCode(dbo.getCode());
		return dto;
	}
	
	@Override
	public List<CurrencyDbo> convertDtos(final List<CurrencyDto> dtos) {
		final List<CurrencyDbo> dbos = new ArrayList<>();
		dtos.forEach(dto -> dbos.add(convertDto(dto)));
		return dbos;
	}
	
	@Override
	public List<CurrencyDto> convertDbos(final List<CurrencyDbo> dbos) {
		final List<CurrencyDto> dtos = new ArrayList<>();
		dbos.forEach(dbo -> dtos.add(convertDbo(dbo)));
		return dtos;
	}
	
}
