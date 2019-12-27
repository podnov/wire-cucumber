package com.evanzeimet.wirecucumber;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;

import java.util.UUID;

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
	private static final String HELLO_GALAXY_URI = "/hello-galaxy";
	private static final String REQUEST_ID_HEADER = "X-request-id";

	private ValidatableResponse actualResponse;
	private String currentRequestId;
	private WireCucumber wireCucumber;

	public WireCucumberFunctionalTest() {
		wireCucumber = new WireCucumber();
		wireCucumber.initialize();
		int port = wireCucumber.getWireMockServer().port();

		When("I DELETE the hello world resource", () -> {
			actualResponse = bootstrapRequest()
					.port(port)
					.delete(HELLO_WORLD_URI)
					.then();
		});

		When("I GET the hello world resource", () -> {
			actualResponse = bootstrapRequest()
					.port(port)
					.get(HELLO_WORLD_URI)
					.then();
		});

		When("I PATCH the hello world resource", () -> {
			actualResponse = bootstrapRequest()
					.port(port)
					.patch(HELLO_WORLD_URI)
					.then();
		});

		When("I PUT the hello world resource", () -> {
			actualResponse = bootstrapRequest()
					.port(port)
					.put(HELLO_WORLD_URI)
					.then();
		});

		When("I POST the hello world resource", () -> {
			actualResponse = bootstrapRequest()
					.port(port)
					.post(HELLO_WORLD_URI)
					.then();
		});

		When("I POST the hello world resource with:", (requestBody) -> {
			actualResponse = bootstrapRequest()
					.port(port)
					.accept(JSON)
					.contentType(JSON)
					.body((String) requestBody)
					.post(HELLO_WORLD_URI)
					.then();
		});

		When("I GET the hello galaxy resource", () -> {
			actualResponse = bootstrapRequest()
					.port(port)
					.get(HELLO_GALAXY_URI)
					.then();
		});

		Then("the response status code should be {int}", (expectedStatusCode) -> {
			actualResponse.statusCode((int) expectedStatusCode);
		});

		Then("the response body should be {string}", (expectedResponseBody) -> {
			actualResponse.body(equalTo((String) expectedResponseBody));
		});

		Then("the response body should be:", (expectedResponseBody) -> {
			actualResponse.body(equalTo((String) expectedResponseBody));
		});

		Then("the request should have had my request id header", () -> {
			wireCucumber.getSteps()
					.getCurrentRequestVerifierBuilder()
					.withHeader(REQUEST_ID_HEADER, WireMock.equalTo(currentRequestId));
		});

		After(() -> {
			wireCucumber.close();
		});

	}

	protected RequestSpecification bootstrapRequest() {
		currentRequestId = createRequestId();
		return given().header(REQUEST_ID_HEADER, currentRequestId);
	}

	protected String createRequestId() {
		return UUID.randomUUID().toString();
	}

}
