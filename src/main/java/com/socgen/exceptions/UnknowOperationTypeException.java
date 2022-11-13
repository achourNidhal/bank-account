package com.socgen.exceptions;

public class UnknowOperationTypeException extends RuntimeException {

	private static final long serialVersionUID = -2069461047031526909L;

	private static final String message = "Unknow account";

	public UnknowOperationTypeException() {
		super(message);
	}

}
