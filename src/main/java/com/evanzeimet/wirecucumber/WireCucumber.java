package com.evanzeimet.wirecucumber;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;

import io.cucumber.java8.En;

public class WireCucumber implements En {

	private static final Logger logger = LoggerFactory.getLogger(WireCucumber.class);

	private MappingBuilder currentStub;
	private ResponseDefinitionBuilder currentStubResponse;
	private WireMockServer wireMockServer;

	public WireMockServer getWireMockServer() {
		return wireMockServer;
	}

	public void init() {
		String host = "localhost";

		wireMockServer = new WireMockServer(options().dynamicPort());
		wireMockServer.start();
		int port = wireMockServer.port();
		configureFor(host, port);

		String message = String.format("wire-cucumber started on [%s:%s]", host, port);
		logger.info(message);

		Given("a stub for a GET with url equal to {string} exists", (path) -> {
			currentStub = get(urlEqualTo((String) path));
		});

		Given("that stub accepts {string}", (headerPattern) -> {
			currentStub.withHeader(ACCEPT, equalTo((String) headerPattern));
		});

		Given("that stub will return a response with status {int}", (status) -> {
			currentStubResponse = aResponse().withStatus(200);
		});

		Given("that stub response content type is {string}", (contentType) -> {
			currentStubResponse.withHeader(CONTENT_TYPE, (String) contentType);
		});

		Given("that stub response body is {string}", (responseBody) -> {
			currentStubResponse.withBody((String) responseBody);
		});

		Given("that stub is configured", () -> {
			stubFor(currentStub.willReturn(currentStubResponse));
		});

	}

}
