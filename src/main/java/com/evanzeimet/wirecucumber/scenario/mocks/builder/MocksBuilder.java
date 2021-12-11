package com.evanzeimet.wirecucumber.scenario.mocks.builder;

import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;

import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.evanzeimet.wirecucumber.scenario.ScenarioContext;
import com.evanzeimet.wirecucumber.scenario.mocks.MockStateKey;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import io.cucumber.java8.Scenario;

public class MocksBuilder {

	protected ScenarioContext context;
	protected MockBuilder mockBuilder;
	protected Integer currentMockStateIndex;
	protected String currentMockName;
	protected String currentMockState;

	public MocksBuilder(ScenarioContext context) {
		this.context = context;
	}

	public MockBuilder mockBuilder() {
		return mockBuilder;
	}

	protected MockBuilder bootstrapMock(String mockName, String httpVerb, UrlPattern urlPattern) {
		boolean isCurrentMockUnfinalized = getIsCurrentMockUnfinalized();

		if (isCurrentMockUnfinalized) {
			String message = String.format("There was an attempt to bootstrap a new mock while mock [%s] remains unfinalized", currentMockName);
			throw new WireCucumberRuntimeException(message);
		}

		currentMockName = mockName;
		context.addMockName(mockName);
		currentMockState = STARTED;
		currentMockStateIndex = 0;

		Scenario currentScenario = context.getCurrentScenario();
		mockBuilder = MockBuilder.create(currentScenario, httpVerb, urlPattern);

		return mockBuilder;
	}

	public MockBuilder bootstrapMockWithUrlEqualTo(String mockName, String httpVerb, String path) {
		UrlPattern urlPattern = urlEqualTo(path);
		return bootstrapMock(mockName, httpVerb, urlPattern);
	}

	public MockBuilder bootstrapMockWithUrlMatching(String mockName, String httpVerb, String path) {
		UrlPattern urlPattern = urlMatching(path);
		return bootstrapMock(mockName, httpVerb, urlPattern);
	}

	public MockBuilder bootstrapMockWithUrlPathEqualTo(String mockName, String httpVerb, String path) {
		UrlPattern urlPattern = urlPathEqualTo(path);
		return bootstrapMock(mockName, httpVerb, urlPattern);
	}

	public MockBuilder bootstrapMockWithUrlPathMatching(String mockName, String httpVerb, String path) {
		UrlPattern urlPattern = urlPathMatching(path);
		return bootstrapMock(mockName, httpVerb, urlPattern);
	}

	protected MockStateKey createCurrentMockStateKey() {
		validateMockStateConfigured();
		return new MockStateKey(currentMockName, currentMockState);
	}

	protected boolean getIsCurrentMockUnfinalized() {
		return (currentMockName != null);
	}

	public void finalizeMock(boolean isDisabled) {
		validateMockStateUnused();

		if (!isDisabled) {
			StubMapping stubMapping = mockBuilder.finalizeMock(currentMockState);
			putCurrentMockStateStubMapping(stubMapping);
		}

		currentMockName = null;
		currentMockState = null;
		currentMockStateIndex = -1;
	}

	protected void putCurrentMockStateStubMapping(StubMapping stubMapping) {
		MockStateKey key = validateMockStateUnused();
		context.putMockState(key, stubMapping, currentMockStateIndex++);
	}

	public void transitionMockState(String nextState) {
		StubMapping stubMapping = mockBuilder.setRequestBuilderState(currentMockState, nextState);
		putCurrentMockStateStubMapping(stubMapping);
		currentMockState = nextState;
	}

	protected void validateMockStateConfigured() {
		if (currentMockName == null) {
			throw new WireCucumberRuntimeException("Mock name not set");
		}

		if (currentMockState == null) {
			throw new WireCucumberRuntimeException("Scenario state not set");
		}
	}

	protected MockStateKey validateMockStateUnused() {
		MockStateKey key = createCurrentMockStateKey();

		if (context.containsMockState(key)) {
			String message = String.format("Mock name [%s] and state [%s] already in use",
					key.getMockName(),
					key.getScenarioState());
			throw new WireCucumberRuntimeException(message);
		}

		return key;
	}

	public void verifyMocksFinalized() {
		boolean isCurrentMockUnfinalized = getIsCurrentMockUnfinalized();

		if (isCurrentMockUnfinalized) {
			String message = String.format("Found unfinalized mock [%s]", currentMockName);
			throw new WireCucumberRuntimeException(message);
		}
	}

}
