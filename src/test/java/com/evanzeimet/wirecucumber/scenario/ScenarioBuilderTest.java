package com.evanzeimet.wirecucumber.scenario;

import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.wirecucumber.WireCucumberOptions;
import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

public class ScenarioBuilderTest {

	private ScenarioBuilder builder;

	@Before
	public void setUp() {
		builder = spy(new ScenarioBuilder(new WireCucumberOptions()));
	}

	@Test
	public void bootstrapMock_nullMockName() throws Throwable {
		String givenMockName = null;
		String givenHttpVerb = "given-http-verb";
		UrlPattern givenUrlPattern = urlEqualTo("given-url-pattern");

		WireCucumberRuntimeException actualException = null;

		try {
			builder.bootstrapMock(givenMockName, givenHttpVerb, givenUrlPattern);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Mock name cannot be null";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
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
	public void closeScenario_requireMockFinalization_false_currentMockFinalized() {
		String givenCurrentMockName = null;
		boolean givenRequireMockFinalization = false;
		boolean givenRequireMockInteractionsVerification = false;

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInteractionsVerification(givenRequireMockInteractionsVerification);

		builder.currentMockName = givenCurrentMockName;
		builder.options = givenOptions;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario();
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void closeScenario_requireMockFinalization_false_currentMockUnfinalized() {
		String givenCurrentMockName = "given-current-mock-name";
		boolean givenRequireMockFinalization = false;
		boolean givenRequireMockInteractionsVerification = false;

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInteractionsVerification(givenRequireMockInteractionsVerification);

		builder.options = givenOptions;
		builder.currentMockName = givenCurrentMockName;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario();
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void closeScenario_requireMockFinalization_true_currentMockFinalized() {
		String givenCurrentMockName = null;
		boolean givenRequireMockFinalization = true;
		boolean givenRequireMockInteractionsVerification = false;

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInteractionsVerification(givenRequireMockInteractionsVerification);

		builder.options = givenOptions;
		builder.currentMockName = givenCurrentMockName;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario();
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void closeScenario_requireMockFinalization_true_currentMockUnfinalized() {
		String givenCurrentMockName = "given-current-mock-name";
		boolean givenRequireMockFinalization = true;
		boolean givenRequireMockInteractionsVerification = false;

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInteractionsVerification(givenRequireMockInteractionsVerification);

		builder.options = givenOptions;
		builder.currentMockName = givenCurrentMockName;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario();
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Found unfinalized mock [given-current-mock-name]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void closeScenario_requireMockInteractionsVerification_false_unverifiedMocks_empty() {
		boolean givenRequireMockFinalization = false;
		boolean givenRequireMockInteractionsVerification = false;

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInteractionsVerification(givenRequireMockInteractionsVerification);

		Set<String> givenMockNames = new HashSet<String>(Arrays.asList("a", "b", "c"));

		builder.options = givenOptions;
		builder.mockNames = givenMockNames;
		builder.verifiedMockNames = givenMockNames;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario();
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void closeScenario_requireMockInteractionsVerification_false_unverifiedMocks_notEmpty() {
		boolean givenRequireMockFinalization = false;
		boolean givenRequireMockInteractionsVerification = false;

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInteractionsVerification(givenRequireMockInteractionsVerification);

		builder.options = givenOptions;
		builder.mockNames = new HashSet<String>(Arrays.asList("a", "b", "c", "d"));
		builder.verifiedMockNames = new HashSet<String>(Arrays.asList("a", "c"));

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario();
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void closeScenario_requireMockInteractionsVerification_true_unverifiedMocks_empty() {
		boolean givenRequireMockFinalization = false;
		boolean givenRequireMockInteractionsVerification = true;

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInteractionsVerification(givenRequireMockInteractionsVerification);

		Set<String> givenMockNames = new HashSet<String>(Arrays.asList("a", "b", "c"));

		builder.options = givenOptions;
		builder.mockNames = givenMockNames;
		builder.verifiedMockNames = givenMockNames;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario();
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void closeScenario_requireMockInteractionsVerification_true_unverifiedMocks_notEmpty() {
		boolean givenRequireMockFinalization = false;
		boolean givenRequireMockInteractionsVerification = true;

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);
		givenOptions.setRequireMockInteractionsVerification(givenRequireMockInteractionsVerification);

		builder.options = givenOptions;
		builder.mockNames = new HashSet<String>(Arrays.asList("a", "b", "c", "d"));
		builder.verifiedMockNames = new HashSet<String>(Arrays.asList("a", "c"));

		WireCucumberRuntimeException actualException = null;

		try {
			builder.closeScenario();
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
	public void finalizeRequestMock() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentScenarioState = "given-current-scenario-state";
		Integer givenCurrentScenarioStateIndex = 42;
		StubMapping givenStubMapping = mock(StubMapping.class);
		MockBuilder givenMockBuilder = spy(new MockBuilder());

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentScenarioState;
		builder.currentMockStateIndex = givenCurrentScenarioStateIndex;
		builder.currentMockBuilder = givenMockBuilder;
		MockStateKey actualKey = builder.createCurrentMockStateKey();

		doReturn(givenStubMapping)
			.when(givenMockBuilder)
			.createStubForBuilders(anyString());

		builder.finalizeMock();

		assertNull(builder.currentMockName);
		assertNull(builder.currentMockState);
		Integer expectedCurrentMockStateIndex = -1;
		assertEquals(expectedCurrentMockStateIndex, builder.currentMockStateIndex);

		StubMapping actualStubMapping = builder.mockStateStubMappings.get(actualKey);
		assertEquals(givenStubMapping, actualStubMapping);

		Integer actualCurrentScenarioStateIndex = builder.mockStateIndices.get(actualKey);
		assertEquals(givenCurrentScenarioStateIndex++, actualCurrentScenarioStateIndex);
	}

	@Test
	public void finalizeRequestMock_cacheNamedMock() throws Throwable {
		StubMapping givenStubmapping = mock(StubMapping.class);
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentScenarioState = "given-current-scenario-state";
		MockBuilder givenMockBuilder = spy(new MockBuilder());

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentScenarioState;
		builder.currentMockStateIndex = 42;
		builder.currentMockBuilder = givenMockBuilder;

		doReturn(givenStubmapping)
			.when(givenMockBuilder)
			.createStubForBuilders(anyString());

		builder.finalizeMock();

		StubMapping actualStubMapping = builder.getMockStateMapping(givenCurrentMockName, givenCurrentScenarioState);

		assertNotNull(actualStubMapping);
	}

	@Test
	public void finalizeRequestMock_currentMockName_used() throws Throwable {
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
			builder.finalizeMock();
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Mock name [given-current-mock-name] and state [given-next-mock-state] already in use";
		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void finalizeRequestMock_notUnfinalized() {
		String givenCurrentMockName = null;

		builder.currentMockName = givenCurrentMockName;

		WireCucumberRuntimeException actual = null;

		try {
			builder.finalizeMock();
		} catch (WireCucumberRuntimeException e) {
			actual = e;
		}

		assertNotNull(actual);

		String actualExceptionMessage = actual.getMessage();
		String expectedExceptionMessage = "Did not find unfinalized mock";

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
		String givenCurrentScenarioState = "given-current-scenario-state";
		Integer givenCurrentScenarioStateIndex = 42;
		StubMapping givenStubMapping = mock(StubMapping.class);

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentScenarioState;
		builder.currentMockStateIndex = givenCurrentScenarioStateIndex;

		builder.putCurrentMockStateStubMapping(givenStubMapping);

		MockStateKey actualKey = builder.createCurrentMockStateKey();

		StubMapping actualStubMapping = builder.mockStateStubMappings.get(actualKey);
		assertEquals(givenStubMapping, actualStubMapping);

		Integer actualStateIndex = builder.mockStateIndices.get(actualKey);
		Integer expectedStateIndex = givenCurrentScenarioStateIndex++;
		assertEquals(expectedStateIndex, actualStateIndex);
	}

	@Test
	public void putCurrentScenarioStateStub_duplicate() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentScenarioState = "given-current-scenario-state";
		Integer givenCurrentScenarioStateIndex = 42;
		StubMapping givenStubMapping = mock(StubMapping.class);

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentScenarioState;
		builder.currentMockStateIndex = givenCurrentScenarioStateIndex;

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
		String expectedMessage = "Mock name [given-current-mock-name] and state [given-current-scenario-state] already in use";

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void setAllMocksVerified() {
		Set<String> givenMockNames = new HashSet<>(Arrays.asList("given-mock-name-1",
				"given-mock-name-2",
				"given-mock-name-3"));
		Set<String> givenVerifiedMockNames = new HashSet<>();

		builder.mockNames = givenMockNames;
		builder.verifiedMockNames = givenVerifiedMockNames;

		builder.setAllMocksVerified();

		Set<String> actual = builder.verifiedMockNames;
		Set<String> expected = givenMockNames;

		assertEquals(expected, actual);
	}

	@Test
	public void transitionMock() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-scenario-state";
		Integer givenCurrentScenarioStateIndex = 42;
		String givenNextMockState = "given-next-state";
		StubMapping givenStubMapping = mock(StubMapping.class);
		MockBuilder givenMockBuilder = mock(MockBuilder.class);


		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.currentMockStateIndex = givenCurrentScenarioStateIndex;
		builder.currentMockBuilder = givenMockBuilder;

		MockStateKey actualKey = builder.createCurrentMockStateKey();
		doReturn(givenStubMapping).when(givenMockBuilder).setRequestBuilderState(givenCurrentMockState,
				givenNextMockState);

		builder.transitionMockState(givenNextMockState);

		assertEquals(givenCurrentMockName, builder.currentMockName);
		Integer expectedCurrentScenarioStateIndex = givenCurrentScenarioStateIndex + 1;
		assertEquals(expectedCurrentScenarioStateIndex, builder.currentMockStateIndex);
		assertEquals(givenNextMockState, builder.currentMockState);

		StubMapping actualStubMapping = builder.mockStateStubMappings.get(actualKey);
		assertEquals(givenStubMapping, actualStubMapping);

		Integer actualStateIndex = builder.mockStateIndices.get(actualKey);
		Integer expectedStateIndex = givenCurrentScenarioStateIndex;
		assertEquals(expectedStateIndex, actualStateIndex);
	}

	@Test
	public void validateMockStateConfigured() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentScenarioState = "given-current-scenario-state";

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentScenarioState;
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
		String givenCurrentScenarioState = "given-current-scenario-state";

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentScenarioState;
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
		String givenCurrentScenarioState = null;

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentScenarioState;
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
		String givenCurrentScenarioState = "given-current-scenario-state";

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentScenarioState;
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
		String expectedMessage = "Mock name [given-current-mock-name] and state [given-current-scenario-state] already in use";

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void validateMockStateUnused_used() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentScenarioState = "given-current-scenario-state";

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentScenarioState;
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
