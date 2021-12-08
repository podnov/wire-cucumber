package com.evanzeimet.wirecucumber.scenario;

import io.cucumber.java8.StepDefinitionBody.A0;

public class TestStepDefinitions {

	private StepDefinitions steps;

	public TestStepDefinitions(StepDefinitions steps) {
		this.steps = steps;
	}

	public A0 verifyInvocations() {
		return steps.verifyMockInvocations();
	}

}
