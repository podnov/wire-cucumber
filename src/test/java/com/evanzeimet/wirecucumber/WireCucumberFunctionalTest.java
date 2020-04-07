package com.evanzeimet.wirecucumber;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.cucumber.java8.En;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true)
public class WireCucumberFunctionalTest implements En {

	private static final String HELLO_WORLD_URI = "/hello-world";
	private static final String HELLO_WORLDS_URI = "/hello-worlds";
	private static final String HELLO_GALAXY_URI = "/hello-galaxy";
	private static final String REQUEST_ID_HEADER = "X-request-id";

	private ValidatableResponse actualResponse;
	private String currentRequestId;

	public WireCucumberFunctionalTest() {
		TestWireCucumber wireCucumber = new TestWireCucumber();
		wireCucumber.initialize(options().dynamicPort());
		int port = wireCucumber.getWireMockServer().port();

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

		Then("the response status code should be {int}", (expectedStatusCode) -> {
			actualResponse.statusCode((int) expectedStatusCode);
		});

		Then("the response body should be {string}", (expectedResponseBody) -> {
			actualResponse.body(equalTo(expectedResponseBody));
		});

		Then("the response body should be:", (expectedResponseBody) -> {
			actualResponse.body(equalTo(expectedResponseBody));
		});

		Then("the request should have had my request id header", () -> {
			wireCucumber.getSteps()
					.getCurrentRequestVerifierBuilder()
					.withHeader(REQUEST_ID_HEADER, WireMock.equalTo(currentRequestId));
		});

		Then("verifying my request should yield this exception message:", (expectedExceptionMessage) -> {
			Throwable actualThrowable = null;

			try {
				wireCucumber.verifyRequest();
			} catch (Throwable e) {
				actualThrowable = e;
			}

			assertNotNull(actualThrowable);

			String actualExceptionMessage = TestUtils.dos2unix(actualThrowable.getMessage());
			assertEquals(expectedExceptionMessage, actualExceptionMessage);
		});

		After(() -> {
			wireCucumber.close();
		});
	}

	protected String getPath(String resource, String endpoint) {
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
			throw new WireCucumberRuntimeException(message );
		}

		boolean notBlank = StringUtils.isNotBlank(endpoint);
		boolean notDefault = !"default".equals(endpoint);

		if (notBlank && notDefault) {
			result = String.format("%s/%s", result, endpoint);
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
