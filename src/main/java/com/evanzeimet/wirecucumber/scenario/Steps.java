package com.evanzeimet.wirecucumber.scenario;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;

import com.evanzeimet.wirecucumber.WireCucumberUtils;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.cucumber.java8.HookBody;
import io.cucumber.java8.StepdefBody.A0;
import io.cucumber.java8.StepdefBody.A1;
import io.cucumber.java8.StepdefBody.A2;
import io.cucumber.java8.StepdefBody.A3;

public class Steps
		implements En {

	protected static final WireCucumberUtils utils = new WireCucumberUtils();

	protected ScenarioBuilder scenarioBuilder = new ScenarioBuilder();

	public ScenarioBuilder getScenarioBuilder() {
		return scenarioBuilder;
	}

	protected A2<Integer, DataTable> addInvocationIndexDataTableBodyVerification() {
		return (invocationIndex, dataTable) -> {
			scenarioBuilder.getInvocationVerifier()
				.addDataTableBodyVerification(invocationIndex, dataTable);
		};
	}

	protected A1<Integer> addInvocationIndexEmptyBodyVerification() {
		return (invocationIndex) -> {
			scenarioBuilder.getInvocationVerifier()
				.addEmptyBodyVerification(invocationIndex);
		};
	}

	protected A2<Integer, String> addInvocationIndexStringBodyVerification() {
		return (invocationIndex, requestBody) -> {
			scenarioBuilder.getInvocationVerifier()
				.addStringBodyVerification(invocationIndex, requestBody);
		};
	}

	protected A2<Integer, String> addInvocationIndexUrlVerification() {
		return (invocationIndex, url) -> {
			scenarioBuilder.getInvocationVerifier()
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

	protected A1<DataTable> addInvocationVerifierDataTableBody() {
		return (dataTable) -> {
			scenarioBuilder.getInvocationVerifier()
					.withRequestBodyEqualTo(dataTable);
		};
	}

	protected A0 addInvocationVerifierEmptyBody() {
		return () -> {
			scenarioBuilder.getInvocationVerifier()
					.withRequestBodyEmpty();
		};
	}

	protected A2<String, String> addInvocationVerifierHeader() {
		return (name, value) -> {
			scenarioBuilder.getInvocationVerifier()
					.withHeaderContaining(name, value);
		};
	}

	protected A1<String> addInvocationVerifierStringBody() {
		return (requestBody) -> {
			scenarioBuilder.getInvocationVerifier()
					.withRequestBodyEqualTo(requestBody);
		};
	}

	protected A1<String> addInvocationVerifierUrl() {
		return (url) -> {
			scenarioBuilder.getInvocationVerifier()
				.withUrl(url);
		};
	}

	protected HookBody beforeScenario() {
		return (scenario) -> {
			scenarioBuilder.setCurrentCucumberScenario(scenario);
		};
	}

	protected A1<Integer> bootstrapResponseBuilder() {
		return (status) -> {
			scenarioBuilder.getMockBuilder()
					.bootstrapResponseBuilder(status);
		};
	}

	protected A3<String, String, String> bootstrapUrlEqualToRequestMock() {
		return (mockName, httpVerb, path) -> {
			scenarioBuilder.bootstrapUrlEqualToRequestMock(mockName, httpVerb, path);
		};
	}

	protected A3<String, String, String> bootstrapUrlMatchingRequestMock() {
		return (mockName, httpVerb, path) -> {
			scenarioBuilder.bootstrapUrlMatchingRequestMock(mockName, httpVerb, path);
		};
	}

	protected A0 finalizeRequestMock() {
		return () -> {
			scenarioBuilder.finalizeRequestMock();
		};
	}

	public void initialize() {
		Before(beforeScenario());
		Given("a wire mock named {string} that handles (the ){word} verb with a url equal to {string}", bootstrapUrlEqualToRequestMock());
		Given("a wire mock named {string} that handles (the ){word} verb with a url matching {string}", bootstrapUrlMatchingRequestMock());
		Given("that wire mock accepts {string}", setMockAccepts());
		Given("that wire mock content type is {string}", setMockContentType());
		Given("that wire mock will return a response with status {int}", bootstrapResponseBuilder());
		Given("that wire mock response body is:", setMockResponseBodyString());
		Given("that wire mock response body is {string}", setMockResponseBodyString());
		Given("that wire mock response body is these records:", setMockResponseBodyDataTable());
		Given("that wire mock response content type is {string}", setMockResponseContentType());
		Given("that wire mock response header {string} is {string}", setMockResponseHeaderValue());
		Given("that wire mock enters state {string}", setRequestBuilderState());
		Given("that wire mock is finalized", finalizeRequestMock());

		Then("I want to verify interactions with the wire mock named {string}", setCurrentRequestVerifyBuilder());
		Then("that mock should have been invoked {int} time(s)", setVerifyMockInvocationCount());
		// TODO I'm not a huge fan of the "the request" language
		Then("the request body should have been:", addInvocationVerifierStringBody());
		Then("the request body should have been {string}", addInvocationVerifierStringBody());
		Then("the request body should have been empty", addInvocationVerifierEmptyBody());
		Then("the request body should have been these records:", addInvocationVerifierDataTableBody());
		Then("the request body of invocation {int} should have been:", addInvocationIndexStringBodyVerification());
		Then("the request body of invocation {int} should have been {string}", addInvocationIndexStringBodyVerification());
		Then("the request body of invocation {int} should have been empty", addInvocationIndexEmptyBodyVerification());
		Then("the request body of invocation {int} should have been these records:", addInvocationIndexDataTableBodyVerification());
		Then("the request body of state {string} should have been:", addInvocationStateStringBodyVerification());
		Then("the request body of state {string} should have been {string}", addInvocationStateStringBodyVerification());
		Then("the request body of state {string} should have been empty", addInvocationStateEmptyBodyVerification());
		Then("the request body of state {string} should have been these records:", addInvocationStateDataTableBodyVerification());
		Then("the request should have had header {string} {string}", addInvocationVerifierHeader());
		Then("the request url should have been {string}", addInvocationVerifierUrl());
		// TODO change these to "of invocation index" and "of invocation state"
		Then("the request url of invocation {int} should have been {string}", addInvocationIndexUrlVerification());
		Then("the request url of state {string} should have been {string}", addInvocationStateUrlVerification());
		Then("the request is verified", verifyInvocations());
	}

	protected A1<String> setRequestBuilderState() {
		return (newState) -> {
			scenarioBuilder.transitionMock(newState);
		};
	}

	protected A1<String> setCurrentRequestVerifyBuilder() {
		return (mockName) -> {
			scenarioBuilder.setCurrentRequestVerifyBuilder(mockName);
		};
	}

	protected A1<String> setMockAccepts() {
		return (value) -> {
			scenarioBuilder.getMockBuilder()
					.requestBuilderWithAccept(containing(value));
		};
	}

	protected A1<String> setMockContentType() {
		return (value) -> {
			scenarioBuilder.getMockBuilder()
					.requestBuilderWithContentType(containing(value));
		};
	}

	protected A1<DataTable> setMockResponseBodyDataTable() {
		return (dataTable) -> {
			scenarioBuilder.getMockBuilder()
				.responseBuilderWithDataTableBody(dataTable);
		};
	}

	protected A1<String> setMockResponseBodyString() {
		return (responseBody) -> {
			scenarioBuilder.getMockBuilder()
				.responseBuilderWithBody(responseBody);
		};
	}

	protected A1<String> setMockResponseContentType() {
		return (contentType) -> {
			scenarioBuilder.getMockBuilder()
				.responseBuilderWithContentType(contentType);
		};
	}

	protected A2<String, String> setMockResponseHeaderValue() {
		return (headerName, headerValue) -> {
			scenarioBuilder.getMockBuilder()
				.responseBuilderWithHeader(headerName, headerValue);
		};
	}

	protected A1<Integer> setVerifyMockInvocationCount() {
		return (count) -> {
			scenarioBuilder.getInvocationVerifier()
				.setExpectedMockInvocationCount(count);
		};
	}

	protected A0 verifyInvocations() {
		return () -> {
			scenarioBuilder.verifyInvocations();
		};
	}

}
