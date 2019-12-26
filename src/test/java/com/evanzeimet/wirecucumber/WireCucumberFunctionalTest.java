package com.evanzeimet.wirecucumber;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;

import org.junit.runner.RunWith;

import io.cucumber.java8.En;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.restassured.response.ValidatableResponse;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true)
public class WireCucumberFunctionalTest implements En {

	private ValidatableResponse actualResponse;
	private WireCucumber wireCucumber;

	public WireCucumberFunctionalTest() {
		wireCucumber = new WireCucumber();
		wireCucumber.initialize();
		int port = wireCucumber.getWireMockServer().port();

		When("I DELETE the hello world resource", () -> {
			actualResponse = given()
					.port(port)
					.delete("/hello-world")
					.then();
		});

		When("I GET the hello world resource", () -> {
			actualResponse = given()
					.port(port)
					.get("/hello-world")
					.then();
		});

		When("I PATCH the hello world resource", () -> {
			actualResponse = given()
					.port(port)
					.patch("/hello-world")
					.then();
		});

		When("I PUT the hello world resource", () -> {
			actualResponse = given()
					.port(port)
					.put("/hello-world")
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
					.accept(JSON)
					.contentType(JSON)
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

		Then("the response body should be:", (expectedResponseBody) -> {
			actualResponse.body(equalTo((String) expectedResponseBody));
		});

		After(() -> {
			wireCucumber.close();
		});

	}

}
