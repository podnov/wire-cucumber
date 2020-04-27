package com.evanzeimet.wirecucumber.scenario.verification;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.wirecucumber.TestUtils;
import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.evanzeimet.wirecucumber.WireCucumberUtils;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;

public class InvocationVerifierTest {

	protected static final WireCucumberUtils utils = new WireCucumberUtils();

	private InvocationsVerifier verifier;

	@Before
	public void setUp() {
		verifier = spy(new InvocationsVerifier());
	}

	@Test
	public void coalesceActualToExpected() {
		Map<String, String> givenExpectedHeaders = new HashMap<>();
		givenExpectedHeaders.put("given-expected-header-name-1", "given-expected-header-value-1");

		Map<String, String> givenActualheaders = new HashMap<>();
		givenActualheaders.put("given-actual-header-name-1", "given-actual-header-value-1");

		RequestPatternPojo givenRequestPattern = new RequestPatternPojo();
		givenRequestPattern.setBodyPatterns("given-expected-body-patterns");
		givenRequestPattern.setHeaders(givenExpectedHeaders);
		givenRequestPattern.setUrl("given-expected-url");

		String givenExpected = TestUtils.stringify(givenRequestPattern);

		RequestPojo givenActual = new RequestPojo();
		givenActual.setBody("given-actual-body");
		givenActual.setHeaders(givenActualheaders);
		givenActual.setUrl("given-actual-url");

		String actual = verifier.coalesceActualToExpected(givenActual, givenExpected);

		String actualJson = TestUtils.dos2unix(actual);
		String expectedJson = TestUtils.readRelativeResource(getClass(),
				"InvocationVerifierTest_coalesceActualToExpected.expected.json");

		assertEquals(expectedJson, actualJson);
	}

	@Test
	public void match_false() {
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
	public void match_invalidInvocationIndex() {
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

	@SuppressWarnings("unused")
	private static class RequestPojo {

		private String body;
		private String extraField1;
		private String extraField2;
		private Map<String, String> headers;
		private String url;

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public Map<String, String> getHeaders() {
			return headers;
		}

		public void setHeaders(Map<String, String> headers) {
			this.headers = headers;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

	@SuppressWarnings("unused")
	private static class RequestPatternPojo {

		private String bodyPatterns;
		private Map<String, String> headers;
		private String url;

		public String getBodyPatterns() {
			return bodyPatterns;
		}

		public void setBodyPatterns(String bodyPatterns) {
			this.bodyPatterns = bodyPatterns;
		}

		public Map<String, String> getHeaders() {
			return headers;
		}

		public void setHeaders(Map<String, String> headers) {
			this.headers = headers;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

}
