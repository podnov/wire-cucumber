package com.evanzeimet.wirecucumber;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.Options;

public class WireCucumber
		implements AutoCloseable {

	protected static final String host = "localhost";

	private static final Logger logger = LoggerFactory.getLogger(WireCucumber.class);

	protected WireCucumberOptions options;
	protected WireCucumberStepDefinitions stepDefinitions;
	protected WireMockServer wireMockServer;

	public WireCucumber() {
		this(createDefaultOptions());
	}

	public WireCucumber(WireCucumberOptions options) {
		this.options = options;
	}

	public WireCucumberStepDefinitions getStepDefinitions() {
		return stepDefinitions;
	}

	public WireMockServer getWireMockServer() {
		return wireMockServer;
	}

	@Override
	public void close() {
		if (wireMockServer != null) {
			wireMockServer.shutdownServer();
			wireMockServer = null;
		}
	}

	protected static WireCucumberOptions createDefaultOptions() {
		return new WireCucumberOptions();
	}

	public void createStepDefinitions() {
		stepDefinitions = new WireCucumberStepDefinitions();
		stepDefinitions.initialize(options);
	}

	public void resetWireMockServer() {
		boolean isDisabled = options.getIsDisabled();

		if (!isDisabled) {
			getWireMockServer().resetAll();
		}
	}

	public void startWireMockServer() {
		if (options.getIsDisabled()) {
			logger.info("Skipping wire-cucumber start due to being disabled");
		} else {
			Options wireMockOptions = options.getWireMockOptions();
			wireMockServer = new WireMockServer(wireMockOptions);
			wireMockServer.start();

			int port = wireMockServer.port();
			configureFor(host, port);

			String message = String.format("wire-cucumber started on [%s:%s]", host, port);
			logger.info(message);
		}
	}

}
