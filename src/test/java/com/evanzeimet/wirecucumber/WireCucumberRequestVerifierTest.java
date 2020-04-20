package com.evanzeimet.wirecucumber;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;

public class WireCucumberRequestVerifierTest {

	private WireCucumberInvocationsVerifier verifier;

	@Before
	public void setUp() {
		verifier = spy(new WireCucumberInvocationsVerifier());
	}

	@Test
	public void matchInvocation_false() {
		String givenHeadername = "given-header-name";
		String givenPatternHeaderValue = "given-pattern-header-value";
		RequestPattern givenPattern = RequestPatternBuilder.newRequestPattern()
				.withHeader(givenHeadername, equalTo(givenPatternHeaderValue))
				.build();
		LoggedRequest givenInvocation = createGivenLoggedRequest();

		MatchResult actual = verifier.match(givenInvocation, givenPattern);

		assertFalse(actual.isExactMatch());
	}

	@Test
	public void verifyInvocation_invalidInvocationIndex() {
		Integer givenInvocationIndex = 42;
		RequestPattern givenPattern = mock(RequestPattern.class);

		List<LoggedRequest> givenRequests = new ArrayList<>();

		doReturn(givenRequests)
				.when(verifier)
				.findRequests();

		WireCucumberRuntimeException actualException = null;

		try {
			verifier.match(givenInvocationIndex, givenPattern);
		} catch (WireCucumberRuntimeException e) {
			actualException = e;
		}

		assertNotNull(actualException);

		String actualExceptionMessage = actualException.getMessage();
		String expectedExceptionMessage = "Invocation at index [42] requested but only [0] invocations found";
		assertEquals(expectedExceptionMessage, actualExceptionMessage);
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
