package com.evanzeimet.wirecucumber.scenario.mocks.verification;

import static com.evanzeimet.wirecucumber.scenario.mocks.verification.VerificationConstants.ACTUAL;
import static com.evanzeimet.wirecucumber.scenario.mocks.verification.VerificationConstants.EXPECTED;
import static com.github.tomakehurst.wiremock.client.WireMock.absent;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.findAll;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.matching.RequestPattern.thatMatch;
import static com.google.common.collect.FluentIterable.from;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.evanzeimet.wirecucumber.WireCucumberUtils;
import com.evanzeimet.wirecucumber.scenario.ScenarioContext;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.tomakehurst.wiremock.client.VerificationException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.matching.ContentPattern;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.github.tomakehurst.wiremock.verification.diff.JUnitStyleDiffRenderer;
import com.github.tomakehurst.wiremock.verification.diff.WireCucumberDiffLine;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;

import io.cucumber.datatable.DataTable;

public class MockInvocationsVerifier {

	protected static final String BODY_FIELD_NAME = "body";
	protected static final String BODY_PATTERNS_FIELD_NAME = "bodyPatterns";

	protected static final WireCucumberUtils utils = new WireCucumberUtils();

	protected ScenarioContext context;
	protected List<MatchInvocationResult<Request>> customMatchResults = new ArrayList<>();
	protected Integer expectedMockInvocationCount;
	protected String mockName;
	protected RequestPatternBuilder requestPatternBuilder;

	protected MockInvocationsVerifier() {

	}

	public void setExpectedMockInvocationCount(Integer expectedMockInvocationCount) {
		this.expectedMockInvocationCount = expectedMockInvocationCount;
	}

	public void addInvocationStateBodyAbsentVerification(String state) {
		Integer invocationIndex = context.getMockStateIndex(mockName, state);
		addBodyAbsentVerification(invocationIndex);
	}

	public void addInvocationStateBodyEqualToVerification(String state,
			String requestBody) {
		Integer invocationIndex = context.getMockStateIndex(mockName, state);
		addBodyEqualToVerification(invocationIndex, requestBody);
	}

	public void addInvocationStateBodyEqualToVerification(String state, DataTable dataTable) {
		Integer invocationIndex = context.getMockStateIndex(mockName, state);
		addBodyEqualToVerification(invocationIndex, dataTable);
	}

	public void addInvocationStateHeaderAbsentVerification(String state, String headerName) {
		Integer invocationIndex = context.getMockStateIndex(mockName, state);
		addHeaderAbsentVerification(invocationIndex, headerName);
	}

	public void addInvocationStateHeaderContainingVerification(String state,
			String headerName,
			String headerValue) {
		Integer invocationIndex = context.getMockStateIndex(mockName, state);
		addHeaderContainingVerification(invocationIndex, headerName, headerValue);
	}

	public void addInvocationStateHeaderPresentVerification(String state, String headerName) {
		Integer invocationIndex = context.getMockStateIndex(mockName, state);
		addHeaderPresentVerification(invocationIndex, headerName);
	}

	public void addInvocationStateUrlVerification(String state, String url) {
		Integer invocationIndex = context.getMockStateIndex(mockName, state);
		addUrlVerification(invocationIndex, url);
	}

	public void addBodyAbsentVerification(Integer invocationIndex) {
		RequestPattern bodyPattern = cloneRequestPatterBuilder()
				.withRequestBody(absent())
				.build();
		addVerification(invocationIndex, bodyPattern);
	}

	public void addBodyEqualToVerification(Integer invocationIndex, String requestBody) {
		RequestPattern bodyPattern = cloneRequestPatterBuilder()
				.withRequestBody(equalTo(requestBody))
				.build();
		addVerification(invocationIndex, bodyPattern);
	}

	public void addBodyEqualToVerification(Integer invocationIndex, DataTable dataTable) {
		String requestBody = utils.convertDataTableToJson(dataTable);
		RequestPattern bodyPattern = cloneRequestPatterBuilder()
				.withRequestBody(equalTo(requestBody))
				.build();
		// TODO make datatable verification do data table diff
		// TODO this would have to match not only body, but other request attributes
		addVerification(invocationIndex, bodyPattern);
	}

	public void addHeaderAbsentVerification(Integer invocationIndex, String headerName) {
		RequestPattern bodyPattern = cloneRequestPatterBuilder()
				.withoutHeader(headerName)
				.build();
		addVerification(invocationIndex, bodyPattern);
	}

	public void addHeaderContainingVerification(Integer invocationIndex,
			String headerName,
			String headerValue) {
		addHeaderVerification(invocationIndex, headerName, containing(headerValue));
	}

	public void addHeaderPresentVerification(Integer invocationIndex, String headerName) {
		addHeaderVerification(invocationIndex, headerName, matching(".*"));
	}

	public void addHeaderVerification(Integer invocationIndex,
			String headerName,
			StringValuePattern headerValuePattern) {
		RequestPattern bodyPattern = cloneRequestPatterBuilder()
				.withHeader(headerName, headerValuePattern)
				.build();
		addVerification(invocationIndex, bodyPattern);
	}

	public void addUrlVerification(Integer invocationIndex, String url) {
		RequestPattern bodyPattern = cloneRequestPatterBuilder()
				.withUrl(url)
				.build();
		addVerification(invocationIndex, bodyPattern);
	}

	public void addVerification(Integer invocationIndex, RequestPattern requestPattern)
			throws VerificationException {
		MatchInvocationResult<Request> matchResult = match(invocationIndex, requestPattern);
		customMatchResults.add(matchResult);
	}

	protected RequestPatternBuilder cloneRequestPatterBuilder() {
		return RequestPatternBuilder.like(requestPatternBuilder.build());
	}

