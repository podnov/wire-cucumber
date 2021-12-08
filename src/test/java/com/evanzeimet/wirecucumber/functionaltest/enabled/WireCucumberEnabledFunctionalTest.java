package com.evanzeimet.wirecucumber.functionaltest.enabled;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

import com.evanzeimet.wirecucumber.TestUtils;
import com.evanzeimet.wirecucumber.TestWireCucumber;
import com.evanzeimet.wirecucumber.WireCucumberOptions;
import com.evanzeimet.wirecucumber.WireCucumberRuntimeException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import io.cucumber.java8.En;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@RunWith(Cucumber.class)
@CucumberOptions
public class WireCucumberEnabledFunctionalTest
		implements En {

	private static final String HELLO_WORLD_URI = "/hello-world";
	private static final String HELLO_WORLDS_URI = "/hello-worlds";
	private static final String HELLO_GALAXY_URI = "/hello-galaxy";
	private static final String REQUEST_ID_HEADER = "X-request-id";

	private ValidatableResponse actualResponse;
	private String currentRequestId;
	private static TestWireCucumber wireCucumber = createWireCucumber();

	public WireCucumberEnabledFunctionalTest() {
		wireCucumber.createStepDefinitions();

		int port = wireCucumber.getWireMockServer().port();

		Before(() -> {
			wireCucumber.getWireMockServer().resetAll();
		});

		When("I DELETE the {string} resource {string} endpoint", (String resource, String endpoint) -> {
			String path = getPath(resource, endpoint);

			actualResponse = bootstrapRequest()
					.port(port)
					.delete(path)
					.then();
		});

		When("I GET the {string} resource {string} endpoint", (String resource, String endpoint) -> {
			String path = getPath(resource, endpoint);

			actualResponse = bootstrapRequest()
					.port(port)
					.get(path)
					.then();
		});

		When("I GET the {string} resource {string} endpoint and query string {string}", (String resource, String endpoint, String queryString) -> {
			String path = getPath(resource, endpoint, queryString);

			actualResponse = bootstrapRequest()
					.port(port)
					.get(path)
					.then();
		});

		When("I OPTIONS the {string} resource {string} endpoint", (String resource, String endpoint) -> {
			String path = getPath(resource, endpoint);

			actualResponse = bootstrapRequest()
					.port(port)
					.options(path)
					.then();
		});

		When("I PATCH the {string} resource {string} endpoint", (String resource, String endpoint) -> {
			String path = getPath(resource, endpoint);

			actualResponse = bootstrapRequest()
					.port(port)
					.patch(path)
					.then();
		});

		When("I PUT the {string} resource {string} endpoint", (String resource, String endpoint) -> {
			String path = getPath(resource, endpoint);

			actualResponse = bootstrapRequest()
					.port(port)
					.put(path)
					.then();
		});

		When("I POST the {string} resource {string} endpoint", (String resource, String endpoint) -> {
			String path = getPath(resource, endpoint);

			actualResponse = bootstrapRequest()
					.port(port)
					.post(path)
					.then();
		});

		When("I POST the hello worlds resource", (String resource, String endpoint) -> {
			String path = getPath(resource, endpoint);

			actualResponse = bootstrapRequest()
					.port(port)
					.post(path)
					.then();
		});

		When("I POST the {string} resource {string} endpoint with:", (String resource, String endpoint, String requestBody) -> {
			String path = getPath(resource, endpoint);

			actualResponse = bootstrapRequest()
					.port(port)
					.accept(JSON)
					.contentType(JSON)
					.body(requestBody)
					.post(path)
					.then();
		});

		Then("the response status code should be {int}", (Integer expectedStatusCode) -> {
			actualResponse.statusCode(expectedStatusCode);
		});

		Then("the response header {string} is {string}", (String expectedHeaderName, String expectedHeaderValue) -> {
			actualResponse.header(expectedHeaderName, expectedHeaderValue);
		});

		Then("the response body should be {string}", (String expectedResponseBody) -> {
			actualResponse.body(equalTo(expectedResponseBody));
		});

		Then("the response body should be:", (String expectedResponseBody) -> {
			actualResponse.body(equalTo(expectedResponseBody));
		});

		Then("the request should have had my request id header", () -> {
			wireCucumber.getStepDefinitions()
					.getScenarioBuilder()
					.getCurrentMockInvocationsVerifier()
					.withHeader(REQUEST_ID_HEADER, WireMock.equalTo(currentRequestId));
		});

		Then("verifying my request should yield this exception message:", (String expectedExceptionMessage) -> {
			Throwable actualThrowable = null;

			try {
				wireCucumber.verifyInvocations();
			} catch (Throwable e) {
				actualThrowable = e;
			}

			assertNotNull(actualThrowable);

			String actualExceptionMessage = TestUtils.dos2unix(actualThrowable.getMessage());
			actualExceptionMessage = scrubMatchingExceptionMessage(actualExceptionMessage);
			assertEquals(expectedExceptionMessage, actualExceptionMessage);

			wireCucumber.getStepDefinitions()
					.getScenarioBuilder()
					.setCurrentMockInvocationsVerified();
		});

	}

	@AfterClass
	public static void afterClass() {
		wireCucumber.close();
	}

	protected static TestWireCucumber createWireCucumber() {
		WireMockConfiguration wireMockConfiguration = options().dynamicPort();

		WireCucumberOptions options = new WireCucumberOptions();
		options.setIsDisabled(false);
		options.setWireMockOptions(wireMockConfiguration);

		TestWireCucumber result = new TestWireCucumber(options);
		result.startWireMockServer();

		return result;
	}

	protected String scrubMatchingExceptionMessage(String actualExceptionMessage) {
		String result = actualExceptionMessage;

		// scrub elements expected to be uniq per run
		result = result.replaceAll("    \"Host\" : \"[^\"]+\",\\n", "");
		result = result.replaceAll("    \"X-request-id\" : \"[^\"]+\",\\n", "");
		result = result.replaceAll("    \"User-Agent\" : \"[^\"]+\",\\n", "");

		return result;
	}

	protected String getPath(String resource, String endpoint) {
		return getPath(resource, endpoint, null);
	}

	protected String getPath(String resource, String endpoint, String queryString) {
		String result;

		switch (resource) {
		case "hello galaxy":
			result = HELLO_GALAXY_URI;
			break;

		case "hello world":
			result = HELLO_WORLD_URI;
			break;

		case "hello worlds":
			result = HELLO_WORLDS_URI;
			break;

		default:
			String message = String.format("Resource [%s] unsupported", resource);
			throw new WireCucumberRuntimeException(message);
		}

		boolean notBlank = StringUtils.isNotBlank(endpoint);
		boolean notDefault = !"default".equals(endpoint);

		if (notBlank && notDefault) {
			result = String.format("%s/%s", result, endpoint);
		}

		if (StringUtils.isNotBlank(queryString)) {
			result = String.format("%s?%s", result, queryString);
		}

		return result;
	}

	protected RequestSpecification bootstrapRequest() {
		currentRequestId = createRequestId();
		return given().header(REQUEST_ID_HEADER, currentRequestId);
	}

	protected String createRequestId() {
		return UUID.randomUUID().toString();
	}

}
