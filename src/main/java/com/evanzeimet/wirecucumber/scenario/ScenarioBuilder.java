package com.evanzeimet.wirecucumber.scenario;

import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.evanzeimet.wirecucumber.WireCucumberOptions;
import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.evanzeimet.wirecucumber.scenario.verification.MockInvocationsVerifier;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.Scenario;
import io.cucumber.java8.Status;

public class ScenarioBuilder {

	// TODO call index across mocks/scenarios? timestamp comparison?

	protected Scenario currentCucumberScenario;
	protected MockBuilder currentMockBuilder;
	protected String currentMockName;
	protected String currentMockState;
	protected Integer currentMockStateIndex;
	protected String currentVerifyMockName;
	protected MockInvocationsVerifier currentMockVerifier;
	protected Set<String> mockNames = new HashSet<String>();
	protected Map<MockStateKey, StubMapping> mockStateStubMappings = new HashMap<>();
	protected Map<MockStateKey, Integer> mockStateIndices = new HashMap<>();
	protected Set<String> verifiedMockNames = new HashSet<>();

	public MockBuilder getCurrentMockBuilder() {
		return currentMockBuilder;
	}

	public String getCurrentMockName() {
		return currentMockName;
	}

	public String getCurrentMockState() {
		return currentMockState;
	}

	public Integer getCurrentMockStateIndex() {
		return currentMockStateIndex;
	}

	public MockInvocationsVerifier getCurrentMockVerifier() {
		return currentMockVerifier;
	}

	public Set<String> getMockNames() {
		return mockNames;
	}

	public void addInvocationStateBodyAbsentVerification(String state) {
		Integer invocationIndex = getMockStateIndex(currentVerifyMockName, state);
		currentMockVerifier.addBodyAbsentVerification(invocationIndex);
	}

	public void addInvocationStateBodyEqualToVerification(String state,
			String requestBody) {
		Integer invocationIndex = getMockStateIndex(currentVerifyMockName, state);
		currentMockVerifier.addBodyEqualToVerification(invocationIndex, requestBody);
	}

	public void addInvocationStateBodyEqualToVerification(String state, DataTable dataTable) {
		Integer invocationIndex = getMockStateIndex(currentVerifyMockName, state);
		currentMockVerifier.addBodyEqualToVerification(invocationIndex, dataTable);
	}

	public void addInvocationStateHeaderAbsentVerification(String state, String headerName) {
		Integer invocationIndex = getMockStateIndex(currentVerifyMockName, state);
		currentMockVerifier.addHeaderAbsentVerification(invocationIndex, headerName);
	}

	public void addInvocationStateHeaderContainingVerification(String state, String headerName, String headerValue) {
		Integer invocationIndex = getMockStateIndex(currentVerifyMockName, state);
		currentMockVerifier.addHeaderContainingVerification(invocationIndex, headerName, headerValue);
	}

	public void addInvocationStateHeaderPresentVerification(String state, String headerName) {
		Integer invocationIndex = getMockStateIndex(currentVerifyMockName, state);
		currentMockVerifier.addHeaderPresentVerification(invocationIndex, headerName);
	}

	public void addInvocationStateUrlVerification(String state, String url) {
		Integer invocationIndex = getMockStateIndex(currentVerifyMockName, state);
		currentMockVerifier.addUrlVerification(invocationIndex, url);
	}

	protected void bootstrapMock(String mockName, String httpVerb, UrlPattern urlPattern) {
		boolean isCurrentMockUnfinalized = getIsCurrentMockUnfinalized();

		if (isCurrentMockUnfinalized) {
			String message = String.format("There was an attempt to bootstrap a new mock while mock [%s] remains unfinalized", currentMockName);
			throw new WireCucumberRuntimeException(message);
		}

		currentMockName = mockName;
		mockNames.add(mockName);
		currentMockState = STARTED;
		currentMockStateIndex = 0;
		currentMockBuilder = MockBuilder.create(currentCucumberScenario, httpVerb, urlPattern);
	}

