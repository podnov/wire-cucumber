package com.evanzeimet.wirecucumber;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.Options;

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

	public void initialize(Options options) {
		startWireMockServer(options);
		createSteps();
	}

	protected void startWireMockServer(Options options) {
		String host = "localhost";

		wireMockServer = new WireMockServer(options);
		wireMockServer.start();
		int port = wireMockServer.port();
		configureFor(host, port);

		String message = String.format("wire-cucumber started on [%s:%s]", host, port);
		logger.info(message);
	}

}
