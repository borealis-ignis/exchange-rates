package com.borealis.erates.repository.converter;

import java.util.List;

/**
 * @author Kastalski Sergey
 */
public interface DtoDboConverter<D, T> {
	
	D convertDto(T dto);
	
	T convertDbo(D dbo);
	
	List<D> convertDtos(List<T> dtos);
	
	List<T> convertDbos(List<D> dbos);
	
}
