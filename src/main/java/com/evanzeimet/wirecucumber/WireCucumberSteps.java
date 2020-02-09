package com.evanzeimet.wirecucumber;

import static com.evanzeimet.wirecucumber.verification.VerificationConstants.ACTUAL;
import static com.evanzeimet.wirecucumber.verification.VerificationConstants.EXPECTED;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.absent;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
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
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.matching.RequestPattern.thatMatch;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
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
import com.github.tomakehurst.wiremock.client.ScenarioMappingBuilder;
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

import io.cucumber.core.api.Scenario;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.cucumber.java8.HookBody;
import io.cucumber.java8.StepdefBody.A0;
import io.cucumber.java8.StepdefBody.A1;
import io.cucumber.java8.StepdefBody.A2;

public class WireCucumberSteps
		implements En {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	// TODO some context object?
	// TODO call index across mocks/scenarios?
	// TODO multiple calls for a single state?
	protected String currentMockName;
	protected ScenarioMappingBuilder currentRequestBuilder;
	protected RequestPatternBuilder currentRequestVerifierBuilder;
	protected List<MatchInvocationResult<?>> currentRequestVerifierCustomMatchResults;
	protected ResponseDefinitionBuilder currentResponseBuilder;
	protected Scenario currentScenario;
	protected String currentScenarioState;
	protected Integer currentScenarioStateIndex;
	protected Integer expectedMockInvocationCount;
	protected Map<RequestBuilderScenarioStateKey, Integer> stateIndices = new HashMap<>();
	protected Map<RequestBuilderScenarioStateKey, StubMapping> stateMocks = new HashMap<>();


	public ScenarioMappingBuilder getCurrentRequestBuilder() {
		return currentRequestBuilder;
	}

	public RequestPatternBuilder getCurrentRequestVerifierBuilder() {
		return currentRequestVerifierBuilder;
	}

	public ResponseDefinitionBuilder getCurrentResponseBuilder() {
		return currentResponseBuilder;
	}

	protected Integer getStateIndex(String state) {
		RequestBuilderScenarioStateKey key = new RequestBuilderScenarioStateKey(currentMockName,
				state);
		Integer result = stateIndices.get(key);

		if (result == null) {
			String message = String.format("Index for mock [%s] and state [%s] not found", currentMockName, state);
			throw new WireCucumberRuntimeException(message);
		}

		return result;
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

	protected A1<String> addRequestVerifierUrl() {
		return (url) -> {
			currentRequestVerifierBuilder.withUrl(url);
		};
	}

	protected HookBody beforeScenario() {
		return (scenario) -> {
			currentScenario = scenario;
		};
	}

	protected void bootstrapRequestMock(String httpVerb, UrlPattern urlPattern) {
		MappingBuilder requestBuilder;

		switch (httpVerb.toLowerCase()) {
		case "any":
			requestBuilder = any(urlPattern);
			break;

		case "delete":
			requestBuilder = delete(urlPattern);
			break;

		case "get":
			requestBuilder = get(urlPattern);
			break;

		case "patch":
			requestBuilder = patch(urlPattern);
			break;

		case "post":
			requestBuilder = post(urlPattern);
			break;

		case "put":
			requestBuilder = put(urlPattern);
			break;

		default:
			throw createUnexpectedHttpVerbException(httpVerb);
		}

		currentRequestBuilder = requestBuilder.inScenario(currentScenario.getName());
		currentScenarioState = STARTED;
		currentScenarioStateIndex = 0;
	}

	protected A2<String, String> bootstrapUrlEqualToRequestMock() {
		return (httpVerb, path) -> {
			UrlPattern urlPattern = urlEqualTo(path);
			bootstrapRequestMock(httpVerb, urlPattern);
		};
	}

	protected A2<String, String> bootstrapUrlMatchingRequestMock() {
		return (httpVerb, path) -> {
			UrlPattern urlPattern = urlMatching(path);
			bootstrapRequestMock(httpVerb, urlPattern);
		};
	}

	protected String convertDataTableToJson(DataTable dataTable) throws JsonProcessingException {
		List<Map<String, String>> maps = dataTable.asMaps();
		return objectMapper.writeValueAsString(maps);
	}

	public void initialize() {
		Before(beforeScenario());
		Given("a wire mock named {string}", setMockName());
		Given("that wire mock handles (the ){word} verb with a url equal to {string}", bootstrapUrlEqualToRequestMock());
		Given("that wire mock handles (the ){word} verb with a url matching {string}", bootstrapUrlMatchingRequestMock());
		Given("that wire mock accepts {string}", setMockAccepts());
		Given("that wire mock content type is {string}", setMockContentType());
		Given("that wire mock will return a response with status {int}", setMockResponseStatus());
		Given("that wire mock response body is:", setStringMockResponseBody());
		Given("that wire mock response body is {string}", setStringMockResponseBody());
		Given("that wire mock response body is these records:", setDataTableMockResponseBody());
		Given("that wire mock enters state {string}", setRequestBuilderState());
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
		Then("the request body of state {string} should have been:", verifyRequestStateStringBody());
		Then("the request body of state {string} should have been {string}", verifyRequestStateStringBody());
		Then("the request body of state {string} should have been empty", verifyRequestStateEmptyBody());
		Then("the request body of state {string} should have been these records:", verifyRequestStateDataTableBody());
		Then("the request should have had header {string} {string}", addRequestVerifierHeader());
		Then("the request url should have been {string}", addRequestVerifierUrl());
		Then("the request url of invocation {int} should have been {string}", verifyRequestInvocationUrl());
		Then("the request url of state {string} should have been {string}", verifyStateInvocationUrl());
		Then("the request is verified", verifyRequest());
	}

	protected WireCucumberRuntimeException createUnexpectedHttpVerbException(String httpVerb) {
		String message = String.format("Unexpected http verb [%s]", httpVerb);
		return new WireCucumberRuntimeException(message);
	}

	protected void finalizeRequestBuilder(RequestBuilderScenarioStateKey key) {
		StubMapping stubMapping = stubFor(currentRequestBuilder);
		stateMocks.put(key, stubMapping);
		stateIndices.put(key, currentScenarioStateIndex++);
	}

	protected A0 finalizeRequestMock() {
		return () -> {
			if (currentMockName == null) {
				throw new WireCucumberRuntimeException("Mock name not set");
			}
			if (currentScenarioState == null) {
				throw new WireCucumberRuntimeException("Scenario state not set");
			}

			RequestBuilderScenarioStateKey key = new RequestBuilderScenarioStateKey(currentMockName,
					currentScenarioState);

			if (stateMocks.containsKey(key)) {
				String message = String.format("Mock name [%s] and state [%s] already in use",
						currentMockName,
						currentScenarioState);
				throw new WireCucumberRuntimeException(message);
			}

			currentRequestBuilder = currentRequestBuilder.whenScenarioStateIs(currentScenarioState)
					.willReturn(currentResponseBuilder);
			finalizeRequestBuilder(key);

			currentMockName = null;
			currentRequestBuilder = null;
			currentResponseBuilder = null;
		};
	}

	protected A1<String> setRequestBuilderState() {
		return (state) -> {
			RequestBuilderScenarioStateKey key = new RequestBuilderScenarioStateKey(currentMockName,
					currentScenarioState);
			currentRequestBuilder = currentRequestBuilder.whenScenarioStateIs(currentScenarioState)
					.willReturn(currentResponseBuilder)
					.willSetStateTo(state);
			finalizeRequestBuilder(key);

			currentResponseBuilder = null;
			currentScenarioState = state;
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
		// TODO special diff for datatable?
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
			currentMockName = name;
			// TODO how do we want to verify? by mock name? by state name? something else?
			RequestBuilderScenarioStateKey key = new RequestBuilderScenarioStateKey(name, STARTED);
			StubMapping stubMapping = stateMocks.get(key);
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

	protected void verifyInvocation(Integer invocationIndex, RequestPattern bodyPattern) throws VerificationException {
		MatchInvocationResult<Request> matchResult = matchInvocation(invocationIndex, bodyPattern);
		currentRequestVerifierCustomMatchResults.add(matchResult);
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
			// TODO make datatable verification do data table diff
			verifyInvocation(invocationIndex, bodyPattern);
		};
	}

	protected A1<Integer> verifyRequestInvocationEmptyBody() {
		return (invocationIndex) -> {
			RequestPattern bodyPattern = RequestPatternBuilder.like(currentRequestVerifierBuilder.build())
					.withRequestBody(absent())
					.build();
			verifyInvocation(invocationIndex, bodyPattern);
		};
	}

	protected A2<Integer, String> verifyRequestInvocationStringBody() {
		return (invocationIndex, requestBody) -> {
			RequestPattern bodyPattern = RequestPatternBuilder.like(currentRequestVerifierBuilder.build())
					.withRequestBody(equalTo(requestBody))
					.build();
			verifyInvocation(invocationIndex, bodyPattern);
		};
	}

	protected A2<Integer, String> verifyRequestInvocationUrl() {
		return (invocationIndex, url) -> {
			RequestPattern bodyPattern = RequestPatternBuilder.like(currentRequestVerifierBuilder.build())
					.withUrl(url)
					.build();
			verifyInvocation(invocationIndex, bodyPattern);
		};
	}

	protected A2<String, DataTable> verifyRequestStateDataTableBody() {
		return (state, dataTable) -> {
			String requestBody = convertDataTableToJson(dataTable);
			RequestPattern bodyPattern = RequestPatternBuilder.like(currentRequestVerifierBuilder.build())
					.withRequestBody(equalTo(requestBody))
					.build();
			Integer invocationIndex = getStateIndex(state);

			verifyInvocation(invocationIndex, bodyPattern);
		};
	}

	protected A1<String> verifyRequestStateEmptyBody() {
		return (state) -> {
			RequestPattern bodyPattern = RequestPatternBuilder.like(currentRequestVerifierBuilder.build())
					.withRequestBody(absent())
					.build();
			Integer invocationIndex = getStateIndex(state);

			verifyInvocation(invocationIndex, bodyPattern);
		};
	}

	protected A2<String, String> verifyRequestStateStringBody() {
		return (state, requestBody) -> {
			RequestPattern bodyPattern = RequestPatternBuilder.like(currentRequestVerifierBuilder.build())
					.withRequestBody(equalTo(requestBody))
					.build();
			Integer invocationIndex = getStateIndex(state);

			verifyInvocation(invocationIndex, bodyPattern);
		};
	}

	protected A2<String, String> verifyStateInvocationUrl() {
		return (state, url) -> {
			RequestPattern bodyPattern = RequestPatternBuilder.like(currentRequestVerifierBuilder.build())
					.withUrl(url)
					.build();
			Integer invocationIndex = getStateIndex(state);

			verifyInvocation(invocationIndex, bodyPattern);
		};
	}

}
