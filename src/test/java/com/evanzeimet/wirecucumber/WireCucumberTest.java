package com.evanzeimet.wirecucumber;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.runner.RunWith;

import io.cucumber.java8.En;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.restassured.response.ValidatableResponse;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true)
public class WireCucumberTest implements En {

	private ValidatableResponse actualResponse;
	private WireCucumber wireCucumber;

	public WireCucumberTest() {
		wireCucumber = new WireCucumber();
		wireCucumber.initialize();
		int port = wireCucumber.getWireMockServer().port();

		When("I GET the hello world resource", () -> {
			actualResponse = given()
					.port(port)
					.get("/hello-world")
					.then();
		});

		When("I POST the hello world resource", () -> {
			actualResponse = given()
					.port(port)
					.post("/hello-world")
					.then();
		});

		When("I POST the hello world resource with:", (requestBody) -> {
			actualResponse = given()
					.port(port)
					.body((String) requestBody)
					.post("/hello-world")
					.then();
		});

		When("I GET the hello galaxy resource", () -> {
			actualResponse = given()
					.port(port)
					.get("/hello-galaxy")
					.then();
		});

		Then("the response status code should be {int}", (expectedStatusCode) -> {
			actualResponse.statusCode((int) expectedStatusCode);
		});

		Then("the response body should be {string}", (expectedResponseBody) -> {
			actualResponse.body(equalTo((String) expectedResponseBody));
		});

		After(() -> {
			wireCucumber.close();
		});

	}

}
