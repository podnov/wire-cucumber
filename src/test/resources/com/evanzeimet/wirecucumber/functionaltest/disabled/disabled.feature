@disabled
Feature: Wire Cucumber Core Disabled Feature Test

Scenario: Basic Scenario
	Given a wire mock named "get-hello-world" that handles the GET verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 500
	And the scenario enters state "recovered"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I want to verify this scenario completes without error
	Then I should not get any errors

Scenario: Mock verification scenario
	Given a wire mock named "get-hello-world" that handles the GET verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	And I want to verify invocations of the wire mock named "any-hello-world"
	And that wire mock should have been invoked 1 time
	And the invocations of that wire mock are verified
	When I want to verify this scenario completes without error
	Then I should not get any errors
