package com.evanzeimet.wirecucumber.scenario;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;

import com.evanzeimet.wirecucumber.WireCucumberOptions;
import com.evanzeimet.wirecucumber.WireCucumberUtils;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.cucumber.java8.HookBody;
import io.cucumber.java8.StepDefinitionBody.A0;
import io.cucumber.java8.StepDefinitionBody.A1;
import io.cucumber.java8.StepDefinitionBody.A2;
import io.cucumber.java8.StepDefinitionBody.A3;

public class Steps
		implements En {

	protected static final WireCucumberUtils utils = new WireCucumberUtils();

	protected ScenarioBuilder scenarioBuilder = new ScenarioBuilder();

	public ScenarioBuilder getScenarioBuilder() {
		return scenarioBuilder;
	}

	protected A2<Integer, DataTable> addInvocationIndexDataTableBodyVerification() {
		return (invocationIndex, dataTable) -> {
			scenarioBuilder.getCurrentMockVerifier()
				.addDataTableBodyVerification(invocationIndex, dataTable);
		};
	}

	protected A1<Integer> addInvocationIndexEmptyBodyVerification() {
		return (invocationIndex) -> {
			scenarioBuilder.getCurrentMockVerifier()
				.addEmptyBodyVerification(invocationIndex);
		};
	}

	protected A2<Integer, String> addInvocationIndexStringBodyVerification() {
		return (invocationIndex, requestBody) -> {
			scenarioBuilder.getCurrentMockVerifier()
				.addStringBodyVerification(invocationIndex, requestBody);
		};
	}

	protected A2<Integer, String> addInvocationIndexUrlVerification() {
		return (invocationIndex, url) -> {
			scenarioBuilder.getCurrentMockVerifier()
				.addUrlVerification(invocationIndex, url);
		};
	}

	protected A2<String, DataTable> addInvocationStateDataTableBodyVerification() {
		return (state, dataTable) -> {
			scenarioBuilder.addInvocationStateDataTableBodyVerification(state,
					dataTable);
		};
	}

	protected A1<String> addInvocationStateEmptyBodyVerification() {
		return (state) -> {
			scenarioBuilder.addInvocationStateEmptyBodyVerification(state);
		};
	}

	protected A2<String, String> addInvocationStateStringBodyVerification() {
		return (state, requestBody) -> {
			scenarioBuilder.addInvocationStateStringBodyVerification(state, requestBody);
		};
	}

	protected A2<String, String> addInvocationStateUrlVerification() {
		return (state, url) -> {
			scenarioBuilder.addInvocationStateUrlVerification(state, url);
		};
	}

	protected A2<String, String> setVerifyMockHeader() {
		return (name, value) -> {
			scenarioBuilder.getCurrentMockVerifier()
					.withHeaderContaining(name, value);
		};
	}

	protected A1<String> setVerifyMockUrl() {
		return (url) -> {
			scenarioBuilder.getCurrentMockVerifier()
				.withUrl(url);
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
			scenarioBuilder.getCurrentMockBuilder()
					.bootstrapResponseBuilder(status);
		};
	}

	protected A3<String, String, String> bootstrapUrlEqualToRequestMock() {
		return (mockName, httpVerb, path) -> {
			scenarioBuilder.bootstrapMockWithUrlEqualTo(mockName, httpVerb, path);
		};
	}

	protected A3<String, String, String> bootstrapUrlMatchingRequestMock() {
		return (mockName, httpVerb, path) -> {
			scenarioBuilder.bootstrapMockWithUrlMatching(mockName, httpVerb, path);
		};
	}

	protected A3<String, String, String> bootstrapUrlPathEqualToRequestMock() {
		return (mockName, httpVerb, path) -> {
			scenarioBuilder.bootstrapMockWithUrlPathEqualTo(mockName, httpVerb, path);
		};
	}

	protected A3<String, String, String> bootstrapUrlPathMatchingRequestMock() {
		return (mockName, httpVerb, path) -> {
			scenarioBuilder.bootstrapMockWithUrlPathMatching(mockName, httpVerb, path);
		};
	}

	protected A0 finalizeMock() {
		return () -> {
			scenarioBuilder.finalizeMock();
		};
	}

	public void initialize(WireCucumberOptions options) {
		Before(beforeScenario());

		Given("a wire mock named {string} that handles (the ){word} verb with a url equal to {string}", bootstrapUrlEqualToRequestMock());
		Given("a wire mock named {string} that handles (the ){word} verb with a url matching {string}", bootstrapUrlMatchingRequestMock());

		Given("a wire mock named {string} that handles (the ){word} verb with a url path equal to {string}", bootstrapUrlPathEqualToRequestMock());
		Given("a wire mock named {string} that handles (the ){word} verb with a url path matching {string}", bootstrapUrlPathMatchingRequestMock());

		Given("that wire mock expects a url query string parameter {string} equal to {string}", setMockQueryParamEqualTo());
		Given("that wire mock expects a url query string parameter {string} matching {string}", setMockQueryParamMatching());

		Given("that wire mock accepts {string}", setMockAccepts());
		Given("that wire mock content type is {string}", setMockContentType());
		// TODO opportunity, what makes this special (bootstrap)?
		Given("that wire mock will return a response with status {int}", bootstrapResponseBuilder());

		Given("that wire mock response body is:", setMockResponseBodyString());
		Given("that wire mock response body is {string}", setMockResponseBodyString());
		Given("that wire mock response body is these records:", setMockResponseBodyDataTable());

		Given("that wire mock response content type is {string}", setMockResponseContentType());
		Given("that wire mock response header {string} is {string}", setMockResponseHeaderValue());

		Given("that wire mock enters invocation state {string}", setMockState());

		Given("that wire mock is finalized", finalizeMock());

		Then("I want to verify interactions with the wire mock named {string}", setMockToBeVerified());

		Then("that wire mock should not have been invoked", setVerifyMockNotInvoked());
		Then("that wire mock should have been invoked {int} time(s)", setVerifyMockInvocationCount());

		// TODO I'm not a huge fan of the "the request" language
		Then("the request body should have been:", setVerifyMockStringBody());
		Then("the request body should have been {string}", setVerifyMockStringBody());
		Then("the request body should have been empty", setVerifyMockEmptyBody());
		Then("the request body should have been these records:", setVerifyMockDataTableBody());
		Then("the request should have had header {string} {string}", setVerifyMockHeader());
		Then("the request url should have been {string}", setVerifyMockUrl());

		Then("the request body of invocation index {int} should have been:", addInvocationIndexStringBodyVerification());
		Then("the request body of invocation index {int} should have been {string}", addInvocationIndexStringBodyVerification());
		Then("the request body of invocation index {int} should have been empty", addInvocationIndexEmptyBodyVerification());
		Then("the request body of invocation index {int} should have been these records:", addInvocationIndexDataTableBodyVerification());
		Then("the request url of invocation index {int} should have been {string}", addInvocationIndexUrlVerification());

		Then("the request body of invocation state {string} should have been:", addInvocationStateStringBodyVerification());
		Then("the request body of invocation state {string} should have been {string}", addInvocationStateStringBodyVerification());
		Then("the request body of invocation state {string} should have been empty", addInvocationStateEmptyBodyVerification());
		Then("the request body of invocation state {string} should have been these records:", addInvocationStateDataTableBodyVerification());
		Then("the request url of invocation state {string} should have been {string}", addInvocationStateUrlVerification());

		Then("the interactions with that wire mock are verified", verifyMockInvocations());

		After(afterScenario(options));
	}

	protected A1<String> setMockState() {
		return (newState) -> {
			scenarioBuilder.transitionMockState(newState);
		};
	}

	protected A1<String> setMockAccepts() {
		return (value) -> {
			scenarioBuilder.getCurrentMockBuilder()
				.requestBuilderWithAccept(containing(value));
		};
	}

	protected A2<String, String> setMockQueryParamEqualTo() {
		return (key, value) -> {
			scenarioBuilder.getCurrentMockBuilder()
				.requestBuilderWithQueryParam(key, equalTo(value));
		};
	}

	protected A2<String, String> setMockQueryParamMatching() {
		return (key, value) -> {
			scenarioBuilder.getCurrentMockBuilder()
				.requestBuilderWithQueryParam(key, matching(value));
		};
	}

	protected A1<String> setMockContentType() {
		return (value) -> {
			scenarioBuilder.getCurrentMockBuilder()
				.requestBuilderWithContentType(containing(value));
		};
	}

	protected A1<DataTable> setMockResponseBodyDataTable() {
		return (dataTable) -> {
			scenarioBuilder.getCurrentMockBuilder()
				.responseBuilderWithDataTableBody(dataTable);
		};
	}

	protected A1<String> setMockResponseBodyString() {
		return (responseBody) -> {
			scenarioBuilder.getCurrentMockBuilder()
				.responseBuilderWithBody(responseBody);
		};
	}

	protected A1<String> setMockResponseContentType() {
		return (contentType) -> {
			scenarioBuilder.getCurrentMockBuilder()
				.responseBuilderWithContentType(contentType);
		};
	}

	protected A2<String, String> setMockResponseHeaderValue() {
		return (headerName, headerValue) -> {
			scenarioBuilder.getCurrentMockBuilder()
				.responseBuilderWithHeader(headerName, headerValue);
		};
	}

	protected A1<String> setMockToBeVerified() {
		return (mockName) -> {
			scenarioBuilder.setMockToBeVerified(mockName);
		};
	}

	protected A1<DataTable> setVerifyMockDataTableBody() {
		return (dataTable) -> {
			scenarioBuilder.getCurrentMockVerifier()
					.withRequestBodyEqualTo(dataTable);
		};
	}

	protected A0 setVerifyMockEmptyBody() {
		return () -> {
			scenarioBuilder.getCurrentMockVerifier()
					.withRequestBodyEmpty();
		};
	}

	protected A1<Integer> setVerifyMockInvocationCount() {
		return (count) -> {
			scenarioBuilder.getCurrentMockVerifier()
				.setExpectedMockInvocationCount(count);
		};
	}

	protected A0 setVerifyMockNotInvoked() {
		return () -> {
			scenarioBuilder.getCurrentMockVerifier()
				.setExpectedMockInvocationCount(0);
		};
	}

	protected A1<String> setVerifyMockStringBody() {
		return (requestBody) -> {
			scenarioBuilder.getCurrentMockVerifier()
					.withRequestBodyEqualTo(requestBody);
		};
	}

	protected A0 verifyMockInvocations() {
		return () -> {
			scenarioBuilder.verifyMockInvocations();
		};
	}

}
