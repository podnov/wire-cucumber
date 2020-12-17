package com.evanzeimet.wirecucumber.scenario;

import io.cucumber.java8.StepdefBody.A0;

public class TestSteps {

	private Steps steps;

	public TestSteps(Steps steps) {
		this.steps = steps;
	}

	public Steps getSteps() {
		return steps;
	}

	public void closeScenario() {
		steps.closeScenario();
	}

	public A0 verifyMockInvocations() {
		return steps.verifyMockInvocations();
	}

}
