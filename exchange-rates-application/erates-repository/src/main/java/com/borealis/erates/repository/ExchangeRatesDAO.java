package com.borealis.erates.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.borealis.erates.repository.model.dbo.ExchangeRateDbo;

/**
 * @author Kastalski Sergey
 */
public interface ExchangeRatesDAO extends JpaRepository<ExchangeRateDbo, Long> {
	
}
