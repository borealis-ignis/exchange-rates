package com.borealis.erates.repository.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Kastalski Sergey
 */
@Getter
@Setter
public class BankDto {
	
	private Long id;
	
	private String code;
	
	private Boolean active;
	
}
