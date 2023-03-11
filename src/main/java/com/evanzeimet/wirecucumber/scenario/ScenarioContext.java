package com.evanzeimet.wirecucumber.scenario;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.evanzeimet.wirecucumber.scenario.mocks.MockStateKey;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import io.cucumber.java8.Scenario;

public class ScenarioContext {

	protected Scenario currentScenario;
	protected Set<String> mockNames = new HashSet<>();
	protected Map<MockStateKey, Integer> mockStateIndices = new HashMap<>();
	protected Map<MockStateKey, StubMapping> mockStateStubMappings = new HashMap<>();

	public Scenario getCurrentScenario() {
		return currentScenario;
	}

	public void setCurrentScenario(Scenario scenario) {
		currentScenario = scenario;
	}

	public void addMockName(String mockName) {
		mockNames.add(mockName);
	}

	public boolean containsMockState(String mockName, String state) {
		MockStateKey key = new MockStateKey(mockName, state);
		return containsMockState(key);
	}

	public boolean containsMockState(MockStateKey key) {
		return mockStateStubMappings.containsKey(key);
	}

	public Set<String> getMockNames() {
		return new HashSet<>(mockNames);
	}

	public Integer getMockStateIndex(String mockName, String state) {
		MockStateKey key = new MockStateKey(mockName, state);
		return getMockStateIndex(key);
	}

	protected Integer getMockStateIndex(MockStateKey key) {
		Integer result = mockStateIndices.get(key);

		if (result == null) {
			String message = String.format("Index for mock [%s] and state [%s] not found",
					key.getMockName(),
					key.getScenarioState());
			throw new WireCucumberRuntimeException(message);
		}

		return result;
	}

	public StubMapping getMockStateMapping(String mockName, String state) {
		MockStateKey key = new MockStateKey(mockName, state);
		return getMockStateMapping(key);
	}

	protected StubMapping getMockStateMapping(MockStateKey key) {
		return mockStateStubMappings.get(key);
	}

	public void putMockState(MockStateKey key, StubMapping stubMapping, Integer index) {
		mockStateStubMappings.put(key, stubMapping);
		mockStateIndices.put(key, index);
	}

}
