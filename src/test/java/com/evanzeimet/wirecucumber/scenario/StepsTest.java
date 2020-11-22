package com.evanzeimet.wirecucumber.scenario;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.evanzeimet.wirecucumber.scenario.verification.MockInvocationsVerifier;
import com.github.tomakehurst.wiremock.WireMockServer;

public class StepsTest {

	private Steps steps;

	@Before
	public void setUp() {
		bootstrapWireMock();

		steps = spy(new Steps());
	}

	@Test
	public void bootstrapUrlEqualToRequestMock_unexpectedHttpVerb() throws Throwable {
		String givenMockName = "given-mock-name";
		String givenHttpVerb = "FLY";
		String givenPath = "/fly";

		WireCucumberRuntimeException actualException = null;

		try {
			steps.bootstrapUrlEqualToRequestMock().accept(givenMockName, givenHttpVerb, givenPath);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Unexpected http verb [FLY]";
		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void setCurrentRequestVerifyBuilder_nameNotFound() throws Throwable {
		String givenName = "given-name";
		// steps.stateMocks = Collections.emptyMap();

		WireCucumberRuntimeException actualException = null;

		try {
			steps.setMockToBeVerified().accept(givenName);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "No mock found for name [given-name]";
		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void verifyRequest() throws Throwable {
		steps.scenarioBuilder.currentMockVerifier = mock(MockInvocationsVerifier.class);

		steps.verifyMockInvocations().accept();

		assertNull(steps.scenarioBuilder.currentMockVerifier);
	}

	protected void bootstrapWireMock() {
		WireMockServer wireMockServer = new WireMockServer(options().dynamicPort());
		wireMockServer.start();
		int port = wireMockServer.port();
		configureFor("localhost", port);
	}

}
