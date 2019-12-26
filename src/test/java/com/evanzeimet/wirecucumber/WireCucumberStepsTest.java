package com.evanzeimet.wirecucumber;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

public class WireCucumberStepsTest {

	private WireCucumberSteps steps;

	@Before
	public void setUp() {
		bootstrapWireMock();

		steps = new WireCucumberSteps();
	}

	@Test
	public void finalizeRequestMock_cacheNamedMock() throws Throwable {
		String givenCurrentMockname = "given-current-mock-name";

		steps.currentMockName = givenCurrentMockname;
		steps.currentRequestBuilder = any(urlEqualTo("meh"));
		steps.currentResponseBuilder = aResponse().withStatus(200);
		steps.namedMocks.clear();

		steps.finalizeRequestMock().accept();

		StubMapping actualStubMapping = steps.namedMocks.get(givenCurrentMockname);
		assertNotNull(actualStubMapping);
	}

	@Test
	public void finalizeRequestMock_currentMockName_null() throws Throwable {
		steps.currentMockName = null;

		WireCucumberRuntimeException actualException = null;

		try {
			steps.finalizeRequestMock().accept();
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
		steps.currentMockName = givenCurrentMockName;
		steps.namedMocks.put(givenCurrentMockName, StubMapping.NOT_CONFIGURED);

		WireCucumberRuntimeException actualException = null;

		try {
			steps.finalizeRequestMock().accept();
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Mock name [given-current-mock-name] already in use";
		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void finalizeRequestMock_resetData() throws Throwable {
		steps.currentMockName = "given-current-mock-name";
		steps.currentRequestBuilder = any(urlEqualTo("meh"));
		steps.currentResponseBuilder = aResponse().withStatus(200);

		steps.finalizeRequestMock().accept();

		assertNull(steps.currentMockName);
		assertNull(steps.currentRequestBuilder);
		assertNull(steps.currentResponseBuilder);
	}

	protected void bootstrapWireMock() {
		WireMockServer wireMockServer = new WireMockServer(options().dynamicPort());
		wireMockServer.start();
		int port = wireMockServer.port();
		configureFor("localhost", port);
	}



}
