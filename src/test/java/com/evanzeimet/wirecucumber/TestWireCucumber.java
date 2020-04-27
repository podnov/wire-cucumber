package com.evanzeimet.wirecucumber;

import com.evanzeimet.wirecucumber.scenario.TestSteps;

public class TestWireCucumber
		extends WireCucumber {

	protected TestSteps getTestSteps() {
		return new TestSteps(steps);
	}

	public void verifyInvocations() throws Throwable {
		getTestSteps().verifyInvocations().accept();
	}

}
