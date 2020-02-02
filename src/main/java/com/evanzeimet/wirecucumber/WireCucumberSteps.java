package com.evanzeimet.wirecucumber;

import static com.evanzeimet.wirecucumber.verification.VerificationConstants.ACTUAL;
import static com.evanzeimet.wirecucumber.verification.VerificationConstants.EXPECTED;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.absent;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.evanzeimet.wirecucumber.verification.MatchInvocationResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.VerificationException;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.github.tomakehurst.wiremock.verification.diff.JUnitStyleDiffRenderer;
import com.github.tomakehurst.wiremock.verification.diff.WireCucumberDiffLine;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.cucumber.java8.StepdefBody.A0;
import io.cucumber.java8.StepdefBody.A1;
import io.cucumber.java8.StepdefBody.A2;

public class WireCucumberSteps
		implements En {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	protected String currentMockName;
	protected MappingBuilder currentRequestBuilder;
	protected RequestPatternBuilder currentRequestVerifierBuilder;
	protected List<MatchInvocationResult<?>> currentRequestVerifierCustomMatchResults;
	protected ResponseDefinitionBuilder currentResponseBuilder;
	protected Integer expectedMockInvocationCount;
	protected Map<String, StubMapping> namedMocks = new HashMap<>();

	public MappingBuilder getCurrentRequestBuilder() {
		return currentRequestBuilder;
	}

	public RequestPatternBuilder getCurrentRequestVerifierBuilder() {
		return currentRequestVerifierBuilder;
	}

	public ResponseDefinitionBuilder getCurrentResponseBuilder() {
		return currentResponseBuilder;
	}

	protected A1<DataTable> addRequestVerifierDataTableBody() {
		return (dataTable) -> {
			String requestBody = convertDataTableToJson(dataTable);
			currentRequestVerifierBuilder.withRequestBody(equalTo(requestBody));
		};
	}

	protected A0 addRequestVerifierEmptyBody() {
		return () -> {
			currentRequestVerifierBuilder.withRequestBody(absent());
		};
	}

	protected A2<String, String> addRequestVerifierHeader() {
		return (name, value) -> {
			currentRequestVerifierBuilder.withHeader(name, containing(value));
		};
	}

	protected A1<String> addRequestVerifierStringBody() {
		return (requestBody) -> {
			currentRequestVerifierBuilder.withRequestBody(equalTo(requestBody));
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

	protected String convertDataTableToJson(DataTable dataTable) throws JsonProcessingException {
		List<Map<String, String>> maps = dataTable.asMaps();
		return objectMapper.writeValueAsString(maps);
	}

	public void initialize() {
		Given("a wire mock named {string}", setMockName());
		Given("that wire mock handles the {word} verb with a url equal to {string}", bootstrapRequestMock());
		Given("that wire mock accepts {string}", setMockAccepts());
		Given("that wire mock content type is {string}", setMockContentType());
		Given("that wire mock will return a response with status {int}", setMockResponseStatus());
		Given("that wire mock response body is:", setStringMockResponseBody());
		Given("that wire mock response body is {string}", setStringMockResponseBody());
		Given("that wire mock response body is these records:", setDataTableMockResponseBody());
		Given("that wire mock is finalized", finalizeRequestMock());

		Then("I want to verify interactions with the wire mock named {string}", setCurrentRequestVerifyBuilder());
		Then("that mock should have been invoked {int} time(s)", setVerifyMockInvocationCount());
		// TODO I'm not a huge fan of the "the request" language
		Then("the request body should have been:", addRequestVerifierStringBody());
		Then("the request body should have been {string}", addRequestVerifierStringBody());
		Then("the request body should have been empty", addRequestVerifierEmptyBody());
		Then("the request body should have been these records:", addRequestVerifierDataTableBody());
		Then("the request body of invocation {int} should have been:", verifyRequestInvocationStringBody());
		Then("the request body of invocation {int} should have been {string}", verifyRequestInvocationStringBody());
		Then("the request body of invocation {int} should have been empty", verifyRequestInvocationEmptyBody());
		Then("the request body of invocation {int} should have been these records:", verifyRequestInvocationDataTableBody());
		Then("the request should have had header {string} {string}", addRequestVerifierHeader());
		Then("the request is verified", verifyRequest());
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

	protected MatchInvocationResult<Request> matchInvocation(Integer invocationIndex,
			RequestPattern pattern)
			throws VerificationException {
		List<LoggedRequest> invocations = findRequestsForPattern(currentRequestVerifierBuilder);
		int invocationCount = invocations.size();

		if (invocationCount <= invocationIndex) {
			String message = String.format("Invocation at index [%s] requested but only [%s] invocations found",
					invocationIndex, invocationCount);
			throw new WireCucumberRuntimeException(message);
		}

		LoggedRequest invocation = invocations.get(invocationIndex);
		MatchResult matchResult = matchInvocation(invocation, pattern);

		String requestAttribute = String.format("request-invocation-%d", invocationIndex);
		WireCucumberDiffLine<Request> diffLine = new WireCucumberDiffLine<Request>(requestAttribute,
				pattern,
				invocation,
				pattern.getExpected());

		MatchInvocationResult<Request> result = new MatchInvocationResult<>();

		result.setDiffLine(diffLine);
		result.setInvocationIndex(invocationIndex);
		result.setMatchResult(matchResult);

		return result;
	}

	protected MatchResult matchInvocation(LoggedRequest invocation, RequestPattern pattern) {
		boolean empty = from(asList(invocation))
				.filter(thatMatch(pattern))
				.isEmpty();
		return MatchResult.of(!empty);
	}

	protected A1<String> setCurrentRequestVerifyBuilder() {
		return (name) -> {
			StubMapping stubMapping = namedMocks.get(name);
			if (stubMapping == null) {
				String message = String.format("No mock found for name [%s]", name);
				throw new WireCucumberRuntimeException(message);
			}

			RequestPattern request = stubMapping.getRequest();
			currentRequestVerifierBuilder = RequestPatternBuilder.like(request);
			currentRequestVerifierCustomMatchResults = new ArrayList<>();
		};
	}

	protected A1<DataTable> setDataTableMockResponseBody() {
		return (dataTable) -> {
			String body = convertDataTableToJson(dataTable);
			currentResponseBuilder.withBody(body);
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

	protected A1<Integer> setMockResponseStatus() {
		return (status) -> {
			currentResponseBuilder = aResponse().withStatus(200);
		};
	}

	protected A1<String> setStringMockResponseBody() {
		return (responseBody) -> {
			currentResponseBuilder.withBody(responseBody);
		};
	}

	protected A1<Integer> setVerifyMockInvocationCount() {
		return (count) -> {
			expectedMockInvocationCount = count;
		};
	}

	protected A0 verifyRequest() {
		return () -> {
			verify(expectedMockInvocationCount, currentRequestVerifierBuilder);
			verifyRequestCustom();
			currentRequestVerifierBuilder = null;
			currentRequestVerifierCustomMatchResults = null;
			expectedMockInvocationCount = null;
		};
	}

	protected void verifyRequestCustom() {
		FluentIterable<MatchInvocationResult<?>> failedMatches = from(currentRequestVerifierCustomMatchResults)
				.filter(input -> !input.getMatchResult().isExactMatch());

		if (!failedMatches.isEmpty()) {
			String message = failedMatches.transform(input -> {
				Object expected = EXPECTED.apply(input.getDiffLine());
				Object actual = ACTUAL.apply(input.getDiffLine());
				String diffMessage = JUnitStyleDiffRenderer.junitStyleDiffMessage(expected, actual);
				return String.format("for invocation at index %d,%s", input.getInvocationIndex(), diffMessage);
			}).join(Joiner.on("\n"));

			throw new VerificationException(message);
		}
	}

	protected A2<Integer, DataTable> verifyRequestInvocationDataTableBody() {
		return (invocationIndex, dataTable) -> {
			String requestBody = convertDataTableToJson(dataTable);
			RequestPattern bodyPattern = RequestPatternBuilder.like(currentRequestVerifierBuilder.build())
					.withRequestBody(equalTo(requestBody))
					.build();
			MatchInvocationResult<Request> matchResult = matchInvocation(invocationIndex, bodyPattern);
			currentRequestVerifierCustomMatchResults.add(matchResult);
		};
	}

	protected A1<Integer> verifyRequestInvocationEmptyBody() {
		return (invocationIndex) -> {
			RequestPattern bodyPattern = RequestPatternBuilder.like(currentRequestVerifierBuilder.build())
					.withRequestBody(absent())
					.build();
			MatchInvocationResult<Request> matchResult = matchInvocation(invocationIndex, bodyPattern);
			currentRequestVerifierCustomMatchResults.add(matchResult);
		};
	}

	protected A2<Integer, String> verifyRequestInvocationStringBody() {
		return (invocationIndex, requestBody) -> {
			RequestPattern bodyPattern = RequestPatternBuilder.like(currentRequestVerifierBuilder.build())
					.withRequestBody(equalTo(requestBody))
					.build();
			MatchInvocationResult<Request> matchResult = matchInvocation(invocationIndex, bodyPattern);
			currentRequestVerifierCustomMatchResults.add(matchResult);
		};
	}

}
