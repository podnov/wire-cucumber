package com.evanzeimet.wirecucumber;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.VerificationException;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;

public class WireCucumberStepsTest {

	private WireCucumberSteps steps;

	@Before
	public void setUp() {
		bootstrapWireMock();

		steps = spy(new WireCucumberSteps());
	}

	@Test
	public void bootstrapRequestMock_default() throws Throwable {
		WireCucumberRuntimeException actualException = null;

		try {
			steps.bootstrapRequestMock().accept("FLY", "/fly");
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Unexpected http verb [FLY]";
		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void createUnexpectedHttpVerbException() {
		WireCucumberRuntimeException actual = steps.createUnexpectedHttpVerbException("FLY");

		assertNotNull(actual);

		String actualExceptionMessage = actual.getMessage();
		String expectedExceptionMessage = "Unexpected http verb [FLY]";
		assertEquals(expectedExceptionMessage, actualExceptionMessage);
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

	@Test
	public void matchInvocation_false() {
		String givenHeadername = "garbage";
		String givenPatternHeaderValue = "do-not-use";
		RequestPattern givenPattern = RequestPatternBuilder.newRequestPattern()
				.withHeader(givenHeadername, equalTo(givenPatternHeaderValue))
				.build();
		LoggedRequest givenInvocation = createGivenLoggedRequest();

		boolean actual = steps.matchInvocation(givenInvocation, givenPattern);

		assertFalse(actual);
	}

	@Test
	public void setCurrentRequestVerifyBuilder_nameNotFound() throws Throwable {
		String givenName = "given-name";
		steps.namedMocks = Collections.emptyMap();

		WireCucumberRuntimeException actualException = null;

		try {
			steps.setCurrentRequestVerifyBuilder().accept(givenName);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "No mock found for name [given-name]";
		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void verifyInvocation_invalidInvocationIndex() {
		Integer givenInvocationIndex = 42;
		RequestPattern givenPattern = mock(RequestPattern.class);
		RequestPatternBuilder givenCurrentRequestVerifyBuilder = mock(RequestPatternBuilder.class);

		steps.currentRequestVerifyBuilder = givenCurrentRequestVerifyBuilder;

		List<LoggedRequest> givenRequests = new ArrayList<>();

		doReturn(givenRequests)
				.when(steps)
				.findRequestsForPattern(givenCurrentRequestVerifyBuilder);

		WireCucumberRuntimeException actualException = null;

		try {
			steps.verifyInvocation(givenInvocationIndex, givenPattern);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Invocation at index [42] requested but only [0] invocations found";
		assertEquals(expectedExceptionMessage, actualExceptionMessage);
	}

	@Test
	public void verifyInvocation_match_false() {
		boolean givenMatch = false;
		LoggedRequest givenInvocation = mock(LoggedRequest.class);
		RequestPattern givenPattern = mock(RequestPattern.class);

		doReturn(givenMatch)
				.when(steps)
				.matchInvocation(givenInvocation, givenPattern);

		VerificationException actualException = null;

		try {
			steps.verifyInvocation(givenInvocation, givenPattern);
		} catch (VerificationException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Expected exactly 1 requests matching the following pattern but received 0";
		assertThat(actualExceptionMessage, startsWith(expectedExceptionMessage));
	}

	@Test
	public void verifyInvocation_match_true() {
		boolean givenMatch = true;
		LoggedRequest givenInvocation = mock(LoggedRequest.class);
		RequestPattern givenPattern = mock(RequestPattern.class);

		doReturn(givenMatch)
				.when(steps)
				.matchInvocation(givenInvocation, givenPattern);

		VerificationException actualException = null;

		try {
			steps.verifyInvocation(givenInvocation, givenPattern);
		} catch (VerificationException e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	protected void bootstrapWireMock() {
		WireMockServer wireMockServer = new WireMockServer(options().dynamicPort());
		wireMockServer.start();
		int port = wireMockServer.port();
		configureFor("localhost", port);
	}

	protected LoggedRequest createGivenLoggedRequest() {
		return new LoggedRequest("given-invocation-url",
				"http://given-invocation-absolute-url/",
				RequestMethod.DELETE,
				"127.0.0.1",
				new HttpHeaders(),
				Collections.emptyMap(),
				false,
				new Date(),
				new byte[] {},
				Collections.emptyList());
	}

}
