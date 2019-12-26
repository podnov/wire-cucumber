package com.evanzeimet.wirecucumber;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.findAll;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.patch;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.matching.RequestPattern.thatMatch;
import static com.google.common.collect.FluentIterable.from;
import static java.util.Arrays.asList;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.evanzeimet.wirecucumber.matchers.EmptyRequestBodyMatcher;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.VerificationException;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;

import io.cucumber.java8.En;
import io.cucumber.java8.StepdefBody.A0;
import io.cucumber.java8.StepdefBody.A1;
import io.cucumber.java8.StepdefBody.A2;

public class WireCucumberSteps implements En {

	protected String currentMockName;
	protected MappingBuilder currentRequestBuilder;
	protected RequestPatternBuilder currentRequestVerifyBuilder;
	protected ResponseDefinitionBuilder currentResponseBuilder;
	protected Integer expectedMockInvocationCount;
	protected Map<String, StubMapping> namedMocks = new HashMap<>();

	protected A1<String> addRequestVerifierBody() {
		return (requestBody) -> {
			currentRequestVerifyBuilder.withRequestBody(equalTo(requestBody));
		};
	}

	protected A0 addRequestVerifierEmptyBody() {
		return () -> {
			currentRequestVerifyBuilder.andMatching(new EmptyRequestBodyMatcher());
		};
	}

	protected A2<String, String> bootstrapRequestMock() {
		return (httpVerb, path) -> {
			UrlPattern urlPattern = urlEqualTo(path);

			switch (httpVerb) {
			case "DELETE":
				currentRequestBuilder = delete(urlPattern);
				break;

			case "GET":
				currentRequestBuilder = get(urlPattern);
				break;

			case "PATCH":
				currentRequestBuilder = patch(urlPattern);
				break;

			case "POST":
				currentRequestBuilder = post(urlPattern);
				break;

			case "PUT":
				currentRequestBuilder = put(urlPattern);
				break;

			default:
				throw createUnexpectedHttpVerbException(httpVerb);
			}
		};
	}

	public void initialize() {
		Given("a wire mock named {string}", setMockName());
		Given("that wire mock handles the {word} verb with a url equal to {string}", bootstrapRequestMock());
		Given("that wire mock accepts {string}", setMockAccepts());
		Given("that wire mock content type is {string}", setMockContentType());
		Given("that wire mock will return a response with status {int}", setMockResponseStatus());
		Given("that wire mock response content type is {string}", setMockResponseBody());
		Given("that wire mock response body is {string}", setMockResponseBody());
		Given("that wire mock response body is:", setMockResponseBody());
		Given("that wire mock is finalized", finalizeRequestMock());

		Then("I want to verify interactions with the wire mock named {string}", setCurrentRequestVerifyBuilder());
		Then("that mock should have been invoked {int} time(s)", setVerifyMockInvocationCount());
		Then("the request body should have been:", addRequestVerifierBody());
		Then("the request body should have been empty", addRequestVerifierEmptyBody());
		Then("the request body of invocation {int} should have been:", verifyRequestInvocationBody());
		Then("the request body of invocation {int} should have been empty", verifyRequestInvocationEmptyBody());
		Then("my request is verified", verifyRequest());
	}

	protected WireCucumberRuntimeException createUnexpectedHttpVerbException(String httpVerb) {
		String message = String.format("Unexpected http verb [%s]", httpVerb);
		return new WireCucumberRuntimeException(message);
	}

	protected A0 finalizeRequestMock() {
		return () -> {
			if (currentMockName == null) {
				throw new WireCucumberRuntimeException("Mock name not set");
			}
			if (namedMocks.containsKey(currentMockName)) {
				String message = String.format("Mock name [%s] already in use", currentMockName);
				throw new WireCucumberRuntimeException(message);
			}

			StubMapping stubMapping = stubFor(currentRequestBuilder.willReturn(currentResponseBuilder));
			namedMocks.put(currentMockName, stubMapping);
			currentMockName = null;
			currentRequestBuilder = null;
			currentResponseBuilder = null;
		};
	}

	protected List<LoggedRequest> findRequestsForPattern(RequestPatternBuilder pattern) {
		return findAll(pattern);
	}

	protected boolean matchInvocation(LoggedRequest invocation, RequestPattern pattern) {
		return !from(asList(invocation))
				.filter(thatMatch(pattern))
				.isEmpty();
	}

	protected A1<String> setCurrentRequestVerifyBuilder() {
		return (name) -> {
			StubMapping stubMapping = namedMocks.get(name);
			if (stubMapping == null) {
				String message = String.format("No mock found for name [%s]", name);
				throw new WireCucumberRuntimeException(message);
			}

			RequestPattern request = stubMapping.getRequest();
			currentRequestVerifyBuilder = RequestPatternBuilder.like(request);
		};
	}

	protected A1<String> setMockAccepts() {
		return (value) -> {
			currentRequestBuilder.withHeader(ACCEPT, containing(value));
		};
	}

	protected A1<String> setMockContentType() {
		return (value) -> {
			currentRequestBuilder.withHeader(CONTENT_TYPE, containing(value));
		};
	}

	protected A1<String> setMockName() {
		return (name) -> {
			currentMockName = name;
		};
	}

	protected A1<String> setMockResponseBody() {
		return (responseBody) -> {
			currentResponseBuilder.withBody(responseBody);
		};
	}

	protected A1<Integer> setMockResponseStatus() {
		return (status) -> {
			currentResponseBuilder = aResponse().withStatus(200);
		};
	}

	protected A1<Integer> setVerifyMockInvocationCount() {
		return (count) -> {
			expectedMockInvocationCount = count;
		};
	}

	protected void verifyInvocation(Integer invocationIndex, RequestPattern pattern) throws VerificationException {
		List<LoggedRequest> invocations = findRequestsForPattern(currentRequestVerifyBuilder);
		int invocationCount = invocations.size();
		if (invocationCount <= invocationIndex) {
			String message = String.format("Invocation at index [%s] requested but only [%s] invocations found",
					invocationIndex, invocationCount);
			throw new WireCucumberRuntimeException(message);
		}

		LoggedRequest invocation = invocations.get(invocationIndex);
		verifyInvocation(invocation, pattern);
	}

	protected void verifyInvocation(LoggedRequest invocation, RequestPattern pattern) throws VerificationException {
		boolean matches = matchInvocation(invocation, pattern);

		if (!matches) {
			// TODO this should delay until "my request is verified" step?
			throw new VerificationException(pattern, 1, 0);
		}
	}

	protected A2<Integer, String> verifyRequestInvocationBody() {
		return (invocationIndex, requestBody) -> {
			RequestPattern bodyPattern = new RequestPatternBuilder()
					.withRequestBody(equalTo(requestBody))
					.build();
			verifyInvocation(invocationIndex, bodyPattern);
		};
	}

	protected A1<Integer> verifyRequestInvocationEmptyBody() {
		return (invocationIndex) -> {
			RequestPattern bodyPattern = new RequestPatternBuilder()
					.andMatching(new EmptyRequestBodyMatcher())
					.build();
			verifyInvocation(invocationIndex, bodyPattern);
		};
	}

	protected A0 verifyRequest() {
		return () -> {
			verify(expectedMockInvocationCount, currentRequestVerifyBuilder);
		};
	}

}
