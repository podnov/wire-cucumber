package com.evanzeimet.wirecucumber.scenario;

import com.evanzeimet.wirecucumber.WireCucumberOptions;
import com.evanzeimet.wirecucumber.scenario.mocks.builder.MocksBuilder;
import com.evanzeimet.wirecucumber.scenario.mocks.verification.MocksInvocationsVerifier;

import io.cucumber.java8.Scenario;
import io.cucumber.java8.Status;

public class ScenarioBuilder {

	protected ScenarioContext context;
	protected MocksBuilder mocksBuilder;
	protected MocksInvocationsVerifier mocksVerifier;

	public ScenarioBuilder() {
		this.context = new ScenarioContext();
		this.mocksBuilder = new MocksBuilder(context);
		this.mocksVerifier = new MocksInvocationsVerifier(context);
	}

	public MocksBuilder getMocksBuilder() {
		return mocksBuilder;
	}

	public MocksInvocationsVerifier getMocksInvocationsVerifier() {
		return mocksVerifier;
	}

	public void closeScenario(WireCucumberOptions options) {
		Status scenarioStatus = context.getCurrentScenario().getStatus();

		if (Status.PASSED.equals(scenarioStatus)) {
			verifyMocksFinalized(options);
			verifyMockInvocationsVerified(options);
		}
	}

	public void setCurrentCucumberScenario(Scenario scenario) {
		context.setCurrentScenario(scenario);
	}

	protected void verifyMockInvocationsVerified(WireCucumberOptions options) {
		boolean isDisabled = options.getIsDisabled();

		boolean requireMockInvocationsVerification = options.getRequireMockInvocationsVerification();
		boolean verifyMockInvocations = (!isDisabled && requireMockInvocationsVerification);

		if (verifyMockInvocations) {
			mocksVerifier.verifyMockInvocationsVerified();
		}
	}

	protected void verifyMocksFinalized(WireCucumberOptions options) {
		boolean isDisabled = options.getIsDisabled();

		boolean requireMockFinalization = options.getRequireMockFinalization();
		boolean verifyMocksFinalized = (!isDisabled && requireMockFinalization);

		if (verifyMocksFinalized) {
			mocksBuilder.verifyMocksFinalized();
		}
	}

}
