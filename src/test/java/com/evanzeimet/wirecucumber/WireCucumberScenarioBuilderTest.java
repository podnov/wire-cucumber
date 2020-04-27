package com.evanzeimet.wirecucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Test;

import com.github.tomakehurst.wiremock.stubbing.StubMapping;

public class WireCucumberScenarioBuilderTest {

	private WireCucumberScenarioBuilder builder;

	@Before
	public void setUp() {
		builder = spy(new WireCucumberScenarioBuilder());
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
		WireCucumberMockBuilder givenMockBuilder = spy(new WireCucumberMockBuilder());

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentScenarioState;
		builder.currentMockStateIndex = givenCurrentScenarioStateIndex;
		builder.mockBuilder = givenMockBuilder;
		MockStateKey actualKey = builder.createCurrentMockStateKey();

		doReturn(givenStubMapping)
			.when(givenMockBuilder)
			.createStubForBuilders(anyString());

		builder.finalizeRequestMock();

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
		WireCucumberMockBuilder givenMockBuilder = spy(new WireCucumberMockBuilder());

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentScenarioState;
		builder.currentMockStateIndex = 42;
		builder.mockBuilder = givenMockBuilder;

		doReturn(givenStubmapping)
			.when(givenMockBuilder)
			.createStubForBuilders(anyString());

		builder.finalizeRequestMock();

		StubMapping actualStubMapping = builder.getMockStateMapping(givenCurrentMockName, givenCurrentScenarioState);

		assertNotNull(actualStubMapping);
	}

	@Test
	public void finalizeRequestMock_currentMockName_null() throws Throwable {
		String givenCurrentMockName = null;

		builder.currentMockName = givenCurrentMockName;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.finalizeRequestMock();
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
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";
		String givenNextMockState = "given-next-mock-state";

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.currentMockStateIndex = 0;
		builder.mockBuilder = mock(WireCucumberMockBuilder.class);
		builder.transitionMock(givenNextMockState);
		builder.putCurrentMockStateStubMapping(null);

		WireCucumberRuntimeException actualException = null;

		try {
			builder.finalizeRequestMock();
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
	public void transitionMock() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-scenario-state";
		Integer givenCurrentScenarioStateIndex = 42;
		String givenNextMockState = "given-next-state";
		StubMapping givenStubMapping = mock(StubMapping.class);
		WireCucumberMockBuilder givenMockBuilder = mock(WireCucumberMockBuilder.class);


		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.currentMockStateIndex = givenCurrentScenarioStateIndex;
		builder.mockBuilder = givenMockBuilder;

		MockStateKey actualKey = builder.createCurrentMockStateKey();
		doReturn(givenStubMapping).when(givenMockBuilder).setRequestBuilderState(givenCurrentMockState,
				givenNextMockState);

		builder.transitionMock(givenNextMockState);

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
