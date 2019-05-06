package com.borealis.erates.supplier.exception;

/**
 * @author Kastalski Sergey
 */
public class RatesProcessingException extends Exception {
	
	private static final long serialVersionUID = -8776009942063536522L;
	
	public RatesProcessingException(final String message) {
		super(message);
	}
	
	public RatesProcessingException(final String message, final Exception e) {
		super(message, e);
	}
	
}
