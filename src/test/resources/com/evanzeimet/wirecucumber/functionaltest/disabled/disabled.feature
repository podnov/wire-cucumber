@disabled
Feature: Wire Cucumber Core Disabled Feature Test

Scenario: Basic Scenario
	Given a wire mock named "get-hello-world" that handles the GET verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I want to verify this scenario completes without error
	Then I should not get any errors
