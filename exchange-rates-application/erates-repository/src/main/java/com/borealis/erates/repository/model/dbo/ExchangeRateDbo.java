package com.borealis.erates.repository.model.dbo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Kastalski Sergey
 */
@Getter
@Setter
@Entity
@Table(name = "ExchangeRates")
public class ExchangeRateDbo {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;
	
	@Column(name = "BuyRate")
	private BigDecimal buyRate;
	
	@Column(name = "SellRate")
	private BigDecimal sellRate;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CurrencyId", nullable = false)
	private CurrencyDbo currency;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BankId", nullable = false)
	private BankDbo bank;
	
	@Column(name = "UpdateDate")
	private LocalDateTime updateDate;
	
}
