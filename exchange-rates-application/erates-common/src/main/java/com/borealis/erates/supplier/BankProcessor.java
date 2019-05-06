package com.borealis.erates.supplier;

import com.borealis.erates.supplier.exception.RatesProcessingException;

/**
 * @author Kastalski Sergey
 */
@FunctionalInterface
public interface BankProcessor {
	
	void process() throws RatesProcessingException;
	
}
