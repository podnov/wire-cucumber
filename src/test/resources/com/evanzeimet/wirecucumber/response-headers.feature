@responseHeaders
Feature: Wire Cucumber Response Entity Tests

Scenario: Response content-type
	Given a wire mock named "get-hello-worlds"
	And that wire mock handles the GET verb with a url equal to "/hello-worlds"
	And that wire mock will return a response with status 200
	And that wire mock response content type is "application/json"
	And that wire mock is finalized
	When I GET the "hello worlds" resource "default" endpoint
	Then the response status code should be 200
	And the response header "Content-Type" is "application/json"

Scenario: Response header generic
	Given a wire mock named "get-hello-worlds"
	And that wire mock handles the GET verb with a url equal to "/hello-worlds"
	And that wire mock will return a response with status 200
	And that wire mock response header "Content-Type" is "application/json"
	And that wire mock is finalized
	When I GET the "hello worlds" resource "default" endpoint
	Then the response status code should be 200
	And the response header "Content-Type" is "application/json"
	