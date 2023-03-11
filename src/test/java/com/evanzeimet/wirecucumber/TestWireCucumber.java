package com.evanzeimet.wirecucumber;

public class TestWireCucumber
		extends WireCucumber {

	public TestWireCucumber() {
		super();
	}

	public TestWireCucumber(WireCucumberOptions options) {
		super(options);
	}

	protected TestWireCucumberStepDefinitions getTestSteps() {
		return new TestWireCucumberStepDefinitions(stepDefinitions);
	}

	public void verifyInvocations() throws Throwable {
		getTestSteps().verifyInvocations().accept();
	}

}
