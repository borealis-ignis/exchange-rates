package com.borealis.erates.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.borealis.erates.repository.model.dbo.BankDbo;

/**
 * @author Kastalski Sergey
 */
public interface BanksDAO extends JpaRepository<BankDbo, Long> {
	
}
