package com.borealis.erates.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.borealis.erates.repository.model.dbo.ExchangeRateDbo;

/**
 * @author Kastalski Sergey
 */
public interface ExchangeRatesDAO extends JpaRepository<ExchangeRateDbo, Long> {
	
	@Query("select r.updateDate from ExchangeRateDbo r inner join r.bank b where b.code = :bankCode order by r.updateDate desc")
	List<LocalDateTime> findDateTimeListByBankCode(@Param("bankCode") String bankCode, final Pageable pageable);
	
}
