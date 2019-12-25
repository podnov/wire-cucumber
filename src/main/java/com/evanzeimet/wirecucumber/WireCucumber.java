package com.evanzeimet.wirecucumber;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.patch;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.evanzeimet.wirecucumber.matchers.EmeptyRequestBodyMatcher;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import io.cucumber.java8.En;
import io.cucumber.java8.StepdefBody.A0;
import io.cucumber.java8.StepdefBody.A1;
import io.cucumber.java8.StepdefBody.A2;

public class WireCucumber implements En, AutoCloseable {

	private static final Logger logger = LoggerFactory.getLogger(WireCucumber.class);

	private String currentMockName;
	private MappingBuilder currentRequestBuilder;
	private RequestPatternBuilder currentRequestVerifyBuilder;
	private ResponseDefinitionBuilder currentResponseBuilder;
	private Integer expectedMockInvocationCount;
	private Map<String, StubMapping> namedMocks = new HashMap<>();
	private WireMockServer wireMockServer;

	public WireMockServer getWireMockServer() {
		return wireMockServer;
	}

	protected A1<String> addRequestVerifierBody() {
		return (requestBody) -> {
			currentRequestVerifyBuilder.withRequestBody(equalTo(requestBody));
		};
	}

	protected A0 addRequestVerifierEmptyBody() {
		return () -> {
			currentRequestVerifyBuilder.andMatching(new EmeptyRequestBodyMatcher());
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
				createUnexpectedHttpVerbException(httpVerb);
			}
		};
	}

	@Override
	public void close() throws Exception {
		if (wireMockServer != null) {
			wireMockServer.shutdownServer();
		}
	}

	protected void createSteps() {
		Given("a wire mock named {string}", setMockName());
		Given("that wire mock handles the {word} verb with a url equal to {string}", bootstrapRequestMock());
		Given("that wire mock accepts {string}", setMockAccepts());
		Given("that wire mock content type is {string}", setMockContentType());
		Given("that wire mock will return a response with status {int}", setMockResponseStatus());
		Given("that wire mock response content type is {string}", setMockResponseBody());
		Given("that wire mock response body is {string}", setMockResponseBody());
		Given("that wire mock is finalized", finalizeRequestMock());

		Then("I want to verify interactions with the wire mock named {string}", setCurrentRequestVerifyBuilder());
		Then("that mock should have been invoked {int} time(s)", setVerifyMockInvocationCount());
		Then("the request body should have been:", addRequestVerifierBody());
		Then("the request body should have been empty", addRequestVerifierEmptyBody());
		Then("my request is verified", verifyRequest());
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

	public void initialize() {
		startWireMockServer();
		createSteps();
	}

	protected A1<String> setCurrentRequestVerifyBuilder() {
		return (name) -> {
			StubMapping stubMapping = namedMocks.get(name);
			if (stubMapping == null) {
				String message = String.format("No mock found for name [%s]", name);
				throw new WireCucumberRuntimeException(message);
			}

			RequestPattern request = stubMapping.getRequest();
			currentRequestVerifyBuilder = RequestPatternBuilder.like(request);
		};
	}

	protected A1<String> setMockName() {
		return (name) -> {
			currentMockName = name;
		};
	}

	protected A1<String> setMockAccepts() {
		return (value) -> {
			currentRequestBuilder.withHeader(ACCEPT, equalTo(value));
		};
	}

	protected A1<String> setMockContentType() {
		return (value) -> {
			currentRequestBuilder.withHeader(CONTENT_TYPE, equalTo(value));
		};
	}

	protected A1<String> setMockResponseBody() {
		return (responseBody) -> {
			currentResponseBuilder.withBody(responseBody);
		};
	}

	protected A1<Integer> setMockResponseStatus() {
		return (status) -> {
			currentResponseBuilder = aResponse().withStatus(200);
		};
	}

	protected A1<Integer> setVerifyMockInvocationCount() {
		return (count) -> {
			expectedMockInvocationCount = count;
		};
	}

	protected void startWireMockServer() {
		String host = "localhost";

		wireMockServer = new WireMockServer(options().dynamicPort());
		wireMockServer.start();
		int port = wireMockServer.port();
		configureFor(host, port);

		String message = String.format("wire-cucumber started on [%s:%s]", host, port);
		logger.info(message);
	}

	protected A0 verifyRequest() {
		return () -> {
			verify(expectedMockInvocationCount, currentRequestVerifyBuilder);
		};
	}

}
