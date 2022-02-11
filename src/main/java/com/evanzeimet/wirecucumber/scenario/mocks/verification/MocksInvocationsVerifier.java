package com.evanzeimet.wirecucumber.scenario.mocks.verification;

import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;

import java.util.HashSet;
import java.util.Set;

import com.evanzeimet.wirecucumber.WireCucumberOptions;
import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.evanzeimet.wirecucumber.scenario.ScenarioContext;
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

		if (!isDisabled) {
			currentMockName = mockName;
			StubMapping stubMapping = context.getMockStateMapping(mockName, STARTED);

			if (stubMapping == null) {
				String message = String.format("No mock found for name [%s]", mockName);
				throw new WireCucumberRuntimeException(message);
			}

			RequestPattern request = stubMapping.getRequest();
			mockInvocationsVerifier = MockInvocationsVerifier.forRequestPattern(context, mockName, request);
		}
	}

	/**
	 * This can be used to override internal mock verification tracking in order
	 * to consider all mocks verified.
	 */
	public void setAllMocksVerified() {
		boolean isDisabled = options.getIsDisabled();

		if (!isDisabled) {
			for (String mockName : context.getMockNames()) {
				verifiedMockNames.add(mockName);
			}
		}
	}

	public void setCurrentMockInvocationsVerified() {
		boolean isDisabled = options.getIsDisabled();

		if (!isDisabled) {
			verifiedMockNames.add(currentMockName);
		}
	}

	public void verifyCurrentMockInvocations() {
		boolean isDisabled = options.getIsDisabled();

		if (!isDisabled) {
			mockInvocationsVerifier.verify();
			mockInvocationsVerifier = null;

			setCurrentMockInvocationsVerified();
			currentMockName = null;
		}
	}

	public void verifyMockInvocationsVerified() {
		boolean isDisabled = options.getIsDisabled();

		if (!isDisabled) {
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

}
