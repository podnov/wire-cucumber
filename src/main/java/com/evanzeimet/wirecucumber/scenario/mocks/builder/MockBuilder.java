package com.evanzeimet.wirecucumber.scenario.mocks.builder;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.patch;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.evanzeimet.wirecucumber.WireCucumberUtils;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.ScenarioMappingBuilder;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.Scenario;

public class MockBuilder {

	protected static final WireCucumberUtils utils = new WireCucumberUtils();

	protected ScenarioMappingBuilder requestBuilder;
	protected ResponseDefinitionBuilder responseBuilder;

	public void bootstrapResponseBuilder(Integer status) {
		responseBuilder = aResponse().withStatus(status);
	}

	protected StubMapping createStubForBuilders(String scenarioState) {
		ScenarioMappingBuilder builder = requestBuilder.whenScenarioStateIs(scenarioState)
				.willReturn(responseBuilder);

		return stubFor(builder);
	}

	protected static WireCucumberRuntimeException createUnexpectedHttpVerbException(String httpVerb) {
		String message = String.format("Unexpected http verb [%s]", httpVerb);
		return new WireCucumberRuntimeException(message);
	}

	public static MockBuilder create(Scenario scenario,
			String httpVerb,
			UrlPattern urlPattern) {
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

		MockBuilder result = new MockBuilder();
		result.requestBuilder = requestBuilder.inScenario(scenario.getName());

		return result;
	}

	public StubMapping finalizeMock(String scenarioState) {
		StubMapping result = createStubForBuilders(scenarioState);

		requestBuilder = null;
		responseBuilder = null;

		return result;
	}

	public ScenarioMappingBuilder requestBuilderWithAccept(StringValuePattern value) {
		return requestBuilderWithHeader(ACCEPT, value);
	}

	public ScenarioMappingBuilder requestBuilderWithContentType(StringValuePattern value) {
		return requestBuilderWithHeader(CONTENT_TYPE, value);
	}

	public ScenarioMappingBuilder requestBuilderWithHeader(String headerName, StringValuePattern headerValuePattern) {
		return requestBuilder.withHeader(headerName, headerValuePattern);
	}

	public ScenarioMappingBuilder requestBuilderWithQueryParam(String key, StringValuePattern pattern) {
		return requestBuilder.withQueryParam(key, pattern);
	}

	public ResponseDefinitionBuilder responseBuilderWithBody(String body) {
		return responseBuilder.withBody(body);
	}

	public ResponseDefinitionBuilder responseBuilderWithBodyDataTable(DataTable dataTable) {
		String body = utils.convertDataTableToJson(dataTable);
		return responseBuilderWithBody(body);
	}

	public ResponseDefinitionBuilder responseBuilderWithBodyFile(String fileName) {
		return responseBuilder.withBodyFile(fileName);
	}

	public ResponseDefinitionBuilder responseBuilderWithContentType(String contentType) {
		return responseBuilderWithHeader(CONTENT_TYPE, contentType);
	}

	public ResponseDefinitionBuilder responseBuilderWithHeader(String headerName, String headerValue) {
		return responseBuilder.withHeader(headerName, headerValue);
	}

	public StubMapping setRequestBuilderState(String currentState, String newState) {
		requestBuilder = requestBuilder.whenScenarioStateIs(currentState)
				.willReturn(responseBuilder)
				.willSetStateTo(newState);
		StubMapping result = stubFor(requestBuilder);

		responseBuilder = null;

		return result;
	}

}
