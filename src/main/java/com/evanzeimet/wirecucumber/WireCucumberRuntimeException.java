package com.evanzeimet.wirecucumber;

public class WireCucumberRuntimeException
		extends RuntimeException {

	private static final long serialVersionUID = -2113855321921961338L;

	public WireCucumberRuntimeException() {
		super();
	}

	public WireCucumberRuntimeException(String message) {
		super(message);
	}

	public WireCucumberRuntimeException(Throwable t) {
		super(t);
	}

	public WireCucumberRuntimeException(String message, Throwable t) {
		super(message, t);
	}

}
