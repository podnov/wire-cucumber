package com.evanzeimet.wirecucumber.scenario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.evanzeimet.wirecucumber.scenario.mocks.MockStateKey;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

public class ScenarioContextTest {

	private ScenarioContext context;

	@Before
	public void setUp() {
		context = new ScenarioContext();
	}

	@Test
	public void containsMockState_false() {
		String givenMockName = "given-mock-name";
		String givenState = "given-state";

		context.mockStateStubMappings.clear();

		boolean actual = context.containsMockState(givenMockName, givenState);

		assertFalse(actual);
	}

	@Test
	public void containsMockState_true() {
		String givenMockName = "given-mock-name";
		String givenState = "given-state";
		StubMapping givenValue = mock(StubMapping.class);

		MockStateKey givenMockKey = new MockStateKey(givenMockName, givenState);
		context.mockStateStubMappings.put(givenMockKey, givenValue);

		boolean actual = context.containsMockState(givenMockName, givenState);

		assertTrue(actual);
	}

	@Test
	public void getMockStateIndex_notPresent() {
		String givenMockName = "given-mock-name";
		String givenState = "given-state";

		context.mockStateIndices.clear();

		WireCucumberRuntimeException actual = null;

		try {
			context.getMockStateIndex(givenMockName, givenState);
		} catch (WireCucumberRuntimeException e) {
			actual = e;
		}

		assertNotNull(actual);

		String actualMessage = actual.getMessage();
		String expectedMessage = "Index for mock [given-mock-name] and state [given-state] not found";

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void getMockStateIndex_present() {
		String givenMockName = "given-mock-name";
		String givenState = "given-state";
		Integer givenValue = 42;

		MockStateKey givenMockKey = new MockStateKey(givenMockName, givenState);
		context.mockStateIndices.put(givenMockKey, givenValue);

		Integer actual = context.getMockStateIndex(givenMockName, givenState);

		assertEquals(givenValue, actual);
	}

	@Test
	public void getMockStateMapping_notPresent() {
		String givenMockName = "given-mock-name";
		String givenState = "given-state";

		context.mockStateStubMappings.clear();

		StubMapping actual = context.getMockStateMapping(givenMockName, givenState);

		assertNull(actual);
	}

	@Test
	public void getMockStateMapping_present() {
		String givenMockName = "given-mock-name";
		String givenState = "given-state";
		StubMapping givenValue = mock(StubMapping.class);

		MockStateKey givenMockKey = new MockStateKey(givenMockName, givenState);
		context.mockStateStubMappings.put(givenMockKey, givenValue);

		StubMapping actual = context.getMockStateMapping(givenMockName, givenState);

		assertEquals(givenValue, actual);
	}

}
