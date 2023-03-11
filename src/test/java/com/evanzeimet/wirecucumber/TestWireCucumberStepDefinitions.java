package com.evanzeimet.wirecucumber;

import io.cucumber.java8.StepDefinitionBody.A0;

public class TestWireCucumberStepDefinitions {

	private WireCucumberStepDefinitions steps;

	public TestWireCucumberStepDefinitions(WireCucumberStepDefinitions steps) {
		this.steps = steps;
	}

	public A0 verifyInvocations() {
		return steps.verifyMockInvocations();
	}

}
