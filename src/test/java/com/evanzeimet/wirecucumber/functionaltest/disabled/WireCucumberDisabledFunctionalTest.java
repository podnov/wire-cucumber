package com.evanzeimet.wirecucumber.functionaltest.disabled;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import com.evanzeimet.wirecucumber.TestWireCucumber;
import com.evanzeimet.wirecucumber.WireCucumberOptions;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import io.cucumber.java8.En;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions
public class WireCucumberDisabledFunctionalTest
		implements En {

	private static TestWireCucumber wireCucumber = createWireCucumber();

	public WireCucumberDisabledFunctionalTest() {
		wireCucumber.createStepDefinitions();

		Before(() -> {
			wireCucumber.resetWireMockServer();
		});

		When("I want to verify this scenario completes without error", () -> {
			// no-op
		});

		Then("I should not get any errors", () -> {
			// no-op
		});

	}

	@AfterClass
	public static void afterClass() {
		wireCucumber.close();
	}

	protected static TestWireCucumber createWireCucumber() {
		WireMockConfiguration wireMockConfiguration = options().dynamicPort();

		WireCucumberOptions options = new WireCucumberOptions();
		options.setIsDisabled(true);
		options.setWireMockOptions(wireMockConfiguration);

		TestWireCucumber result = new TestWireCucumber(options);
		result.startWireMockServer();

		return result;
	}

}
