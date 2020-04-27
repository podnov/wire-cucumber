package com.evanzeimet.wirecucumber.scenario;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

public class MockBuilderTest {

	private MockBuilder builder;

	@Before
	public void setUp() {
		builder = spy(new MockBuilder());
	}

	@Test
	public void createUnexpectedHttpVerbException() {
		WireCucumberRuntimeException actual = MockBuilder.createUnexpectedHttpVerbException("FLY");

		assertNotNull(actual);

		String actualExceptionMessage = actual.getMessage();
		String expectedExceptionMessage = "Unexpected http verb [FLY]";
		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void finalizeRequestMock() throws Throwable {
		String givenScenarioState = "given-current-scenario-state";
		StubMapping givenStubmapping = mock(StubMapping.class);

		builder.requestBuilder = any(urlEqualTo("given-url")).inScenario("given-scenario");
		builder.responseBuilder = aResponse().withStatus(200);

		doReturn(givenStubmapping).when(builder).createStubForBuilders(givenScenarioState);

		builder.finalizeMock(givenScenarioState);

		assertNull(builder.requestBuilder);
		assertNull(builder.responseBuilder);
	}

}
