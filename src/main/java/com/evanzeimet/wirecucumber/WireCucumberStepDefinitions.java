package com.evanzeimet.wirecucumber;

import static com.evanzeimet.wirecucumber.scenario.mocks.builder.MocksBuilder.DEFAULT_MOCK_EXPECTED_SCENARIO_STATE;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToIgnoreCase;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;

import com.evanzeimet.wirecucumber.scenario.ScenarioBuilder;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.cucumber.java8.HookBody;
import io.cucumber.java8.StepDefinitionBody.A0;
import io.cucumber.java8.StepDefinitionBody.A1;
import io.cucumber.java8.StepDefinitionBody.A2;
import io.cucumber.java8.StepDefinitionBody.A3;

public class WireCucumberStepDefinitions
		implements En {

	protected static final WireCucumberUtils utils = new WireCucumberUtils();

	protected ScenarioBuilder scenarioBuilder;

	public ScenarioBuilder getScenarioBuilder() {
		return scenarioBuilder;
	}

	protected A0 addInvocationDefaultStateBodyAbsentVerification() {
		return () -> {
			addInvocationStateBodyAbsentVerification().accept(DEFAULT_MOCK_EXPECTED_SCENARIO_STATE);
		};
	}

	protected A1<DataTable> addInvocationDefaultStateBodyEqualToDataTableVerification() {
		return (dataTable) -> {
			addInvocationStateBodyEqualToDataTableVerification().accept(DEFAULT_MOCK_EXPECTED_SCENARIO_STATE, dataTable);
		};
	}

	protected A1<String> addInvocationDefaultStateBodyEqualToStringVerification() {
		return (requestBody) -> {
			addInvocationStateBodyEqualToStringVerification().accept(DEFAULT_MOCK_EXPECTED_SCENARIO_STATE, requestBody);
		};
	}

	protected A1<String> addInvocationDefaultStateHeaderAbsentVerification() {
		return (headerName) -> {
			addInvocationStateHeaderAbsentVerification().accept(DEFAULT_MOCK_EXPECTED_SCENARIO_STATE, headerName);
		};
	}

	protected A2<String, String> addInvocationDefaultStateHeaderContainingVerification() {
		return (headerName, headerValue) -> {
			addInvocationStateHeaderContainingVerification().accept(DEFAULT_MOCK_EXPECTED_SCENARIO_STATE, headerName, headerValue);
		};
	}

	protected A1<String> addInvocationDefaultStateHeaderPresentVerification() {
		return (headerName) -> {
			addInvocationStateHeaderPresentVerification().accept(DEFAULT_MOCK_EXPECTED_SCENARIO_STATE, headerName);
		};
	}

	protected A1<String> addInvocationDefaultStateUrlVerification() {
		return (url) -> {
			addInvocationStateUrlVerification().accept(DEFAULT_MOCK_EXPECTED_SCENARIO_STATE, url);
		};
	}

	protected A1<Integer> addInvocationIndexBodyAbsentVerification() {
		return (invocationIndex) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.getMockInvocationsVerifier()
					.addBodyAbsentVerification(invocationIndex);
		};
	}

	protected A2<Integer, DataTable> addInvocationIndexBodyEqualToDataTableVerification() {
		return (invocationIndex, dataTable) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.getMockInvocationsVerifier()
					.addBodyEqualToVerification(invocationIndex, dataTable);
		};
	}

	protected A2<Integer, String> addInvocationIndexBodyEqualToStringVerification() {
		return (invocationIndex, requestBody) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.getMockInvocationsVerifier()
					.addBodyEqualToVerification(invocationIndex, requestBody);
		};
	}

	protected A2<Integer, String> addInvocationIndexHeaderAbsentVerification() {
		return (invocationIndex, headerName) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.getMockInvocationsVerifier()
					.addHeaderAbsentVerification(invocationIndex, headerName);
		};
	}

	protected A3<Integer, String, String> addInvocationIndexHeaderContainingVerification() {
		return (invocationIndex, headerName, headerValue) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.getMockInvocationsVerifier()
					.addHeaderContainingVerification(invocationIndex, headerName, headerValue);
		};
	}

	protected A2<Integer, String> addInvocationIndexHeaderPresentVerification() {
		return (invocationIndex, headerName) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.getMockInvocationsVerifier()
					.addHeaderPresentVerification(invocationIndex, headerName);
		};
	}

	protected A2<Integer, String> addInvocationIndexUrlVerification() {
		return (invocationIndex, url) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.getMockInvocationsVerifier()
					.addUrlVerification(invocationIndex, url);
		};
	}

	protected A1<String> addInvocationStateBodyAbsentVerification() {
		return (state) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
			.getMockInvocationsVerifier()
			.addInvocationStateBodyAbsentVerification(state);
		};
	}

	protected A2<String, DataTable> addInvocationStateBodyEqualToDataTableVerification() {
		return (state, dataTable) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
			.getMockInvocationsVerifier()
			.addInvocationStateBodyEqualToVerification(state, dataTable);
		};
	}

	protected A2<String, String> addInvocationStateBodyEqualToStringVerification() {
		return (state, requestBody) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
			.getMockInvocationsVerifier()
			.addInvocationStateBodyEqualToVerification(state, requestBody);
		};
	}

	protected A2<String, String> addInvocationStateHeaderAbsentVerification() {
		return (state, headerName) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
			.getMockInvocationsVerifier()
			.addInvocationStateHeaderAbsentVerification(state, headerName);
		};
	}

	protected A3<String, String, String> addInvocationStateHeaderContainingVerification() {
		return (state, headerName, headerValue) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
			.getMockInvocationsVerifier()
			.addInvocationStateHeaderContainingVerification(state, headerName, headerValue);
		};
	}

	protected A2<String, String> addInvocationStateHeaderPresentVerification() {
		return (state, headerName) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
			.getMockInvocationsVerifier()
			.addInvocationStateHeaderPresentVerification(state, headerName);
		};
	}

	protected A2<String, String> addInvocationStateUrlVerification() {
		return (state, url) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
			.getMockInvocationsVerifier()
			.addInvocationStateUrlVerification(state, url);
		};
	}

	protected HookBody afterScenario(WireCucumberOptions options) {
		return (scenario) -> {
			scenarioBuilder.closeScenario(options);
		};
	}

	protected HookBody beforeScenario() {
		return (scenario) -> {
			scenarioBuilder.setCurrentCucumberScenario(scenario);
		};
	}

	protected A1<Integer> bootstrapResponseBuilder() {
		return (status) -> {
			scenarioBuilder.getMocksBuilder()
					.mockBuilder()
					.bootstrapResponseBuilder(status);
		};
	}

	protected A3<String, String, String> bootstrapMockWithUrlEqualTo() {
		return (mockName, httpVerb, path) -> {
			scenarioBuilder.getMocksBuilder()
					.bootstrapMockWithUrlEqualTo(mockName, httpVerb, path);
		};
	}

	protected A3<String, String, String> bootstrapMockWithUrlMatching() {
		return (mockName, httpVerb, path) -> {
			scenarioBuilder.getMocksBuilder()
					.bootstrapMockWithUrlMatching(mockName, httpVerb, path);
		};
	}

	protected A3<String, String, String> bootstrapMockWithUrlPathEqualTo() {
		return (mockName, httpVerb, path) -> {
			scenarioBuilder.getMocksBuilder()
					.bootstrapMockWithUrlPathEqualTo(mockName, httpVerb, path);
		};
	}

	protected A3<String, String, String> bootstrapMockWithUrlPathMatching() {
		return (mockName, httpVerb, path) -> {
			scenarioBuilder.getMocksBuilder()
					.bootstrapMockWithUrlPathMatching(mockName, httpVerb, path);
		};
	}

	protected A0 finalizeMock() {
		return () -> {
			scenarioBuilder.getMocksBuilder()
					.finalizeMock();
		};
	}

	public void initialize(WireCucumberOptions options) {
		scenarioBuilder = new ScenarioBuilder(options);

		Before(beforeScenario());

		Given("a wire mock named {string} that handles (the ){word} verb with a url equal to {string}", bootstrapMockWithUrlEqualTo());
		Given("a wire mock named {string} that handles (the ){word} verb with a url matching {string}", bootstrapMockWithUrlMatching());

		Given("a wire mock named {string} that handles (the ){word} verb with a url path equal to {string}", bootstrapMockWithUrlPathEqualTo());
		Given("a wire mock named {string} that handles (the ){word} verb with a url path matching {string}", bootstrapMockWithUrlPathMatching());

		Given("that wire mock expects a url query string parameter {string} equal to {string}", setMockWithQueryParamEqualTo());
		Given("that wire mock expects a url query string parameter {string} matching {string}", setMockWithQueryParamMatching());

		Given("that wire mock expects header {string} equal to {string}", setMockWithRequestHeaderEqualTo());
		Given("that wire mock expects header {string} equal to {string} ignoring case", setMockWithRequestHeaderEqualToIgnoreCase());
		Given("that wire mock expects header {string} matching {string}", setMockWithRequestHeaderMatching());

		Given("that wire mock expects this request body:", setMockWithRequestBody());

		// TODO opportunity, what makes this special (bootstrap)?
		Given("that wire mock will return a response with status {int}", bootstrapResponseBuilder());

		Given("that wire mock response content type is {string}", setMockResponseWithContentType());
		Given("that wire mock response header {string} is {string}", setMockResponseWithHeader());

		Given("that wire mock response body is:", setMockResponseBodyString());
		Given("that wire mock response body is the contents of file {string}", setMockResponseBodyFile());
		Given("that wire mock response body is {string}", setMockResponseBodyString());
		Given("that wire mock response body is these records:", setMockResponseWithBodyDataTable());

		Given("the scenario enters state {string}", setScenarioState());

		Given("that wire mock is finalized", finalizeMock());

		Then("I want to verify invocations of the wire mock named {string}", setMockInvocationsToBeVerified());

		Then("that wire mock should not have been invoked", setVerifyMockNotInvoked());
		Then("that wire mock should have been invoked {int} time(s)", setVerifyMockInvocationCount());

		// TODO I'm not a huge fan of the "the request" language
		Then("the request url should have been {string}", setVerifyMockUrl());

		Then("the request should have had header {string} absent", setVerifyMockHeaderAbsent());
		Then("the request should have had header {string} containing {string}", setVerifyMockHeaderContaining());
		Then("the request should have had header {string} present", setVerifyMockHeaderPresent());

		Then("the request body should have been:", setVerifyMockBodyEqualToString());
		Then("the request body should have been {string}", setVerifyMockBodyEqualToString());
		Then("the request body should have been empty", setVerifyMockBodyAbsent());
		Then("the request body should have been these records:", setVerifyMockBodyEqualToDataTable());

		Then("the request at invocation index {int} should have had url {string}", addInvocationIndexUrlVerification());

		Then("the request at invocation index {int} should have had header {string} absent", addInvocationIndexHeaderAbsentVerification());
		Then("the request at invocation index {int} should have had header {string} containing {string}", addInvocationIndexHeaderContainingVerification());
		Then("the request at invocation index {int} should have had header {string} present", addInvocationIndexHeaderPresentVerification());

		Then("the request at invocation index {int} should have had body:", addInvocationIndexBodyEqualToStringVerification());
		Then("the request at invocation index {int} should have had body {string}", addInvocationIndexBodyEqualToStringVerification());
		Then("the request at invocation index {int} should have had an empty body", addInvocationIndexBodyAbsentVerification());
		Then("the request at invocation index {int} should have had body records:", addInvocationIndexBodyEqualToDataTableVerification());

		Then("the request at the default scenario state should have had url {string}", addInvocationDefaultStateUrlVerification());

		Then("the request at the default scenario state should have had header {string} absent", addInvocationDefaultStateHeaderAbsentVerification());
		Then("the request at the default scenario state should have had header {string} containing {string}", addInvocationDefaultStateHeaderContainingVerification());
		Then("the request at the default scenario state should have had header {string} present", addInvocationDefaultStateHeaderPresentVerification());

		Then("the request at the default scenario state should have had body:", addInvocationDefaultStateBodyEqualToStringVerification());
		Then("the request at the default scenario state should have had body {string}", addInvocationDefaultStateBodyEqualToStringVerification());
		Then("the request at the default scenario state should have had an empty body", addInvocationDefaultStateBodyAbsentVerification());
		Then("the request at the default scenario state should have had body records:", addInvocationDefaultStateBodyEqualToDataTableVerification());

		Then("the request at the {string} scenario state should have had url {string}", addInvocationStateUrlVerification());

		Then("the request at the {string} scenario state should have had header {string} absent", addInvocationStateHeaderAbsentVerification());
		Then("the request at the {string} scenario state should have had header {string} containing {string}", addInvocationStateHeaderContainingVerification());
		Then("the request at the {string} scenario state should have had header {string} present", addInvocationStateHeaderPresentVerification());

		Then("the request at the {string} scenario state should have had body:", addInvocationStateBodyEqualToStringVerification());
		Then("the request at the {string} scenario state should have had body {string}", addInvocationStateBodyEqualToStringVerification());
		Then("the request at the {string} scenario state should have had an empty body", addInvocationStateBodyAbsentVerification());
		Then("the request at the {string} scenario state should have had body records:", addInvocationStateBodyEqualToDataTableVerification());

		Then("the invocations of that wire mock are verified", verifyMockInvocations());

		Then("I want to skip verifying invocations of all wire mocks", setSkipAllMockInvocationsToBeVerified());
		Then("I want to skip verifying invocations of the wire mock named {string}", setSkipMockInvocationsToBeVerified());

		After(afterScenario(options));
	}

	protected A1<DataTable> setMockResponseWithBodyDataTable() {
		return (dataTable) -> {
			scenarioBuilder.getMocksBuilder()
					.mockBuilder()
					.responseBuilderWithBodyDataTable(dataTable);
		};
	}

	protected A1<String> setMockResponseBodyString() {
		return (responseBody) -> {
			scenarioBuilder.getMocksBuilder()
					.mockBuilder()
					.responseBuilderWithBody(responseBody);
		};
	}

	protected A1<String> setMockResponseBodyFile() {
		return (responseBody) -> {
			scenarioBuilder.getMocksBuilder()
					.mockBuilder()
					.responseBuilderWithBodyFile(responseBody);
		};
	}

	protected A1<String> setMockResponseWithContentType() {
		return (contentType) -> {
			scenarioBuilder.getMocksBuilder()
					.mockBuilder()
					.responseBuilderWithContentType(contentType);
		};
	}

	protected A2<String, String> setMockResponseWithHeader() {
		return (headerName, headerValue) -> {
			scenarioBuilder.getMocksBuilder()
					.mockBuilder()
					.responseBuilderWithHeader(headerName, headerValue);
		};
	}

	protected A1<String> setMockInvocationsToBeVerified() {
		return (mockName) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.bootstrapVerifier(mockName);
		};
	}

	protected A2<String, String> setMockWithRequestHeaderEqualTo() {
		return (key, value) -> {
			scenarioBuilder.getMocksBuilder()
					.mockBuilder()
					.requestBuilderWithHeader(key, equalTo(value));
		};
	}

	protected A2<String, String> setMockWithRequestHeaderEqualToIgnoreCase() {
		return (key, value) -> {
			scenarioBuilder.getMocksBuilder()
					.mockBuilder()
					.requestBuilderWithHeader(key, equalToIgnoreCase(value));
		};
	}

	protected A2<String, String> setMockWithRequestHeaderMatching() {
		return (key, value) -> {
			scenarioBuilder.getMocksBuilder()
					.mockBuilder()
					.requestBuilderWithHeader(key, matching(value));
		};
	}

	protected A2<String, String> setMockWithQueryParamEqualTo() {
		return (key, value) -> {
			scenarioBuilder.getMocksBuilder()
					.mockBuilder()
					.requestBuilderWithQueryParam(key, equalTo(value));
		};
	}

	protected A2<String, String> setMockWithQueryParamMatching() {
		return (key, value) -> {
			scenarioBuilder.getMocksBuilder()
					.mockBuilder()
					.requestBuilderWithQueryParam(key, matching(value));
		};
	}

	protected A1<String> setMockWithRequestBody() {
		return (value) -> {
			scenarioBuilder.getMocksBuilder()
					.mockBuilder()
					.requestBuilderWithRequestBody(equalTo(value));
		};
	}

	protected A1<String> setScenarioState() {
		return (newState) -> {
			scenarioBuilder.getMocksBuilder()
					.transitionScenarioState(newState);
		};
	}

	protected A0 setSkipAllMockInvocationsToBeVerified() {
		return () -> {
			scenarioBuilder.getMocksInvocationsVerifier()
				.setAllMocksVerified();
		};
	}

	protected A1<String> setSkipMockInvocationsToBeVerified() {
		return (mockName) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
			.setMockVerified(mockName);
		};
	}

	protected A1<String> setVerifyMockHeaderAbsent() {
		return (name) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.getMockInvocationsVerifier()
					.withHeaderAbsent(name);
		};
	}

	protected A2<String, String> setVerifyMockHeaderContaining() {
		return (name, value) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.getMockInvocationsVerifier()
					.withHeaderContaining(name, value);
		};
	}

	protected A1<String> setVerifyMockHeaderPresent() {
		return (name) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.getMockInvocationsVerifier()
					.withHeaderPresent(name);
		};
	}

	protected A1<String> setVerifyMockUrl() {
		return (url) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.getMockInvocationsVerifier()
					.withUrl(url);
		};
	}

	protected A0 setVerifyMockBodyAbsent() {
		return () -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.getMockInvocationsVerifier()
					.withRequestBodyAbsent();
		};
	}

	protected A1<DataTable> setVerifyMockBodyEqualToDataTable() {
		return (dataTable) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.getMockInvocationsVerifier()
					.withRequestBodyEqualTo(dataTable);
		};
	}

	protected A1<String> setVerifyMockBodyEqualToString() {
		return (requestBody) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.getMockInvocationsVerifier()
					.withRequestBodyEqualTo(requestBody);
		};
	}

	protected A1<Integer> setVerifyMockInvocationCount() {
		return (count) -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.getMockInvocationsVerifier()
					.setExpectedMockInvocationCount(count);
		};
	}

	protected A0 setVerifyMockNotInvoked() {
		return () -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.getMockInvocationsVerifier()
					.setExpectedMockInvocationCount(0);
		};
	}

	protected A0 verifyMockInvocations() {
		return () -> {
			scenarioBuilder.getMocksInvocationsVerifier()
					.verifyCurrentMockInvocations();
		};
	}

}
