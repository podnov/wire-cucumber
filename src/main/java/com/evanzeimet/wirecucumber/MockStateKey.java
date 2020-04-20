package com.evanzeimet.wirecucumber;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MockStateKey {

	private String mockName;
	private String scenarioState;

	public MockStateKey() {

	}

	public MockStateKey(String mockName, String scenarioState) {
		this.mockName = mockName;
		this.scenarioState = scenarioState;
	}

	public String getMockName() {
		return mockName;
	}

	public void setMockName(String mockName) {
		this.mockName = mockName;
	}

	public String getScenarioState() {
		return scenarioState;
	}

	public void setScenarioState(String scenarioState) {
		this.scenarioState = scenarioState;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		MockStateKey rhs = (MockStateKey) obj;
		return new EqualsBuilder()
				.append(mockName, rhs.mockName)
				.append(scenarioState, rhs.scenarioState)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(mockName)
				.append(scenarioState)
				.toHashCode();
	}

}