	protected String coalesceActualRequestToExpectedRequestPattern(Request actual, String printedPatternValue) {
		ObjectNode actualNode = utils.valueToObject(actual);
		ObjectNode expectedNode = utils.readObject(printedPatternValue);

		List<String> expectedFieldNames = utils.getNodeFieldNames(expectedNode);

		coalesceRequestPatternToRequestFieldNames(expectedFieldNames);
		removeUnexpectedFieldNames(actualNode, expectedFieldNames);

		return utils.writeValueAsPrettyString(actualNode);
	}

	protected void coalesceRequestPatternToRequestFieldNames(List<String> fieldNames) {
		if (fieldNames.contains(BODY_PATTERNS_FIELD_NAME)) {
			fieldNames.add(BODY_FIELD_NAME);
		}
	}

	protected String createFailedInvocationMessage(MatchInvocationResult<Request> input) {
		String printedPatternValue = (String) EXPECTED.apply(input.getDiffLine());
		Request actual = (Request) ACTUAL.apply(input.getDiffLine());
		String actualJson = coalesceActualRequestToExpectedRequestPattern(actual, printedPatternValue);

		String diffMessage = JUnitStyleDiffRenderer.junitStyleDiffMessage(printedPatternValue, actualJson);
		int invocationIndex = input.getInvocationIndex();

		return String.format("for invocation at index %d,%s", invocationIndex, diffMessage);
	}

	public static MockInvocationsVerifier forRequestPattern(ScenarioContext context,
			String mockName,
			RequestPattern requestPattern) {
		MockInvocationsVerifier result = new MockInvocationsVerifier();

		result.mockName = mockName;
		result.context = context;
		result.requestPatternBuilder = RequestPatternBuilder.like(requestPattern);

		return result;
	}

	protected List<LoggedRequest> findRequests() {
		return findAll(requestPatternBuilder);
	}

	protected MatchInvocationResult<Request> match(Integer invocationIndex,
			RequestPattern pattern)
			throws VerificationException {
		LoggedRequest invocation = verifyInvocationIndex(invocationIndex);
		return match(invocationIndex, pattern, invocation);
	}

	protected MatchInvocationResult<Request> match(Integer invocationIndex,
			RequestPattern pattern,
			LoggedRequest invocation) {
		MatchResult matchResult = match(invocation, pattern);
		String requestAttribute = String.format("request-invocation-%d", invocationIndex);
		String printedPatternValue = pattern.getExpected();

		WireCucumberDiffLine<Request> diffLine = new WireCucumberDiffLine<Request>(requestAttribute,
				pattern,
				invocation,
				printedPatternValue);

		MatchInvocationResult<Request> result = new MatchInvocationResult<>();

		result.setDiffLine(diffLine);
		result.setInvocationIndex(invocationIndex);
		result.setMatchResult(matchResult);

		return result;
	}

	protected MatchResult match(LoggedRequest invocation, RequestPattern pattern) {
		boolean isMatchEmpty = from(asList(invocation))
				.filter(thatMatch(pattern))
				.isEmpty();
		return MatchResult.of(!isMatchEmpty);
	}

	protected void removeUnexpectedFieldNames(ObjectNode actualNode,
			List<String> expectedFieldNames) {
		List<String> actualFieldNames = utils.getNodeFieldNames(actualNode);

		actualFieldNames.removeAll(expectedFieldNames);
		actualFieldNames.forEach(actualNode::remove);
	}

	public void verify() {
		verifyCount();
		verifyCustom();
	}

	protected void verifyCustom() {
		FluentIterable<MatchInvocationResult<Request>> failedMatches = from(customMatchResults)
				.filter(input -> !input.getMatchResult().isExactMatch());

		if (!failedMatches.isEmpty()) {
			String message = failedMatches.transform(input -> {
				return createFailedInvocationMessage(input);
			}).join(Joiner.on("\n"));

			throw new VerificationException(message);
		}
	}

	protected void verifyCount() {
		WireMock.verify(expectedMockInvocationCount, requestPatternBuilder);
	}

	protected LoggedRequest verifyInvocationIndex(Integer invocationIndex) {
		List<LoggedRequest> invocations = findRequests();
		int invocationCount = invocations.size();

		if (invocationCount <= invocationIndex) {
			String message = String.format("Invocation at index [%s] requested but only [%s] invocations found",
					invocationIndex, invocationCount);
			throw new WireCucumberRuntimeException(message);
		}

		return invocations.get(invocationIndex);
	}

	public RequestPatternBuilder withHeader(String name, StringValuePattern valuePattern) {
		return requestPatternBuilder.withHeader(name, valuePattern);
	}

	public RequestPatternBuilder withHeaderAbsent(String name) {
		return requestPatternBuilder.withoutHeader(name);
	}

	public RequestPatternBuilder withHeaderContaining(String name, String value) {
		return withHeader(name, containing(value));
	}

	public RequestPatternBuilder withHeaderPresent(String name) {
		return withHeader(name, matching(".*"));
	}

	public RequestPatternBuilder withRequestBody(ContentPattern<?> valuePattern) {
		return requestPatternBuilder.withRequestBody(valuePattern);
	}

	public RequestPatternBuilder withRequestBodyAbsent() {
		return withRequestBody(absent());
	}

	public RequestPatternBuilder withRequestBodyEqualTo(DataTable dataTable) {
		String requestBody = utils.convertDataTableToJson(dataTable);
		return withRequestBodyEqualTo(requestBody);
	}

	public RequestPatternBuilder withRequestBodyEqualTo(String requestBody) {
		return withRequestBody(equalTo(requestBody));
	}

	public RequestPatternBuilder withUrl(String url) {
		return requestPatternBuilder.withUrl(url);
	}

}
