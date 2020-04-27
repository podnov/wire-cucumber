package com.evanzeimet.wirecucumber.scenario;

import io.cucumber.java8.StepdefBody.A0;

public class TestSteps {

	private Steps steps;

	public TestSteps(Steps steps) {
		this.steps = steps;
	}

	public A0 verifyInvocations() {
		return this.steps.verifyInvocations();
	}

}
