@responseHeaders
Feature: Wire Cucumber Response Headers Tests

Scenario: Response content-type
	Given a wire mock named "get-hello-worlds" that handles the GET verb with a url equal to "/hello-worlds"
	And that wire mock will return a response with status 200
	And that wire mock response content type is "application/json"
	And that wire mock is finalized
	When I GET the "hello worlds" resource "default" endpoint
	Then the response status code should be 200
	And the response header "Content-Type" is "application/json"
	And I want to verify interactions with the wire mock named "get-hello-worlds"
	And that mock should have been invoked 1 times
	And the request is verified

Scenario: Response header generic
	Given a wire mock named "get-hello-worlds" that handles the GET verb with a url equal to "/hello-worlds"
	And that wire mock will return a response with status 200
	And that wire mock response header "Content-Type" is "application/json"
	And that wire mock is finalized
	When I GET the "hello worlds" resource "default" endpoint
	Then the response status code should be 200
	And the response header "Content-Type" is "application/json"
	And I want to verify interactions with the wire mock named "get-hello-worlds"
	And that mock should have been invoked 1 times
	And the request is verified
	