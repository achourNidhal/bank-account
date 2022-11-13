package com.socgen.exceptions;

public class InsufficientFundException extends RuntimeException {

	private static final long serialVersionUID = -2069461047031526909L;

	private static final String message = "Insufficient balance";

	public InsufficientFundException() {
		super(message);
	}

}
