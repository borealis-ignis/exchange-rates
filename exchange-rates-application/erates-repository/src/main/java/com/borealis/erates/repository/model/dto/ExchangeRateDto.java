package com.borealis.erates.repository.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Kastalski Sergey
 */
@Getter
@Setter
public class ExchangeRateDto {
	
	private Long id;
	
	private BigDecimal buyRate;
	
	private BigDecimal sellRate;
	
	private CurrencyDto currency;
	
	private BankDto bank;
	
	private LocalDateTime updateDate;
	
}
