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
		RequestPattern bodyPattern = RequestPatternBuilder.like(requestPatternBuilder.build())
				.withRequestBody(equalTo(requestBody))
				.build();
		// TODO make datatable verification do data table diff
		addVerification(invocationIndex, bodyPattern);
	}

	public void addEmptyBodyVerification(Integer invocationIndex) {
		RequestPattern bodyPattern = RequestPatternBuilder.like(requestPatternBuilder.build())
				.withRequestBody(absent())
				.build();
		addVerification(invocationIndex, bodyPattern);
	}

	public void addStringBodyVerification(Integer invocationIndex, String requestBody) {
		RequestPattern bodyPattern = RequestPatternBuilder.like(requestPatternBuilder.build())
				.withRequestBody(equalTo(requestBody))
				.build();
		addVerification(invocationIndex, bodyPattern);
	}

	public void addUrlVerification(Integer invocationIndex, String url) {
		RequestPattern bodyPattern = RequestPatternBuilder.like(requestPatternBuilder.build())
				.withUrl(url)
				.build();
		addVerification(invocationIndex, bodyPattern);
	}

	public void addVerification(Integer invocationIndex, RequestPattern bodyPattern)
			throws VerificationException {
		MatchInvocationResult<Request> matchResult = match(invocationIndex, bodyPattern);
		customMatchResults.add(matchResult);
	}

	protected String coalesceActualToExpected(Object actual, String expected) {
		ObjectNode expectedNode = utils.readTree(expected);
		ObjectNode actualNode = utils.valueToTree(actual);

		List<String> actualFieldNames = utils.getNodeFieldNames(actualNode);
		List<String> expectedFieldNames = utils.getNodeFieldNames(expectedNode);

		if (expectedFieldNames.contains("bodyPatterns")) {
			expectedFieldNames.add("body");
		}

		actualFieldNames.removeAll(expectedFieldNames);
		actualFieldNames.forEach(actualNode::remove);

		return utils.writeValueAsPrettyString(actualNode);
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

	public void verify() {
		verifyCount();
		verifyCustom();
	}

	protected void verifyCustom() {
		FluentIterable<MatchInvocationResult<Request>> failedMatches = from(customMatchResults)
				.filter(input -> !input.getMatchResult().isExactMatch());

		if (!failedMatches.isEmpty()) {
			String message = failedMatches.transform(input -> {
				String expected = (String) EXPECTED.apply(input.getDiffLine());
				Object actual = ACTUAL.apply(input.getDiffLine());
				actual = coalesceActualToExpected(actual, expected);
				String diffMessage = JUnitStyleDiffRenderer.junitStyleDiffMessage(expected, actual);
				return String.format("for invocation at index %d,%s", input.getInvocationIndex(), diffMessage);
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
