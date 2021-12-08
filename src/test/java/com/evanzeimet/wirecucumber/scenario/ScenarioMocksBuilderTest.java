package com.evanzeimet.wirecucumber.scenario;

import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.cucumber.java8.Status.PASSED;
import static io.cucumber.java8.Status.UNDEFINED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import io.cucumber.java8.Scenario;

public class ScenarioMocksBuilderTest {

	private ScenarioMocksBuilder builder;

	@Before
	public void setUp() {
		builder = spy(new ScenarioMocksBuilder());
	}

	@Test
	public void bootstrapMock_existingMockUnfinalized() {
		String givenUnfinalizedMockName = "given-unfinalized-mock-name";

		String givenNewMockName = "given-new-mock-name";
		String givenNewHttpVerb = "given-http-verb";
		UrlPattern givenNewPattern = urlEqualTo("given-url-pattern");

		builder.currentMockName = givenUnfinalizedMockName;

		WireCucumberRuntimeException actual = null;

		try {
			builder.bootstrapMock(givenNewMockName, givenNewHttpVerb, givenNewPattern);
		} catch (WireCucumberRuntimeException e) {
			actual = e;
		}

		assertNotNull(actual);

		String actualExceptionMessage = actual.getMessage();
		String expectedExceptionMessage = "There was an attempt to bootstrap a new mock while mock [given-unfinalized-mock-name] remains unfinalized";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void closeScenario_isDisabled_true_currentMockUnfinalized() {
		String givenCurrentMockName = "given-current-mock-name";
		boolean givenIsDisabled = true;
		boolean givenRequireMockFinalization = true;
		boolean givenRequireMockInvocationsVerification = true;

		Scenario givenCurrentCucumberScenario = mock(Scenario.class);
		doReturn(PASSED).when(givenCurrentCucumberScenario).getStatus();

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setIsDisabled(givenIsDisabled);
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInvocationsVerification(givenRequireMockInvocationsVerification);

		builder.currentCucumberScenario = givenCurrentCucumberScenario;
		builder.currentMockName = givenCurrentMockName;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario(givenOptions);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void closeScenario_isDisabled_true_currentMockInvocationsUnverified() {
		String givenCurrentMockName = null;
		boolean givenIsDisabled = true;
		boolean givenRequireMockFinalization = true;
		boolean givenRequireMockInvocationsVerification = true;

		Set<String> givenMockNames = new HashSet<>() {

			private static final long serialVersionUID = -8408162546126661141L;

			{
				add("given-mock-name-1");
				add("given-mock-name-2");
				add("given-mock-name-3");
			}

		};

		Scenario givenCurrentCucumberScenario = mock(Scenario.class);
		doReturn(PASSED).when(givenCurrentCucumberScenario).getStatus();

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setIsDisabled(givenIsDisabled);
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInvocationsVerification(givenRequireMockInvocationsVerification);

		builder.currentCucumberScenario = givenCurrentCucumberScenario;
		builder.currentMockName = givenCurrentMockName;
		builder.mockNames = givenMockNames;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario(givenOptions);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void closeScenario_requireMockFinalization_false_currentMockFinalized() {
		String givenCurrentMockName = null;
		boolean givenRequireMockFinalization = false;
		boolean givenRequireMockInvocationsVerification = false;

		Scenario givenCurrentCucumberScenario = mock(Scenario.class);
		doReturn(PASSED).when(givenCurrentCucumberScenario).getStatus();

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInvocationsVerification(givenRequireMockInvocationsVerification);

		builder.currentCucumberScenario = givenCurrentCucumberScenario;
		builder.currentMockName = givenCurrentMockName;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario(givenOptions);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void closeScenario_requireMockFinalization_false_currentMockUnfinalized() {
		String givenCurrentMockName = "given-current-mock-name";
		boolean givenRequireMockFinalization = false;
		boolean givenRequireMockInvocationsVerification = false;

		Scenario givenCurrentCucumberScenario = mock(Scenario.class);
		doReturn(PASSED).when(givenCurrentCucumberScenario).getStatus();

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInvocationsVerification(givenRequireMockInvocationsVerification);

		builder.currentCucumberScenario = givenCurrentCucumberScenario;
		builder.currentMockName = givenCurrentMockName;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario(givenOptions);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void closeScenario_requireMockFinalization_true_currentMockFinalized() {
		String givenCurrentMockName = null;
		boolean givenRequireMockFinalization = true;
		boolean givenRequireMockInvocationsVerification = false;

		Scenario givenCurrentCucumberScenario = mock(Scenario.class);
		doReturn(PASSED).when(givenCurrentCucumberScenario).getStatus();

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInvocationsVerification(givenRequireMockInvocationsVerification);

		builder.currentCucumberScenario = givenCurrentCucumberScenario;
		builder.currentMockName = givenCurrentMockName;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario(givenOptions);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void closeScenario_requireMockInvocationsVerification_false_unverifiedMocks_empty() {
		boolean givenRequireMockFinalization = false;
		boolean givenRequireMockInvocationsVerification = false;

		Scenario givenCurrentCucumberScenario = mock(Scenario.class);
		doReturn(PASSED).when(givenCurrentCucumberScenario).getStatus();

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInvocationsVerification(givenRequireMockInvocationsVerification);

		Set<String> givenMockNames = new HashSet<String>(Arrays.asList("a", "b", "c"));

		builder.currentCucumberScenario = givenCurrentCucumberScenario;
		builder.mockNames = givenMockNames;
		builder.invocationVerifiedMockNames = givenMockNames;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario(givenOptions);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void closeScenario_requireMockInvocationsVerification_false_unverifiedMocks_notEmpty() {
		boolean givenRequireMockFinalization = false;
		boolean givenRequireMockInvocationsVerification = false;

		Scenario givenCurrentCucumberScenario = mock(Scenario.class);
		doReturn(PASSED).when(givenCurrentCucumberScenario).getStatus();

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInvocationsVerification(givenRequireMockInvocationsVerification);

		builder.currentCucumberScenario = givenCurrentCucumberScenario;
		builder.mockNames = new HashSet<String>(Arrays.asList("a", "b", "c", "d"));
		builder.invocationVerifiedMockNames = new HashSet<String>(Arrays.asList("a", "c"));

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario(givenOptions);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void closeScenario_requireMockInvocationsVerification_true_unverifiedMocks_empty() {
		boolean givenRequireMockFinalization = false;
		boolean givenRequireMockInvocationsVerification = true;

		Scenario givenCurrentCucumberScenario = mock(Scenario.class);
		doReturn(PASSED).when(givenCurrentCucumberScenario).getStatus();

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInvocationsVerification(givenRequireMockInvocationsVerification);

		Set<String> givenMockNames = new HashSet<String>(Arrays.asList("a", "b", "c"));

		builder.currentCucumberScenario = givenCurrentCucumberScenario;
		builder.mockNames = givenMockNames;
		builder.invocationVerifiedMockNames = givenMockNames;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario(givenOptions);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void closeScenario_scenarioStatus_notPassed_requireMockFinalization_true_currentMockUnfinalized() {
		String givenCurrentMockName = "given-current-mock-name";
		boolean givenRequireMockFinalization = true;
		boolean givenRequireMockInvocationsVerification = false;

		Scenario givenCurrentCucumberScenario = mock(Scenario.class);
		doReturn(UNDEFINED).when(givenCurrentCucumberScenario).getStatus();

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInvocationsVerification(givenRequireMockInvocationsVerification);

		builder.currentCucumberScenario = givenCurrentCucumberScenario;
		builder.currentMockName = givenCurrentMockName;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario(givenOptions);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void
			closeScenario_scenarioStatus_notPassed_requireMockInvocationsVerification_true_unverifiedMocks_notEmpty() {
		boolean givenRequireMockFinalization = false;
		boolean givenRequireMockInvocationsVerification = true;

		Scenario givenCurrentCucumberScenario = mock(Scenario.class);
		doReturn(UNDEFINED).when(givenCurrentCucumberScenario).getStatus();

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInvocationsVerification(givenRequireMockInvocationsVerification);

		builder.currentCucumberScenario = givenCurrentCucumberScenario;
		builder.mockNames = new HashSet<String>(Arrays.asList("a", "b", "c", "d"));
		builder.invocationVerifiedMockNames = new HashSet<String>(Arrays.asList("a", "c"));

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario(givenOptions);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void closeScenario_scenarioStatus_passed_requireMockFinalization_true_currentMockUnfinalized() {
		String givenCurrentMockName = "given-current-mock-name";
		boolean givenRequireMockFinalization = true;
		boolean givenRequireMockInvocationsVerification = false;

		Scenario givenCurrentCucumberScenario = mock(Scenario.class);
		doReturn(PASSED).when(givenCurrentCucumberScenario).getStatus();

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInvocationsVerification(givenRequireMockInvocationsVerification);

		builder.currentCucumberScenario = givenCurrentCucumberScenario;
		builder.currentMockName = givenCurrentMockName;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario(givenOptions);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Found unfinalized mock [given-current-mock-name]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void closeScenario_scenarioStatus_passed_requireMockInvocationsVerification_true_unverifiedMocks_notEmpty() {
		boolean givenRequireMockFinalization = false;
		boolean givenRequireMockInvocationsVerification = true;

		Scenario givenCurrentCucumberScenario = mock(Scenario.class);
		doReturn(PASSED).when(givenCurrentCucumberScenario).getStatus();

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInvocationsVerification(givenRequireMockInvocationsVerification);

		builder.currentCucumberScenario = givenCurrentCucumberScenario;
		builder.mockNames = new HashSet<String>(Arrays.asList("a", "b", "c", "d"));
		builder.invocationVerifiedMockNames = new HashSet<String>(Arrays.asList("a", "c"));

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario(givenOptions);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Found [2] unverified mocks [b, d]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void contains_false() {
		String givenMockName = "given-mock-name";
		String givenState = "given-state";

		builder.mockStateStubMappings.clear();

		boolean actual = builder.containsMockState(givenMockName, givenState);

		assertFalse(actual);
	}

	@Test
	public void contains_true() {
		String givenMockName = "given-mock-name";
		String givenState = "given-state";
		StubMapping givenValue = mock(StubMapping.class);

		MockStateKey givenMockKey = new MockStateKey(givenMockName, givenState);
		builder.mockStateStubMappings.put(givenMockKey, givenValue);

		boolean actual = builder.containsMockState(givenMockName, givenState);

		assertTrue(actual);
	}

	@Test
	public void finalizeRequestMock_isDisabled_false() {
		boolean givenIsDisabled = false;
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";
		Integer givenCurrentMockStateIndex = 42;
		StubMapping givenStubMapping = mock(StubMapping.class);
		MockBuilder givenMockBuilder = spy(new MockBuilder());

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.currentMockStateIndex = givenCurrentMockStateIndex;
		builder.currentMockBuilder = givenMockBuilder;
		MockStateKey actualKey = builder.createCurrentMockStateKey();

		doReturn(givenStubMapping)
			.when(givenMockBuilder)
			.createStubForBuilders(anyString());

		builder.finalizeMock(givenIsDisabled);

		assertNull(builder.currentMockName);
		assertNull(builder.currentMockState);

		verify(givenMockBuilder).finalizeMock(givenCurrentMockState);

		Integer expectedCurrentMockStateIndex = -1;
		assertEquals(expectedCurrentMockStateIndex, builder.currentMockStateIndex);

		StubMapping actualStubMapping = builder.mockStateStubMappings.get(actualKey);
		assertEquals(givenStubMapping, actualStubMapping);

		Integer actualCurrentScenarioStateIndex = builder.mockStateIndices.get(actualKey);
		assertEquals(givenCurrentMockStateIndex++, actualCurrentScenarioStateIndex);
	}

	@Test
	public void finalizeRequestMock_isDisabled_true() {
		boolean givenIsDisabled = true;
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";
		Integer givenCurrentMockStateIndex = 42;
		StubMapping givenStubMapping = mock(StubMapping.class);
		MockBuilder givenMockBuilder = spy(new MockBuilder());

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.currentMockStateIndex = givenCurrentMockStateIndex;
		builder.currentMockBuilder = givenMockBuilder;
		MockStateKey actualKey = builder.createCurrentMockStateKey();

		doReturn(givenStubMapping)
				.when(givenMockBuilder)
				.createStubForBuilders(anyString());

		builder.finalizeMock(givenIsDisabled);

		assertNull(builder.currentMockName);
		assertNull(builder.currentMockState);

		verify(givenMockBuilder, never()).finalizeMock(givenCurrentMockState);

		Integer expectedCurrentMockStateIndex = -1;
		assertEquals(expectedCurrentMockStateIndex, builder.currentMockStateIndex);

		StubMapping actualStubMapping = builder.mockStateStubMappings.get(actualKey);
		assertNull(actualStubMapping);

		Integer actualCurrentScenarioStateIndex = builder.mockStateIndices.get(actualKey);
		assertNull(actualCurrentScenarioStateIndex);
	}

	@Test
	public void finalizeRequestMock_cacheNamedMock() throws Throwable {
		boolean givenIsDisabled = false;
		StubMapping givenStubmapping = mock(StubMapping.class);
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";
		MockBuilder givenMockBuilder = spy(new MockBuilder());

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.currentMockStateIndex = 42;
		builder.currentMockBuilder = givenMockBuilder;

		doReturn(givenStubmapping)
			.when(givenMockBuilder)
			.createStubForBuilders(anyString());

		builder.finalizeMock(givenIsDisabled);

		StubMapping actualStubMapping = builder.getMockStateMapping(givenCurrentMockName, givenCurrentMockState);

		assertNotNull(actualStubMapping);
	}

	@Test
	public void finalizeRequestMock_currentMockName_null() throws Throwable {
		boolean givenIsDisabled = false;
		String givenCurrentMockName = null;

		builder.currentMockName = givenCurrentMockName;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.finalizeMock(givenIsDisabled);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Mock name not set";
		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void finalizeRequestMock_currentMockName_used() throws Throwable {
		boolean givenIsDisabled = false;
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";
		String givenNextMockState = "given-next-mock-state";

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.currentMockStateIndex = 0;
		builder.currentMockBuilder = mock(MockBuilder.class);
		builder.transitionMockState(givenNextMockState);
		builder.putCurrentMockStateStubMapping(null);

		WireCucumberRuntimeException actualException = null;

		try {
			builder.finalizeMock(givenIsDisabled);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Mock name [given-current-mock-name] and state [given-next-mock-state] already in use";
		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void getStateIndex_notPresent() {
		String givenMockName = "given-mock-name";
		String givenState = "given-state";

		builder.mockStateIndices.clear();

		WireCucumberRuntimeException actual = null;

		try {
			builder.getMockStateIndex(givenMockName, givenState);
		} catch (WireCucumberRuntimeException e) {
			actual = e;
		}

		assertNotNull(actual);

		String actualMessage = actual.getMessage();
		String expectedMessage = "Index for mock [given-mock-name] and state [given-state] not found";

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void getStateIndex_present() {
		String givenMockName = "given-mock-name";
		String givenState = "given-state";
		Integer givenValue = 42;

		MockStateKey givenMockKey = new MockStateKey(givenMockName, givenState);
		builder.mockStateIndices.put(givenMockKey, givenValue);

		Integer actual = builder.getMockStateIndex(givenMockName, givenState);

		assertEquals(givenValue, actual);
	}

	@Test
	public void getStateMapping_notPresent() {
		String givenMockName = "given-mock-name";
		String givenState = "given-state";

		builder.mockStateStubMappings.clear();

		StubMapping actual = builder.getMockStateMapping(givenMockName, givenState);

		assertNull(actual);
	}

	@Test
	public void getStateMapping_present() {
		String givenMockName = "given-mock-name";
		String givenState = "given-state";
		StubMapping givenValue = mock(StubMapping.class);

		MockStateKey givenMockKey = new MockStateKey(givenMockName, givenState);
		builder.mockStateStubMappings.put(givenMockKey, givenValue);

		StubMapping actual = builder.getMockStateMapping(givenMockName, givenState);

		assertEquals(givenValue, actual);
	}

	@Test
	public void putCurrentScenarioStateStub() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";
		Integer givenCurrentMockStateIndex = 42;
		StubMapping givenStubMapping = mock(StubMapping.class);

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.currentMockStateIndex = givenCurrentMockStateIndex;

		builder.putCurrentMockStateStubMapping(givenStubMapping);

		MockStateKey actualKey = builder.createCurrentMockStateKey();

		StubMapping actualStubMapping = builder.mockStateStubMappings.get(actualKey);
		assertEquals(givenStubMapping, actualStubMapping);

		Integer actualStateIndex = builder.mockStateIndices.get(actualKey);
		Integer expectedStateIndex = givenCurrentMockStateIndex++;
		assertEquals(expectedStateIndex, actualStateIndex);
	}

	@Test
	public void putCurrentScenarioStateStub_duplicate() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";
		Integer givenCurrentMockStateIndex = 42;
		StubMapping givenStubMapping = mock(StubMapping.class);

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.currentMockStateIndex = givenCurrentMockStateIndex;

		MockStateKey actualKey = builder.createCurrentMockStateKey();
		builder.mockStateStubMappings.put(actualKey, givenStubMapping);

		WireCucumberRuntimeException actual = null;

		try {
			builder.putCurrentMockStateStubMapping(givenStubMapping);
		} catch (WireCucumberRuntimeException e) {
			actual = e;
		}

		assertNotNull(actual);

		String actualMessage = actual.getMessage();
		String expectedMessage =
				"Mock name [given-current-mock-name] and state [given-current-mock-state] already in use";

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void setAllMocksVerified() {
		Set<String> givenMockNames = new HashSet<>(Arrays.asList("given-mock-name-1",
				"given-mock-name-2",
				"given-mock-name-3"));
		Set<String> givenVerifiedMockNames = new HashSet<>();

		builder.mockNames = givenMockNames;
		builder.invocationVerifiedMockNames = givenVerifiedMockNames;

		builder.setAllMocksVerified();

		Set<String> actual = builder.invocationVerifiedMockNames;
		Set<String> expected = givenMockNames;

		assertEquals(expected, actual);
	}

	@Test
	public void transitionMock() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";
		Integer givenCurrentMockStateIndex = 42;
		String givenNextMockState = "given-next-state";
		StubMapping givenStubMapping = mock(StubMapping.class);
		MockBuilder givenMockBuilder = mock(MockBuilder.class);

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.currentMockStateIndex = givenCurrentMockStateIndex;
		builder.currentMockBuilder = givenMockBuilder;

		MockStateKey actualKey = builder.createCurrentMockStateKey();
		doReturn(givenStubMapping).when(givenMockBuilder).setRequestBuilderState(givenCurrentMockState,
				givenNextMockState);

		builder.transitionMockState(givenNextMockState);

		assertEquals(givenCurrentMockName, builder.currentMockName);
		Integer expectedCurrentScenarioStateIndex = givenCurrentMockStateIndex + 1;
		assertEquals(expectedCurrentScenarioStateIndex, builder.currentMockStateIndex);
		assertEquals(givenNextMockState, builder.currentMockState);

		StubMapping actualStubMapping = builder.mockStateStubMappings.get(actualKey);
		assertEquals(givenStubMapping, actualStubMapping);

		Integer actualStateIndex = builder.mockStateIndices.get(actualKey);
		Integer expectedStateIndex = givenCurrentMockStateIndex;
		assertEquals(expectedStateIndex, actualStateIndex);
	}

	@Test
	public void validateMockStateConfigured() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.mockStateStubMappings.clear();

		WireCucumberRuntimeException actual = null;

		try {
			builder.validateMockStateConfigured();
		} catch (WireCucumberRuntimeException e) {
			actual = e;
		}

		assertNull(actual);
	}

	@Test
	public void validateMockStateConfigured_nullMockName() {
		String givenCurrentMockName = null;
		String givenCurrentMockState = "given-current-mock-state";

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.mockStateStubMappings.clear();

		WireCucumberRuntimeException actual = null;

		try {
			builder.validateMockStateConfigured();
		} catch (WireCucumberRuntimeException e) {
			actual = e;
		}

		assertNotNull(actual);

		String actualMessage = actual.getMessage();
		String expectedMessage = "Mock name not set";

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void validateMockStateConfigured_nullMockState() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = null;

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.mockStateStubMappings.clear();

		WireCucumberRuntimeException actual = null;

		try {
			builder.validateMockStateConfigured();
		} catch (WireCucumberRuntimeException e) {
			actual = e;
		}

		assertNotNull(actual);

		String actualMessage = actual.getMessage();
		String expectedMessage = "Scenario state not set";

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void validateMockStateUnused_unused() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		MockStateKey givenKey = builder.createCurrentMockStateKey();
		builder.mockStateStubMappings.put(givenKey, mock(StubMapping.class));

		WireCucumberRuntimeException actual = null;

		try {
			builder.validateMockStateUnused();
		} catch (WireCucumberRuntimeException e) {
			actual = e;
		}

		assertNotNull(actual);

		String actualMessage = actual.getMessage();
		String expectedMessage =
				"Mock name [given-current-mock-name] and state [given-current-mock-state] already in use";

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void validateMockStateUnused_used() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.mockStateStubMappings.clear();

		WireCucumberRuntimeException actual = null;

		try {
			builder.validateMockStateUnused();
		} catch (WireCucumberRuntimeException e) {
			actual = e;
		}

		assertNull(actual);
	}

}
