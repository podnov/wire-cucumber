package com.evanzeimet.wirecucumber.scenario.mocks.verification;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.evanzeimet.wirecucumber.scenario.ScenarioContext;

public class MocksInvocationsVerifierTest {

	private ScenarioContext givenContext;

	private MocksInvocationsVerifier verifier;

	@Before
	public void setUp() {
		givenContext = spy(new ScenarioContext());

		verifier = new MocksInvocationsVerifier(givenContext);
	}

	@Test
	public void bootstrapVerifier_nameNotFound() throws Throwable {
		String givenName = "given-name";
		// steps.stateMocks = Collections.emptyMap();

		WireCucumberRuntimeException actualException = null;

		try {
			verifier.bootstrapVerifier(givenName);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "No mock found for name [given-name]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void setAllMocksVerified() {
		Set<String> givenMockNames = new HashSet<>(Arrays.asList("given-mock-name-1",
				"given-mock-name-2",
				"given-mock-name-3"));
		Set<String> givenVerifiedMockNames = new HashSet<>();

		doReturn(givenMockNames).when(givenContext).getMockNames();

		verifier.verifiedMockNames = givenVerifiedMockNames;

		verifier.setAllMocksVerified();

		Set<String> actual = givenVerifiedMockNames;
		Set<String> expected = givenMockNames;

		assertEquals(expected, actual);
	}

	@Test
	public void verifyCurrentMockInvocations() throws Throwable {
		String givenCurrentMockName = "given-current-mock-name";
		verifier.currentMockName = givenCurrentMockName;

		MockInvocationsVerifier givenMockInvocationsVerifier = mock(MockInvocationsVerifier.class);
		verifier.mockInvocationsVerifier = givenMockInvocationsVerifier;

		verifier.verifyCurrentMockInvocations();

		assertNull(verifier.currentMockName);
		assertNull(verifier.mockInvocationsVerifier);

		assertThat(verifier.verifiedMockNames, hasItem(givenCurrentMockName));

		verify(givenMockInvocationsVerifier).verify();
	}

	@Test
	public void verifyMockInvocationsVerified_unverifiedMocks_empty() {
		Set<String> givenMockNames = new HashSet<String>(Arrays.asList("a", "b", "c"));
		doReturn(givenMockNames).when(givenContext).getMockNames();

		verifier.verifiedMockNames = givenMockNames;

		WireCucumberRuntimeException actualException = null;

		try {
			verifier.verifyMockInvocationsVerified();
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void verifyMockInvocationsVerified_unverifiedMocks_notEmpty() {
		Set<String> givenMockNames = new HashSet<String>(Arrays.asList("a", "b", "c", "d"));
		Set<String> givenVerifiedMockNames = new HashSet<String>(Arrays.asList("a", "c"));

		doReturn(givenMockNames).when(givenContext).getMockNames();

		verifier.verifiedMockNames = givenVerifiedMockNames;

		WireCucumberRuntimeException actualException = null;

		try {
			verifier.verifyMockInvocationsVerified();
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Found [2] unverified mocks [b, d]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

}
