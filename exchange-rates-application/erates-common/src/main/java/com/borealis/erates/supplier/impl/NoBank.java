package com.borealis.erates.supplier.impl;

import com.borealis.erates.supplier.Bank;

/**
 * @author Kastalski Sergey
 */
public class NoBank implements Bank {

	@Override
	public String getBankName() {
		return null;
	}

	@Override
	public String getBankCode() {
		return null;
	}
	
}
