package com.borealis.erates.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.borealis.erates.repository.model.dbo.BankDbo;

/**
 * @author Kastalski Sergey
 */
public interface BanksDAO extends JpaRepository<BankDbo, Long> {
	
	@Query("select b from BankDbo b where b.code = :code")
	BankDbo findByCode(@Param("code") String code);
	
}
