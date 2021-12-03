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

	protected WireCucumberOptions createDefaultWireCucumberOptions() {
		return new WireCucumberOptions();
	}

	public void createSteps() {
		WireCucumberOptions options = createDefaultWireCucumberOptions();
		createSteps(options);
	}

	public void createSteps(WireCucumberOptions options) {
		steps = new Steps();
		steps.initialize(options);
	}

	public void startWireMockServer() {
		WireCucumberOptions options = createDefaultWireCucumberOptions();
		startWireMockServer(options);
	}

	public void startWireMockServer(WireCucumberOptions options) {
		Options wireMockOptions = options.getWireMockOptions();
		wireMockServer = new WireMockServer(wireMockOptions);
		wireMockServer.start();

		int port = wireMockServer.port();
		configureFor(host, port);

		String message = String.format("wire-cucumber started on [%s:%s]", host, port);
		logger.info(message);
	}

}
