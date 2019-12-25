package com.evanzeimet.wirecucumber;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.runner.RunWith;

import io.cucumber.java8.En;
import io.cucumber.junit.Cucumber;
import io.restassured.response.ValidatableResponse;

@RunWith(Cucumber.class)
public class WireCucumberTest implements En {

	private ValidatableResponse actualResponse;
	private WireCucumber wireCucumber;

	public WireCucumberTest() {
		wireCucumber = new WireCucumber();
		wireCucumber.init();

		When("I call the hello world resource", () -> {
			int port = wireCucumber.getWireMockServer().port();
			actualResponse = given()
					.port(port)
					.get("/hello-world")
					.then();
		});

		Then("the response status code should be {int}", (expectedStatusCode) -> {
			actualResponse.statusCode((int) expectedStatusCode);
		});

		Then("the response body should be {string}", (expectedResponseBody) -> {
			actualResponse.body(equalTo((String) expectedResponseBody));
		});
	}

}