	public void bootstrapMockWithUrlEqualTo(String mockName, String httpVerb, String path) {
		UrlPattern urlPattern = urlEqualTo(path);
		bootstrapMock(mockName, httpVerb, urlPattern);
	}

	public void bootstrapMockWithUrlMatching(String mockName, String httpVerb, String path) {
		UrlPattern urlPattern = urlMatching(path);
		bootstrapMock(mockName, httpVerb, urlPattern);
	}

	public void bootstrapMockWithUrlPathEqualTo(String mockName, String httpVerb, String path) {
		UrlPattern urlPattern = urlPathEqualTo(path);
		bootstrapMock(mockName, httpVerb, urlPattern);
	}

	public void bootstrapMockWithUrlPathMatching(String mockName, String httpVerb, String path) {
		UrlPattern urlPattern = urlPathMatching(path);
		bootstrapMock(mockName, httpVerb, urlPattern);
	}

	public void closeScenario(WireCucumberOptions options) {
		Status scenarioStatus = currentCucumberScenario.getStatus();
		if (Status.PASSED.equals(scenarioStatus)) {
			if (options.getRequireMockFinalization()) {
				verifyMocksFinalized();
			}

			if (options.getRequireMockInteractionsVerification()) {
				verifyMockInteractionsVerified();
			}
		}
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

	public void finalizeMock() {
		validateMockStateUnused();

		StubMapping stubMapping = currentMockBuilder.finalizeMock(currentMockState);
		putCurrentMockStateStubMapping(stubMapping);

		currentMockName = null;
		currentMockState = null;
		currentMockStateIndex = -1;
	}

	protected boolean getIsCurrentMockUnfinalized() {
		return (currentMockName != null);
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

	/**
	 * This can be used to override internal mock verification tracking in order to
	 * consider all mocks verified.
	 */
	public void setAllMocksVerified() {
		for (String mockName : mockNames) {
			verifiedMockNames.add(mockName);
		}
	}

	public void setCurrentCucumberScenario(Scenario scenario) {
		currentCucumberScenario = scenario;
	}

	public void setCurrentMockVerified() {
		verifiedMockNames.add(currentVerifyMockName);
	}

	public void setMockToBeVerified(String mockName) {
		currentVerifyMockName = mockName;
		StubMapping stubMapping = getMockStateMapping(mockName, STARTED);

		if (stubMapping == null) {
			String message = String.format("No mock found for name [%s]", mockName);
			throw new WireCucumberRuntimeException(message);
		}

		RequestPattern request = stubMapping.getRequest();
		currentMockVerifier = MockInvocationsVerifier.forRequestPattern(request);
	}

	public void transitionMockState(String nextState) {
		StubMapping stubMapping = currentMockBuilder.setRequestBuilderState(currentMockState, nextState);
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

	public void verifyMockInvocations() {
		currentMockVerifier.verify();
		currentMockVerifier = null;

		setCurrentMockVerified();
		currentVerifyMockName = null;
	}

	protected void verifyMocksFinalized() {
		boolean isCurrentMockUnfinalized = getIsCurrentMockUnfinalized();

		if (isCurrentMockUnfinalized) {
			String message = String.format("Found unfinalized mock [%s]", currentMockName);
			throw new WireCucumberRuntimeException(message);
		}
	}

	protected void verifyMockInteractionsVerified() {
		Set<String> unverifiedMocks = new HashSet<>(mockNames);
		unverifiedMocks.removeAll(verifiedMockNames);

		int unverifiedMockCount = unverifiedMocks.size();

		if (unverifiedMockCount > 0) {
			String message = String.format("Found [%s] unverified mocks %s",
					unverifiedMockCount,
					unverifiedMocks);
			throw new WireCucumberRuntimeException(message);
		}
	}

}
