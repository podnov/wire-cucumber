package com.evanzeimet.wirecucumber.scenario.mocks.verification;

import java.util.HashSet;
import java.util.Set;

import com.evanzeimet.wirecucumber.WireCucumberOptions;
import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.evanzeimet.wirecucumber.scenario.ScenarioContext;
import com.evanzeimet.wirecucumber.scenario.mocks.builder.MocksBuilder;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

public class MocksInvocationsVerifier {

	protected ScenarioContext context;
	protected String currentMockName;
	protected MockInvocationsVerifier mockInvocationsVerifier;
	protected WireCucumberOptions options;
	protected Set<String> verifiedMockNames = new HashSet<>();

	public MocksInvocationsVerifier(WireCucumberOptions options, ScenarioContext context) {
		this.options = options;
		this.context = context;
	}

	public MockInvocationsVerifier getMockInvocationsVerifier() {
		return mockInvocationsVerifier;
	}

	public void bootstrapVerifier(String mockName) {
		boolean isDisabled = options.getIsDisabled();

		if (isDisabled) {
			mockInvocationsVerifier = new NoOpMockInvocationsVerifier(mockName);
		} else {
			currentMockName = mockName;
			StubMapping stubMapping = context.getMockStateMapping(mockName, MocksBuilder.DEFAULT_MOCK_EXPECTED_SCENARIO_STATE);

			if (stubMapping == null) {
				String message = String.format("No mock found for name [%s]", mockName);
				throw new WireCucumberRuntimeException(message);
			}

			RequestPattern request = stubMapping.getRequest();
			mockInvocationsVerifier = DefaultMockInvocationsVerifier.forRequestPattern(context, mockName, request);
		}
	}

	/**
	 * This can be used to override internal mock verification tracking in order
	 * to consider all mocks verified.
	 */
	public void setAllMocksVerified() {
		for (String mockName : context.getMockNames()) {
			setMockVerified(mockName);
		}
	}

	public void setCurrentMockInvocationsVerified() {
		setMockVerified(currentMockName);
	}

	public void setMockVerified(String mockName) {
		verifiedMockNames.add(mockName);
	}

	public void verifyCurrentMockInvocations() {
		mockInvocationsVerifier.verify();
		mockInvocationsVerifier = null;

		setCurrentMockInvocationsVerified();
		currentMockName = null;
	}

	public void verifyMockInvocationsVerified() {
		Set<String> mockNames = context.getMockNames();
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
