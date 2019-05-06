package com.borealis.erates.supplier.prior;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.borealis.erates.supplier.Bank;
import com.borealis.erates.supplier.BankProcessor;
import com.borealis.erates.supplier.exception.RatesProcessingException;

/**
 * @author Kastalski Sergey
 */
@Component
public class PriorBank implements Bank, BankProcessor {
	
	private String name;
	
	private String code;
	
	public PriorBank(
			@Value("${bank.prior.name}") final String bankName,
			@Value("${bank.prior.code}") final String bankCode) {
		this.name = bankName;
		this.code = bankCode;
	}
	
	@Override
	public String getBankName() {
		return name;
	}

	@Override
	public String getBankCode() {
		return code;
	}

	@Override
	public void process() throws RatesProcessingException {
		
	}
	
}
