package com.evanzeimet.wirecucumber.scenario.mocks.verification;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class NoOpMockInvocationsVerifierTest {

	private String givenMockName;
	private NoOpMockInvocationsVerifier verifier;

	@Before
	public void setUp() {
		givenMockName = "given-mock-name";
		verifier = new NoOpMockInvocationsVerifier(givenMockName);
	}

	@Test
	public void createSkippingVerifyMessage() {
		String actual = verifier.createSkippingVerifyMessage();

		String expected = "Skipping given-mock-name mock verification due to wire-cucumber being disabled";

		assertEquals(expected, actual);
	}

}
