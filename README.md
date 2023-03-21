# wire-cucumber

[![Maven Central](https://img.shields.io/maven-central/v/com.evanzeimet.wirecucumber/wire-cucumber.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.evanzeimet.wirecucumber%22%20AND%20a:%22wire-cucumber%22) [<img src="https://github.com/podnov/wire-cucumber/workflows/java-ci/badge.svg">](https://github.com/podnov/wire-cucumber/actions?query=workflow%3A%22java-ci%22) [<img src="https://codecov.io/gh/podnov/wire-cucumber/branch/main/graph/badge.svg">](https://codecov.io/gh/podnov/wire-cucumber/branch/main)

## Why?
The intent of this project is to make mocking external REST calls easier. This is achieved by standardizing how the mocks are configured and reducing the scaffolding needed to create and verify invocations of those mocks.

## How?
### What steps are provided?
```
Given a wire mock named {string} that handles (the ){word} verb with a url equal to {string}
Given a wire mock named {string} that handles (the ){word} verb with a url matching {string}
Given a wire mock named {string} that handles (the ){word} verb with a url path equal to {string}
Given a wire mock named {string} that handles (the ){word} verb with a url path matching {string}

Given that wire mock expects a url query string parameter {string} equal to {string}
Given that wire mock expects a url query string parameter {string} matching {string}

Given that wire mock expects header {string} equal to {string}
Given that wire mock expects header {string} equal to {string} ignoring case
Given that wire mock expects header {string} matching {string}

Given that wire mock expects this request body:

Given that wire mock will return a response with status {int}

Given that wire mock response content type is {string}
Given that wire mock response header {string} is {string}

Given that wire mock response body is:
Given that wire mock response body is the contents of file {string}
Given that wire mock response body is {string}
Given that wire mock response body is these records:

Given the scenario enters state {string}

Given that wire mock is finalized

Then I want to verify invocations of the wire mock named {string}

Then that wire mock should not have been invoked
Then that wire mock should have been invoked {int} time(s)

Then the request url should have been {string}

Then the request should have had header {string} absent
Then the request should have had header {string} containing {string}
Then the request should have had header {string} present

Then the request body should have been:
Then the request body should have been {string}
Then the request body should have been empty
Then the request body should have been these records:

Then the request at invocation index {int} should have had url {string}

Then the request at invocation index {int} should have had header {string} absent
Then the request at invocation index {int} should have had header {string} containing {string}
Then the request at invocation index {int} should have had header {string} present

Then the request at invocation index {int} should have had body:
Then the request at invocation index {int} should have had body {string}
Then the request at invocation index {int} should have had an empty body
Then the request at invocation index {int} should have had body records:

Then the request at the default scenario state should have had url {string}

Then the request at the default scenario state should have had header {string} absent
Then the request at the default scenario state should have had header {string} containing {string}
Then the request at the default scenario state should have had header {string} present

Then the request at the default scenario state should have had body:
Then the request at the default scenario state should have had body {string}
Then the request at the default scenario state should have had an empty body
Then the request at the default scenario state should have had body records:

Then the request at the {string} scenario state should have had url {string}

Then the request at the {string} scenario state should have had header {string} absent
Then the request at the {string} scenario state should have had header {string} containing {string}
Then the request at the {string} scenario state should have had header {string} present

Then the request at the {string} scenario state should have had body:
Then the request at the {string} scenario state should have had body {string}
Then the request at the {string} scenario state should have had an empty body
Then the request at the {string} scenario state should have had body records:

Then the invocations of that wire mock are verified

Then I want to skip verifying invocations of all wire mocks
Then I want to skip verifying invocations of the wire mock named {string}
```

### Write a scenario
```
Scenario: GET Something That Calls Hello World
	# provided by wire-cucumber
	Given a wire mock named "get-hello-world" that handles the GET verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	# handled by consumer of wire-cucumber
	When I GET the something that calls hello world resource
	Then the response status code should be 200
	And the response body should be "Something Called Hello World"
	# provided by wire-cucumber
	And I want to verify invocations of the wire mock named "get-hello-world"
	And that wire mock should have been invoked 1 time
	And the invocations of that wire mock are verified
```

### Bootstrap wire-cucumber in your steps file
```
wireCucumber = new WireCucumber();
wireCucumber.startWireMockServer();
wireCucumber.createSteps();
```

### Call the service that calls the mock you configured in your scenario and verify that it behaved correctly:
```
// using REST Assured here to perform service invocation

When("I GET the something that calls hello world resource", () -> {
	actualResponse = given()
		.port(port)
		.get("/something-that-calls-hello-world")
		.then();
});

Then("the response status code should be {int}", (Integer expectedStatusCode) -> {
	actualResponse.statusCode(expectedStatusCode);
});

Then("the response body should be {string}", (String expectedResponseBody) -> {
	actualResponse.body(equalTo(expectedResponseBody));
});
```

### Cleanup wire-cucumber
```
After(() -> {
	wireCucumber.close();
});
```

### Details
Example cucumber features consuming the this library can be [found in this project](src/test/resources/com/evanzeimet/wirecucumber/functionaltest/enabled). The steps defined in this test for this project only define a [few steps](src/test/java/com/evanzeimet/wirecucumber/functionaltest/enabled/WireCucumberEnabledFunctionalTest.java). The steps that create the REST mocks and verify invocations of them are defined [in the library itself](src/main/java/com/evanzeimet/wirecucumber/scenario/Steps.java) in the `initialize` method.

## Maintainer's Corner
### How do I run specific cucumber tests?
```
CUCUMBER_FILTER_TAGS='@getVerb' ./gradlew clean test --tests **WireCucumber*FunctionalTest
CUCUMBER_FILTER_TAGS='@extensibility' ./gradlew clean test --tests **WireCucumber*FunctionalTest
CUCUMBER_FILTER_TAGS='@headers' ./gradlew clean test --tests **WireCucumber*FunctionalTest
CUCUMBER_FILTER_TAGS='@multipleInvocations' ./gradlew clean test --tests **WireCucumber*FunctionalTest
```

### How do I publish to sonatype?
```
./gradlew build
# Update CHANGELOG.md
# commit
# push main
# check github build results
# update gradle.properties version
./gradlew build
# commit version rev
# tag
./gradlew publish
# update gradle.properties version
# commit
# push main and tag
```
Visit [sonatype](https://oss.sonatype.org/#stagingRepositories), reference [guide](https://egkatzioura.com/2022/10/17/gradle-push-to-maven-repository) if needed.
