package com.evanzeimet.wirecucumber;

public class TestWireCucumber
		extends WireCucumber {

	public void verifyRequest() throws Throwable {
		steps.verifyInvocations().accept();
	}
}
