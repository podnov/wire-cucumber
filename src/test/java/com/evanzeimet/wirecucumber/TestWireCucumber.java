package com.evanzeimet.wirecucumber;

import com.evanzeimet.wirecucumber.scenario.TestStepDefinitions;

public class TestWireCucumber
		extends WireCucumber {

	public TestWireCucumber() {
		super();
	}

	public TestWireCucumber(WireCucumberOptions options) {
		super(options);
	}

	protected TestStepDefinitions getTestSteps() {
		return new TestStepDefinitions(stepDefinitions);
	}

	public void verifyInvocations() throws Throwable {
		getTestSteps().verifyInvocations().accept();
	}

}
