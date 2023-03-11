package com.evanzeimet.wirecucumber;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.core.Options;

public class WireCucumberOptions {

	private boolean isDisabled = false;
	private boolean requireMockInvocationsVerification = true;
	private Options wireMockOptions = options();
	private boolean requireMockFinalization = true;

	public boolean getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public boolean getRequireMockFinalization() {
		return requireMockFinalization;
	}

	public void setRequireMockFinalization(boolean requireMockFinalization) {
		this.requireMockFinalization = requireMockFinalization;
	}

	public boolean getRequireMockInvocationsVerification() {
		return requireMockInvocationsVerification;
	}

	public void setRequireMockInvocationsVerification(boolean requireMockInvocationsVerification) {
		this.requireMockInvocationsVerification = requireMockInvocationsVerification;
	}

	public Options getWireMockOptions() {
		return wireMockOptions;
	}

	public void setWireMockOptions(Options wireMockOptions) {
		this.wireMockOptions = wireMockOptions;
	}


}
