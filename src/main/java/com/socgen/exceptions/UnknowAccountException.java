package com.socgen.exceptions;

public class UnknowAccountException extends RuntimeException {

	private static final long serialVersionUID = -2069461047031526909L;

	private static final String message = "Unknow operation type";

	public UnknowAccountException() {
		super(message);
	}

}
