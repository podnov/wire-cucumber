package com.evanzeimet.wirecucumber.scenario.mocks.verification;

import static com.evanzeimet.wirecucumber.scenario.mocks.verification.DefaultMockInvocationsVerifier.BODY_FIELD_NAME;
import static com.evanzeimet.wirecucumber.scenario.mocks.verification.DefaultMockInvocationsVerifier.BODY_PATTERNS_FIELD_NAME;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;

public class MockInvocationsVerifierTest {

	protected static final WireCucumberUtils utils = new WireCucumberUtils();

	private DefaultMockInvocationsVerifier verifier;

	@Before
	public void setUp() {
		verifier = spy(new DefaultMockInvocationsVerifier());
	}

	@Test
	public void coalesceActualToExpected() {
		Map<String, String> givenExpectedHeaders = new HashMap<>();
		givenExpectedHeaders.put("given-expected-header-name-1", "given-expected-header-value-1");

		HttpHeaders givenActualHeaders = new HttpHeaders();
		givenActualHeaders = givenActualHeaders.plus(new HttpHeader("given-actual-header-name-1", "given-actual-header-value-1"));

		RequestPatternPojo givenRequestPattern = new RequestPatternPojo();
		givenRequestPattern.setBodyPatterns("given-expected-body-patterns");
		givenRequestPattern.setHeaders(givenExpectedHeaders);
		givenRequestPattern.setUrl("given-expected-url");

		String givenExpected = TestUtils.stringify(givenRequestPattern);

		LoggedRequest givenActual = mock(LoggedRequest.class);
		doReturn("given-actual-body".getBytes()).when(givenActual).getBody();
		doReturn(givenActualHeaders).when(givenActual).getHeaders();
		doReturn("given-actual-url").when(givenActual).getUrl();
		givenActual = LoggedRequest.createFrom(givenActual);

		String actual = verifier.coalesceActualRequestToExpectedRequestPattern(givenActual, givenExpected);

		String actualJson = TestUtils.dos2unix(actual);
		String expectedJson = TestUtils.readRelativeResource(getClass(),
				"MockInvocationsVerifierTest_coalesceActualToExpected.expected.json");

		assertEquals(expectedJson, actualJson);
	}

	@Test
	public void coalesceRequestPatternToRequestFieldNames_bodyPatterns_notPresent() {
		String givenFieldName = "givenFieldName";

		List<String> givenFieldNames = new ArrayList<String>();
		givenFieldNames.add(givenFieldName);

		verifier.coalesceRequestPatternToRequestFieldNames(givenFieldNames);

		assertThat(givenFieldNames, containsInAnyOrder(givenFieldName));
	}

	@Test
	public void coalesceRequestPatternToRequestFieldNames_bodyPatterns_present() {
		List<String> givenFieldNames = new ArrayList<String>();
		givenFieldNames.add(BODY_PATTERNS_FIELD_NAME);

		verifier.coalesceRequestPatternToRequestFieldNames(givenFieldNames);

		assertThat(givenFieldNames, containsInAnyOrder(BODY_FIELD_NAME, BODY_PATTERNS_FIELD_NAME));
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
				Collections.emptyList(),
				"http");
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
