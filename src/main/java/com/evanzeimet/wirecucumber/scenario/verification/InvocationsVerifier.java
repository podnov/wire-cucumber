package com.evanzeimet.wirecucumber.scenario.verification;

import static com.evanzeimet.wirecucumber.scenario.verification.VerificationConstants.ACTUAL;
import static com.evanzeimet.wirecucumber.scenario.verification.VerificationConstants.EXPECTED;
import static com.github.tomakehurst.wiremock.client.WireMock.absent;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.findAll;
import static com.github.tomakehurst.wiremock.matching.RequestPattern.thatMatch;
import static com.google.common.collect.FluentIterable.from;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.evanzeimet.wirecucumber.WireCucumberUtils;
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

public class InvocationsVerifier {

	protected static final String BODY_FIELD_NAME = "body";
	protected static final String BODY_PATTERNS_FIELD_NAME = "bodyPatterns";

	protected static final WireCucumberUtils utils = new WireCucumberUtils();

	protected Integer expectedMockInvocationCount;
	protected List<MatchInvocationResult<Request>> customMatchResults = new ArrayList<>();
	protected RequestPatternBuilder requestPatternBuilder;

	public RequestPatternBuilder getRequestPatternBuilder() {
		return requestPatternBuilder;
	}

	public Integer getExpectedMockInvocationCount() {
		return expectedMockInvocationCount;
	}

	public void setExpectedMockInvocationCount(Integer expectedMockInvocationCount) {
		this.expectedMockInvocationCount = expectedMockInvocationCount;
	}

	public void addDataTableBodyVerification(Integer invocationIndex, DataTable dataTable) {
		String requestBody = utils.convertDataTableToJson(dataTable);
		RequestPattern bodyPattern = cloneRequestPatterBuilder()
				.withRequestBody(equalTo(requestBody))
				.build();
		// TODO make datatable verification do data table diff
		addVerification(invocationIndex, bodyPattern);
	}

	public void addEmptyBodyVerification(Integer invocationIndex) {
		RequestPattern bodyPattern = cloneRequestPatterBuilder()
				.withRequestBody(absent())
				.build();
		addVerification(invocationIndex, bodyPattern);
	}

	public void addStringBodyVerification(Integer invocationIndex, String requestBody) {
		RequestPattern bodyPattern = cloneRequestPatterBuilder()
				.withRequestBody(equalTo(requestBody))
				.build();
		addVerification(invocationIndex, bodyPattern);
	}

	public void addUrlVerification(Integer invocationIndex, String url) {
		RequestPattern bodyPattern = cloneRequestPatterBuilder()
				.withUrl(url)
				.build();
		addVerification(invocationIndex, bodyPattern);
	}

	public void addVerification(Integer invocationIndex, RequestPattern bodyPattern)
			throws VerificationException {
		MatchInvocationResult<Request> matchResult = match(invocationIndex, bodyPattern);
		customMatchResults.add(matchResult);
	}

	protected RequestPatternBuilder cloneRequestPatterBuilder() {
		return RequestPatternBuilder.like(requestPatternBuilder.build());
	}

	protected String coalesceActualRequestToExpectedRequestPattern(Request actual, String printedPatternValue) {
		ObjectNode actualNode = utils.valueToTree(actual);
		ObjectNode expectedNode = utils.readTree(printedPatternValue);

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

	public static InvocationsVerifier forRequestPattern(RequestPattern requestPattern) {
		InvocationsVerifier result = new InvocationsVerifier();

		result.requestPatternBuilder = RequestPatternBuilder.like(requestPattern);

		return result;
	}

	protected List<LoggedRequest> findRequests() {
		return findAll(requestPatternBuilder);
	}

	protected MatchInvocationResult<Request> match(Integer invocationIndex,
			RequestPattern pattern)
			throws VerificationException {
		List<LoggedRequest> invocations = findRequests();
		int invocationCount = invocations.size();

		if (invocationCount <= invocationIndex) {
			String message = String.format("Invocation at index [%s] requested but only [%s] invocations found",
					invocationIndex, invocationCount);
			throw new WireCucumberRuntimeException(message);
		}

		return match(invocationIndex, pattern, invocations);
	}

	protected MatchInvocationResult<Request> match(Integer invocationIndex,
			RequestPattern pattern,
			List<LoggedRequest> invocations) {
		LoggedRequest invocation = invocations.get(invocationIndex);
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
		boolean empty = from(asList(invocation))
				.filter(thatMatch(pattern))
				.isEmpty();
		return MatchResult.of(!empty);
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

	public RequestPatternBuilder withHeader(String name, StringValuePattern valuePattern) {
		return requestPatternBuilder.withHeader(name, valuePattern);
	}

	public RequestPatternBuilder withHeaderContaining(String name, String value) {
		return withHeader(name, containing(value));
	}


	public RequestPatternBuilder withRequestBody(ContentPattern<?> valuePattern) {
		return requestPatternBuilder.withRequestBody(valuePattern);
	}

	public RequestPatternBuilder withRequestBodyEmpty() {
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
