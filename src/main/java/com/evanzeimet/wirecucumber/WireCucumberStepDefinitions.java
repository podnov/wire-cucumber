package com.evanzeimet.wirecucumber;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
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
		Given("that wire mock expects this request body:", setMockWithRequestBody());

		Given("that wire mock accepts {string}", setMockWithAcceptContaining());
		Given("that wire mock content type is {string}", setMockWithContentTypeContaining());
		// TODO opportunity, what makes this special (bootstrap)?
		Given("that wire mock will return a response with status {int}", bootstrapResponseBuilder());

		Given("that wire mock response body is:", setMockResponseBodyString());
		Given("that wire mock response body is the contents of file {string}", setMockResponseBodyFile());
		Given("that wire mock response body is {string}", setMockResponseBodyString());
		Given("that wire mock response body is these records:", setMockResponseWithBodyDataTable());

		Given("that wire mock response content type is {string}", setMockResponseWithContentType());
		Given("that wire mock response header {string} is {string}", setMockResponseWithHeader());

		Given("that wire mock enters invocation state {string}", setMockState());

		Given("that wire mock is finalized", finalizeMock());

		Then("I want to verify invocations of the wire mock named {string}", setMockInvocationsToBeVerified());

		Then("that wire mock should not have been invoked", setVerifyMockNotInvoked());
		Then("that wire mock should have been invoked {int} time(s)", setVerifyMockInvocationCount());

		// TODO I'm not a huge fan of the "the request" language
		Then("the request body should have been:", setVerifyMockBodyEqualToString());
		Then("the request body should have been {string}", setVerifyMockBodyEqualToString());
		Then("the request body should have been empty", setVerifyMockBodyAbsent());
		Then("the request body should have been these records:", setVerifyMockBodyEqualToDataTable());
		Then("the request at invocation index {int} should have had body:", addInvocationIndexBodyEqualToStringVerification());
		Then("the request at invocation index {int} should have had body {string}", addInvocationIndexBodyEqualToStringVerification());
		Then("the request at invocation index {int} should have had an empty body", addInvocationIndexBodyAbsentVerification());
		Then("the request at invocation index {int} should have had body records:", addInvocationIndexBodyEqualToDataTableVerification());
		Then("the request at invocation state {string} should have had body:", addInvocationStateBodyEqualToStringVerification());
		Then("the request at invocation state {string} should have had body {string}", addInvocationStateBodyEqualToStringVerification());
		Then("the request at invocation state {string} should have had an empty body", addInvocationStateBodyAbsentVerification());
		Then("the request at invocation state {string} should have had body records:", addInvocationStateBodyEqualToDataTableVerification());

		Then("the request should have had header {string} absent", setVerifyMockHeaderAbsent());
		Then("the request should have had header {string} containing {string}", setVerifyMockHeaderContaining());
		Then("the request should have had header {string} present", setVerifyMockHeaderPresent());
		Then("the request at invocation index {int} should have had header {string} absent", addInvocationIndexHeaderAbsentVerification());
		Then("the request at invocation index {int} should have had header {string} containing {string}", addInvocationIndexHeaderContainingVerification());
		Then("the request at invocation index {int} should have had header {string} present", addInvocationIndexHeaderPresentVerification());
		Then("the request at invocation state {string} should have had header {string} absent", addInvocationStateHeaderAbsentVerification());
		Then("the request at invocation state {string} should have had header {string} containing {string}", addInvocationStateHeaderContainingVerification());
		Then("the request at invocation state {string} should have had header {string} present", addInvocationStateHeaderPresentVerification());

		Then("the request url should have been {string}", setVerifyMockUrl());
		Then("the request at invocation index {int} should have had url {string}", addInvocationIndexUrlVerification());
		Then("the request at invocation state {string} should have had url {string}", addInvocationStateUrlVerification());

		Then("the invocations of that wire mock are verified", verifyMockInvocations());

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

	protected A1<String> setMockWithAcceptContaining() {
		return (value) -> {
			scenarioBuilder.getMocksBuilder()
					.mockBuilder()
					.requestBuilderWithAccept(containing(value));
		};
	}

	protected A1<String> setMockWithContentTypeContaining() {
		return (value) -> {
			scenarioBuilder.getMocksBuilder()
					.mockBuilder()
					.requestBuilderWithContentType(containing(value));
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

	protected A1<String> setMockState() {
		return (newState) -> {
			scenarioBuilder.getMocksBuilder()
					.transitionMockState(newState);
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
