package com.borealis.erates.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.borealis.erates.repository.model.dbo.CurrencyDbo;

/**
 * @author Kastalski Sergey
 */
public interface CurrenciesDAO extends JpaRepository<CurrencyDbo, Long> {
	
}
