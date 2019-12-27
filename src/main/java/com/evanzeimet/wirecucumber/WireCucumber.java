package com.evanzeimet.wirecucumber;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.cucumber.java8.En;

public class WireCucumber implements En, AutoCloseable {

	private static final Logger logger = LoggerFactory.getLogger(WireCucumber.class);

	protected WireCucumberSteps steps;
	protected WireMockServer wireMockServer;

	public WireMockServer getWireMockServer() {
		return wireMockServer;
	}

	public WireCucumberSteps getSteps() {
		return steps;
	}

	@Override
	public void close() throws Exception {
		if (wireMockServer != null) {
			wireMockServer.shutdownServer();
			wireMockServer = null;
		}
	}

	protected void createSteps() {
		steps = new WireCucumberSteps();
		steps.initialize();
	}

	public void initialize() {
		startWireMockServer();
		createSteps();
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

}
