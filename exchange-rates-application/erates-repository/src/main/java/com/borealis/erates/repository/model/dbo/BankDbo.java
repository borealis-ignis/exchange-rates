package com.borealis.erates.repository.model.dbo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Kastalski Sergey
 */
@Getter
@Setter
@Entity
@Table(name = "Banks")
public class BankDbo {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;
	
	@Column(name = "Code", unique = true)
	private String code;
	
	@Column(name = "Active")
	private Boolean active;
	
	@OneToMany(mappedBy = "bank", orphanRemoval = false, fetch = FetchType.LAZY)
	private List<ExchangeRateDbo> exchangeRates;
	
}
