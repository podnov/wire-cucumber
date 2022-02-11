package com.evanzeimet.wirecucumber.scenario.mocks.verification;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.wirecucumber.WireCucumberOptions;
import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.evanzeimet.wirecucumber.scenario.ScenarioContext;

public class MocksInvocationsVerifierTest {

	private ScenarioContext givenContext;
	private WireCucumberOptions givenOptions;

	private MocksInvocationsVerifier verifier;

	@Before
	public void setUp() {
		givenContext = spy(new ScenarioContext());
		givenOptions = spy(new WireCucumberOptions());

		verifier = new MocksInvocationsVerifier(givenOptions, givenContext);
	}

	@Test
	public void bootstrapVerifier_nameNotFound_isDisabled_false() throws Throwable {
		String givenName = "given-name";
		boolean givenIsDisabled = false;

		doReturn(givenIsDisabled).when(givenOptions).getIsDisabled();

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
	public void bootstrapVerifier_nameNotFound_isDisabled_true() throws Throwable {
		String givenName = "given-name";
		boolean givenIsDisabled = true;

		doReturn(givenIsDisabled).when(givenOptions).getIsDisabled();

		WireCucumberRuntimeException actualException = null;

		try {
			verifier.bootstrapVerifier(givenName);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void setCurrentMockInvocationsVerified_isDisabled_false() {
		String givenCurrentMockName = "given-current-mock-name";
		boolean givenIsDisabled = false;

		verifier.currentMockName = givenCurrentMockName;
		doReturn(givenIsDisabled).when(givenOptions).getIsDisabled();

		verifier.setCurrentMockInvocationsVerified();

		assertThat(verifier.verifiedMockNames, contains(givenCurrentMockName));
	}

	@Test
	public void setCurrentMockInvocationsVerified_isDisabled_true() {
		String givenCurrentMockName = "given-current-mock-name";
		boolean givenIsDisabled = true;

		verifier.currentMockName = givenCurrentMockName;
		doReturn(givenIsDisabled).when(givenOptions).getIsDisabled();

		verifier.setCurrentMockInvocationsVerified();

		assertThat(verifier.verifiedMockNames, empty());
	}

	@Test
	public void setAllMocksVerified_isDisabled_false() {
		Set<String> givenMockNames = new HashSet<>(Arrays.asList("given-mock-name-1",
				"given-mock-name-2",
				"given-mock-name-3"));
		Set<String> givenVerifiedMockNames = new HashSet<>();
		boolean givenIsDisabled = false;

		doReturn(givenIsDisabled).when(givenOptions).getIsDisabled();
		doReturn(givenMockNames).when(givenContext).getMockNames();

		verifier.verifiedMockNames = givenVerifiedMockNames;

		verifier.setAllMocksVerified();

		Set<String> actual = verifier.verifiedMockNames;
		Set<String> expected = givenMockNames;

		assertEquals(expected, actual);
	}

	@Test
	public void setAllMocksVerified_isDisabled_true() {
		Set<String> givenMockNames = new HashSet<>(Arrays.asList("given-mock-name-1",
				"given-mock-name-2",
				"given-mock-name-3"));
		Set<String> givenVerifiedMockNames = new HashSet<>();
		boolean givenIsDisabled = true;

		doReturn(givenIsDisabled).when(givenOptions).getIsDisabled();
		doReturn(givenMockNames).when(givenContext).getMockNames();

		verifier.verifiedMockNames = givenVerifiedMockNames;

		verifier.setAllMocksVerified();

		Set<String> actual = verifier.verifiedMockNames;

		assertThat(actual, empty());
	}

	@Test
	public void verifyCurrentMockInvocations_isDisabled_false() throws Throwable {
		boolean givenIsDisabled = false;
		doReturn(givenIsDisabled).when(givenOptions).getIsDisabled();

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
	public void verifyCurrentMockInvocations_isDisabled_true() throws Throwable {
		boolean givenIsDisabled = true;
		doReturn(givenIsDisabled).when(givenOptions).getIsDisabled();

		String givenCurrentMockName = "given-current-mock-name";
		verifier.currentMockName = givenCurrentMockName;

		MockInvocationsVerifier givenMockInvocationsVerifier = mock(MockInvocationsVerifier.class);
		verifier.mockInvocationsVerifier = givenMockInvocationsVerifier;

		verifier.verifyCurrentMockInvocations();

		assertEquals(givenCurrentMockName, verifier.currentMockName);
		assertEquals(givenMockInvocationsVerifier, verifier.mockInvocationsVerifier);

		assertThat(verifier.verifiedMockNames, empty());

		verify(givenMockInvocationsVerifier, never()).verify();
	}

	@Test
	public void verifyMockInvocationsVerified_isDisabled_false_unverifiedMocks_empty() {
		boolean givenIsDisabled = false;
		Set<String> givenMockNames = new HashSet<String>(Arrays.asList("a", "b", "c"));

		doReturn(givenIsDisabled).when(givenOptions).getIsDisabled();
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
	public void verifyMockInvocationsVerified_isDisabled_false_unverifiedMocks_notEmpty() {
		boolean givenIsDisabled = false;
		Set<String> givenMockNames = new HashSet<String>(Arrays.asList("a", "b", "c", "d"));
		Set<String> givenVerifiedMockNames = new HashSet<String>(Arrays.asList("a", "c"));

		doReturn(givenIsDisabled).when(givenOptions).getIsDisabled();
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

	@Test
	public void verifyMockInvocationsVerified_isDisabled_true_unverifiedMocks_empty() {
		boolean givenIsDisabled = true;
		Set<String> givenMockNames = new HashSet<String>(Arrays.asList("a", "b", "c"));

		doReturn(givenIsDisabled).when(givenOptions).getIsDisabled();
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
	public void verifyMockInvocationsVerified_isDisabled_true_unverifiedMocks_notEmpty() {
		boolean givenIsDisabled = true;
		Set<String> givenMockNames = new HashSet<String>(Arrays.asList("a", "b", "c", "d"));
		Set<String> givenVerifiedMockNames = new HashSet<String>(Arrays.asList("a", "c"));

		doReturn(givenIsDisabled).when(givenOptions).getIsDisabled();
		doReturn(givenMockNames).when(givenContext).getMockNames();

		verifier.verifiedMockNames = givenVerifiedMockNames;

		WireCucumberRuntimeException actualException = null;

		try {
			verifier.verifyMockInvocationsVerified();
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

}
