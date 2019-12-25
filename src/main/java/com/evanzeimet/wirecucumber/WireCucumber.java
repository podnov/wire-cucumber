package com.evanzeimet.wirecucumber;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.patch;
import static com.github.tomakehurst.wiremock.client.WireMock.patchRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.evanzeimet.wirecucumber.matchers.EmeptyRequestBodyMatcher;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;

import io.cucumber.java8.En;
import io.cucumber.java8.StepdefBody.A0;
import io.cucumber.java8.StepdefBody.A1;
import io.cucumber.java8.StepdefBody.A2;

public class WireCucumber implements En {

	private static final Logger logger = LoggerFactory.getLogger(WireCucumber.class);

	private MappingBuilder currentRequestStub;
	private ResponseDefinitionBuilder currentRequestStubResponse;
	private RequestPatternBuilder currentRequestVerifyBuilder;
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

	protected A2<String, String> bootstrapRequestVerifier() {
		return (httpVerb, path) -> {
			UrlPattern urlPattern = urlEqualTo(path);

			switch (httpVerb) {
			case "DELETE":
				currentRequestVerifyBuilder = deleteRequestedFor(urlPattern);
				break;

			case "GET":
				currentRequestVerifyBuilder = getRequestedFor(urlPattern);
				break;

			case "PATCH":
				currentRequestVerifyBuilder = patchRequestedFor(urlPattern);
				break;

			case "POST":
				currentRequestVerifyBuilder = postRequestedFor(urlPattern);
				break;

			case "PUT":
				currentRequestVerifyBuilder = putRequestedFor(urlPattern);
				break;

			default:
				throw createUnexpectedHttpVerbException(httpVerb);
			}
		};
	}

	protected A2<String, String> bootstrapRequestStub() {
		return (httpVerb, path) -> {
			UrlPattern urlPattern = urlEqualTo(path);

			switch (httpVerb) {
			case "DELETE":
				currentRequestStub = delete(urlPattern);
				break;

			case "GET":
				currentRequestStub = get(urlPattern);
				break;

			case "PATCH":
				currentRequestStub = patch(urlPattern);
				break;

			case "POST":
				currentRequestStub = post(urlPattern);
				break;

			case "PUT":
				currentRequestStub = put(urlPattern);
				break;

			default:
				createUnexpectedHttpVerbException(httpVerb);
			}

		};
	}

	protected WireCucumberRuntimeException createUnexpectedHttpVerbException(String httpVerb) {
		String message = String.format("Unexpected http verb [%s]", httpVerb);
		return new WireCucumberRuntimeException(message);
	}

	protected A0 finalizeRequestStub() {
		return () -> {
			stubFor(currentRequestStub.willReturn(currentRequestStubResponse));
			currentRequestStub = null;
		};
	}

	public void init() {
		String host = "localhost";

		wireMockServer = new WireMockServer(options().dynamicPort());
		wireMockServer.start();
		int port = wireMockServer.port();
		configureFor(host, port);

		String message = String.format("wire-cucumber started on [%s:%s]", host, port);
		logger.info(message);

		Given("a stub for a {word} with url equal to {string} exists", bootstrapRequestStub());
		Given("that stub accepts {string}", setStubAccepts());
		Given("that stub will return a response with status {int}", setStubResponseStatus());
		Given("that stub response content type is {string}", setStubResponseBody());
		Given("that stub response body is {string}", setStubResponseBody());
		Given("that stub is finalized", finalizeRequestStub());
		Then("a {word} should have been sent with url equal to {string}", bootstrapRequestVerifier());
		Then("the request body should have been:", addRequestVerifierBody());
		Then("the request body should have been empty", addRequestVerifierEmptyBody());
		Then("my request is verified", verifyRequest());
	}

	protected A1<String> setStubAccepts() {
		return (headerPattern) -> {
			currentRequestStub.withHeader(ACCEPT, equalTo(headerPattern));
		};
	}

	protected A1<String> setStubResponseBody() {
		return (responseBody) -> {
			currentRequestStubResponse.withBody(responseBody);
		};
	}

	protected A1<String> setStubResponseContent() {
		return (contentType) -> {
			currentRequestStubResponse.withHeader(CONTENT_TYPE, contentType);
		};
	}

	protected A1<Integer> setStubResponseStatus() {
		return (status) -> {
			currentRequestStubResponse = aResponse().withStatus(200);
		};
	}

	protected A0 verifyRequest() {
		return () -> {
			verify(currentRequestVerifyBuilder);
		};
	}

}
