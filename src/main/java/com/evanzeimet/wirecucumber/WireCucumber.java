package com.evanzeimet.wirecucumber;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.evanzeimet.wirecucumber.scenario.Steps;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.Options;

public class WireCucumber
		implements AutoCloseable {

	protected static final String host = "localhost";

	private static final Logger logger = LoggerFactory.getLogger(WireCucumber.class);

	protected Steps steps;
	protected WireMockServer wireMockServer;


	public WireMockServer getWireMockServer() {
		return wireMockServer;
	}

	public Steps getSteps() {
		return steps;
	}

	@Override
	public void close() throws Exception {
		if (wireMockServer != null) {
			wireMockServer.shutdownServer();
			wireMockServer = null;
		}
	}

	protected void createSteps(WireCucumberOptions options) {
		steps = new Steps();
		steps.initialize(options);
	}

	public void initialize(WireCucumberOptions options) {
		if (options == null) {
			options = new WireCucumberOptions();
		}

		startWireMockServer(options);
		createSteps(options);
	}

	protected void startWireMockServer(WireCucumberOptions options) {
		Options wireMockOptions = options.getWireMockOptions();
		wireMockServer = new WireMockServer(wireMockOptions);
		wireMockServer.start();

		int port = wireMockServer.port();
		configureFor(host, port);

		String message = String.format("wire-cucumber started on [%s:%s]", host, port);
		logger.info(message);
	}

}
