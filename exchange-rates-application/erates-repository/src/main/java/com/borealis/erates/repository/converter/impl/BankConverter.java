package com.borealis.erates.repository.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.borealis.erates.repository.converter.DtoDboConverter;
import com.borealis.erates.repository.model.dbo.BankDbo;
import com.borealis.erates.repository.model.dto.BankDto;

/**
 * @author Kastalski Sergey
 */
@Service
public class BankConverter implements DtoDboConverter<BankDbo, BankDto> {
	
	@Override
	public BankDbo convertDto(final BankDto dto) {
		final BankDbo dbo = new BankDbo();
		dbo.setId(dto.getId());
		dbo.setCode(dto.getCode());
		dbo.setActive(dto.getActive());
		return dbo;
	}
	
	@Override
	public BankDto convertDbo(final BankDbo dbo) {
		final BankDto dto = new BankDto();
		dto.setId(dbo.getId());
		dto.setCode(dbo.getCode());
		dto.setActive(dbo.getActive());
		return dto;
	}
	
	@Override
	public List<BankDbo> convertDtos(final List<BankDto> dtos) {
		final List<BankDbo> dbos = new ArrayList<>();
		dtos.forEach(dto -> dbos.add(convertDto(dto)));
		return dbos;
	}
	
	@Override
	public List<BankDto> convertDbos(final List<BankDbo> dbos) {
		final List<BankDto> dtos = new ArrayList<>();
		dbos.forEach(dbo -> dtos.add(convertDbo(dbo)));
		return dtos;
	}
	
}
