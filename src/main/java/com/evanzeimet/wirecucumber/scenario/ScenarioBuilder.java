package com.evanzeimet.wirecucumber.scenario;

import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;

import java.util.HashMap;
import java.util.Map;

import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.evanzeimet.wirecucumber.scenario.verification.InvocationsVerifier;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import io.cucumber.core.api.Scenario;
import io.cucumber.datatable.DataTable;

public class ScenarioBuilder {

	// TODO call index across mocks/scenarios? timestamp comparison?

	protected Scenario currentCucumberScenario;
	protected String currentMockName;
	protected String currentMockState;
	protected Integer currentMockStateIndex;
	protected String currentVerifyMockName;
	protected InvocationsVerifier invocationsVerifier;
	protected MockBuilder mockBuilder;
	protected Map<MockStateKey, StubMapping> mockStateStubMappings = new HashMap<>();
	protected Map<MockStateKey, Integer> mockStateIndices = new HashMap<>();

	public String getCurrentMockName() {
		return currentMockName;
	}

	public String getCurrentScenarioState() {
		return currentMockState;
	}

	public Integer getCurrentScenarioStateIndex() {
		return currentMockStateIndex;
	}

	public InvocationsVerifier getInvocationVerifier() {
		return invocationsVerifier;
	}

	public MockBuilder getMockBuilder() {
		return mockBuilder;
	}

	public void addInvocationStateDataTableBodyVerification(String state, DataTable dataTable) {
		Integer invocationIndex = getMockStateIndex(currentVerifyMockName, state);
		invocationsVerifier.addDataTableBodyVerification(invocationIndex, dataTable);
	}

	public void addInvocationStateEmptyBodyVerification(String state) {
		Integer invocationIndex = getMockStateIndex(currentVerifyMockName, state);
		invocationsVerifier.addEmptyBodyVerification(invocationIndex);
	}

	public void addInvocationStateStringBodyVerification(String state,
			String requestBody) {
		Integer invocationIndex = getMockStateIndex(currentVerifyMockName, state);
		invocationsVerifier.addStringBodyVerification(invocationIndex, requestBody);
	}

	public void addInvocationStateUrlVerification(String state, String url) {
		Integer invocationIndex = getMockStateIndex(currentVerifyMockName, state);
		invocationsVerifier.addUrlVerification(invocationIndex, url);
	}

	protected void bootstrapRequestMock(String mockName, String httpVerb, UrlPattern urlPattern) {
		currentMockName = mockName;
		currentMockState = STARTED;
		currentMockStateIndex = 0;
		mockBuilder = MockBuilder.create(currentCucumberScenario, httpVerb, urlPattern);
	}

	public void bootstrapUrlEqualToRequestMock(String mockName, String httpVerb, String path) {
		UrlPattern urlPattern = urlEqualTo(path);
		bootstrapRequestMock(mockName, httpVerb, urlPattern);
	}

	public void bootstrapUrlMatchingRequestMock(String mockName, String httpVerb, String path) {
		UrlPattern urlPattern = urlMatching(path);
		bootstrapRequestMock(mockName, httpVerb, urlPattern);
	}

	public void bootstrapUrlPathEqualToRequestMock(String mockName, String httpVerb, String path) {
		UrlPattern urlPattern = urlPathEqualTo(path);
		bootstrapRequestMock(mockName, httpVerb, urlPattern);
	}

	public void bootstrapUrlPathMatchingRequestMock(String mockName, String httpVerb, String path) {
		UrlPattern urlPattern = urlPathMatching(path);
		bootstrapRequestMock(mockName, httpVerb, urlPattern);
	}

	public boolean containsMockState(String mockName, String state) {
		MockStateKey key = new MockStateKey(mockName, state);
		return containsMockState(key);
	}

	public boolean containsMockState(MockStateKey key) {
		return mockStateStubMappings.containsKey(key);
	}

	protected MockStateKey createCurrentMockStateKey() {
		validateMockStateConfigured();
		return new MockStateKey(currentMockName, currentMockState);
	}

	public void finalizeRequestMock() {
		validateMockStateUnused();

		StubMapping stubMapping = mockBuilder.finalizeMock(currentMockState);
		putCurrentMockStateStubMapping(stubMapping);

		currentMockName = null;
		currentMockState = null;
		currentMockStateIndex = -1;
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

	protected void putCurrentMockStateStubMapping(StubMapping stubMapping) {
		MockStateKey key = validateMockStateUnused();
		mockStateStubMappings.put(key, stubMapping);
		mockStateIndices.put(key, currentMockStateIndex++);
	}

	public void setCurrentCucumberScenario(Scenario scenario) {
		currentCucumberScenario = scenario;
	}

	public void setCurrentRequestVerifyBuilder(String mockName) {
		currentVerifyMockName = mockName;
		StubMapping stubMapping = getMockStateMapping(mockName, STARTED);

		if (stubMapping == null) {
			String message = String.format("No mock found for name [%s]", mockName);
			throw new WireCucumberRuntimeException(message);
		}

		RequestPattern request = stubMapping.getRequest();
		invocationsVerifier = InvocationsVerifier.forRequestPattern(request);
	}

	public void transitionMock(String nextState) {
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

		if (mockStateStubMappings.containsKey(key)) {
			String message = String.format("Mock name [%s] and state [%s] already in use",
					key.getMockName(),
					key.getScenarioState());
			throw new WireCucumberRuntimeException(message);
		}

		return key;
	}

	public void verifyInvocations() {
		invocationsVerifier.verify();
		invocationsVerifier = null;
	}

}
