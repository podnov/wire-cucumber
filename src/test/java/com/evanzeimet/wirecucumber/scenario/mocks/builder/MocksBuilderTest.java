package com.evanzeimet.wirecucumber.scenario.mocks.builder;

import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.cucumber.java8.Status.PASSED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.wirecucumber.WireCucumberOptions;
import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.evanzeimet.wirecucumber.scenario.ScenarioContext;
import com.evanzeimet.wirecucumber.scenario.mocks.MockStateKey;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import io.cucumber.java8.Scenario;

public class MocksBuilderTest {

	private MocksBuilder builder;
	private ScenarioContext givenContext;
	private WireCucumberOptions givenOptions;

	@Before
	public void setUp() {
		givenContext = spy(new ScenarioContext());
		givenOptions = spy(new WireCucumberOptions());

		builder = spy(new MocksBuilder(givenOptions, givenContext));
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
	public void bootstrapMockWithUrlEqualTo_unexpectedHttpVerb() throws Throwable {
		String givenMockName = "given-mock-name";
		String givenHttpVerb = "FLY";
		String givenPath = "/fly";

		WireCucumberRuntimeException actualException = null;

		try {
			builder.bootstrapMockWithUrlEqualTo(givenMockName, givenHttpVerb, givenPath);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Unexpected http verb [FLY]";
		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void finalizeMock_isDisabled_false() {
		boolean givenIsDisabled = false;
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";
		Integer givenCurrentMockStateIndex = 42;
		StubMapping givenStubMapping = mock(StubMapping.class);
		MockBuilder givenMockBuilder = spy(new MockBuilder());

		builder.context = givenContext;
		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.currentMockStateIndex = givenCurrentMockStateIndex;
		builder.mockBuilder = givenMockBuilder;
		MockStateKey actualKey = builder.createCurrentMockStateKey();

		doReturn(givenIsDisabled)
			.when(givenOptions)
				.getIsDisabled();

		doReturn(givenStubMapping)
			.when(givenMockBuilder)
			.createStubForBuilders(anyString());

		builder.finalizeMock();

		assertNull(builder.currentMockName);
		assertNull(builder.currentMockState);

		verify(givenMockBuilder).finalizeMock(givenCurrentMockState);

		Integer expectedCurrentMockStateIndex = -1;
		assertEquals(expectedCurrentMockStateIndex, builder.currentMockStateIndex);

		verify(givenContext).putMockState(actualKey, givenStubMapping, givenCurrentMockStateIndex);
	}

	@Test
	public void finalizeRequestMock_isDisabled_true() {
		boolean givenIsDisabled = true;
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";
		Integer givenCurrentMockStateIndex = 42;
		MockBuilder givenMockBuilder = spy(new MockBuilder());

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.currentMockStateIndex = givenCurrentMockStateIndex;
		builder.mockBuilder = givenMockBuilder;

		doReturn(givenIsDisabled)
				.when(givenOptions)
				.getIsDisabled();

		builder.finalizeMock();

		assertNull(builder.currentMockName);
		assertNull(builder.currentMockState);

		verify(givenMockBuilder, never()).finalizeMock(givenCurrentMockState);

		Integer expectedCurrentMockStateIndex = -1;
		assertEquals(expectedCurrentMockStateIndex, builder.currentMockStateIndex);

		verify(givenContext, never()).putMockState(any(MockStateKey.class), any(StubMapping.class), anyInt());
	}

	@Test
	public void finalizeRequestMock_currentMockName_null() {
		boolean givenIsDisabled = false;
		String givenCurrentMockName = null;

		builder.currentMockName = givenCurrentMockName;

		doReturn(givenIsDisabled)
				.when(givenOptions)
				.getIsDisabled();

		WireCucumberRuntimeException actualException = null;

		try {
			builder.finalizeMock();
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Mock name not set";
		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void finalizeRequestMock_currentMockName_used() {
		boolean givenIsDisabled = false;
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";
		String givenNextMockState = "given-next-mock-state";

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.currentMockStateIndex = 0;
		builder.mockBuilder = mock(MockBuilder.class);
		builder.transitionMockState(givenNextMockState);
		builder.putCurrentMockStateStubMapping(null);

		doReturn(givenIsDisabled)
				.when(givenOptions)
				.getIsDisabled();

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
	public void putCurrentMockStateStubMapping() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";
		Integer givenCurrentMockStateIndex = 42;
		StubMapping givenStubMapping = mock(StubMapping.class);

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.currentMockStateIndex = givenCurrentMockStateIndex;

		builder.putCurrentMockStateStubMapping(givenStubMapping);

		MockStateKey actualKey = builder.createCurrentMockStateKey();

		verify(givenContext).putMockState(actualKey, givenStubMapping, givenCurrentMockStateIndex);
	}

	@Test
	public void putCurrentMockStateStubMapping_duplicate() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";
		Integer givenCurrentMockStateIndex = 42;
		Boolean givenContainsMockState = true;
		StubMapping givenStubMapping = mock(StubMapping.class);

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.currentMockStateIndex = givenCurrentMockStateIndex;

		MockStateKey actualKey = builder.createCurrentMockStateKey();

		doReturn(givenContainsMockState).when(givenContext).containsMockState(actualKey);

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
	public void transitionMockState() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";
		Integer givenCurrentMockStateIndex = 42;
		String givenNextMockState = "given-next-state";
		StubMapping givenStubMapping = mock(StubMapping.class);
		MockBuilder givenMockBuilder = mock(MockBuilder.class);

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		builder.currentMockStateIndex = givenCurrentMockStateIndex;
		builder.mockBuilder = givenMockBuilder;

		MockStateKey actualKey = builder.createCurrentMockStateKey();
		doReturn(givenStubMapping).when(givenMockBuilder)
				.setRequestBuilderState(givenCurrentMockState, givenNextMockState);

		builder.transitionMockState(givenNextMockState);

		assertEquals(givenCurrentMockName, builder.currentMockName);

		Integer expectedCurrentScenarioStateIndex = givenCurrentMockStateIndex + 1;
		assertEquals(expectedCurrentScenarioStateIndex, builder.currentMockStateIndex);
		assertEquals(givenNextMockState, builder.currentMockState);

		verify(givenContext).putMockState(actualKey, givenStubMapping, givenCurrentMockStateIndex);
	}

	@Test
	public void validateMockStateConfigured() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;

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

		boolean givenContextContainsMockState = false;
		doReturn(givenContextContainsMockState).when(givenContext).containsMockState(givenKey);

		WireCucumberRuntimeException actual = null;

		try {
			builder.validateMockStateUnused();
		} catch (WireCucumberRuntimeException e) {
			actual = e;
		}

		assertNull(actual);
	}

	@Test
	public void validateMockStateUnused_used() {
		String givenCurrentMockName = "given-current-mock-name";
		String givenCurrentMockState = "given-current-mock-state";

		builder.currentMockName = givenCurrentMockName;
		builder.currentMockState = givenCurrentMockState;
		MockStateKey givenKey = builder.createCurrentMockStateKey();

		boolean givenContextContainsMockState = true;
		doReturn(givenContextContainsMockState).when(givenContext).containsMockState(givenKey);

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
	public void verifyMocksFinalized_currentMockFinalized() {
		String givenCurrentMockName = null;

		Scenario givenCurrentCucumberScenario = mock(Scenario.class);
		doReturn(PASSED).when(givenCurrentCucumberScenario).getStatus();

		builder.currentMockName = givenCurrentMockName;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.verifyMocksFinalized();
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void verifyMocksFinalized_currentMockUnfinalized() {
		String givenCurrentMockName = "given-current-mock-name";

		Scenario givenCurrentCucumberScenario = mock(Scenario.class);
		doReturn(PASSED).when(givenCurrentCucumberScenario).getStatus();

		builder.currentMockName = givenCurrentMockName;

		WireCucumberRuntimeException actualException = null;

		try {
			builder.verifyMocksFinalized();
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Found unfinalized mock [given-current-mock-name]";

		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

}
