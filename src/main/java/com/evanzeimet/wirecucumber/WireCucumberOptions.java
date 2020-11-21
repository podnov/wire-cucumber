package com.evanzeimet.wirecucumber;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.core.Options;

public class WireCucumberOptions {

	private boolean requireMockInteractionsVerification = true;
	private Options wireMockOptions = options();

	public boolean getRequireMockInteractionsVerification() {
		return requireMockInteractionsVerification;
	}

	public void setRequireMockInteractionsVerification(boolean requireMockInteractionsVerification) {
		this.requireMockInteractionsVerification = requireMockInteractionsVerification;
	}

	public Options getWireMockOptions() {
		return wireMockOptions;
	}

	public void setWireMockOptions(Options wireMockOptions) {
		this.wireMockOptions = wireMockOptions;
	}


}
