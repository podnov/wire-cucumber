package com.evanzeimet.wirecucumber;

import com.evanzeimet.wirecucumber.scenario.ScenarioBuilder;
import com.evanzeimet.wirecucumber.scenario.TestSteps;

public class TestWireCucumber
		extends WireCucumber {

	protected TestSteps getTestSteps() {
		return new TestSteps(steps);
	}

	public void closeScenario() {
		getTestSteps().closeScenario();
	}

	public void verifyMockInvocations() throws Throwable {
		getTestSteps().verifyMockInvocations().accept();
	}

	public void finalizeCurrentMockIfNecessary() {
		ScenarioBuilder scenarioBuilder = getTestSteps().getSteps().getScenarioBuilder();
		String currentMockName = scenarioBuilder.getCurrentMockName();

		if (currentMockName != null) {
			scenarioBuilder.finalizeMock();
		}
	}

}
