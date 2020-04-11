@responseGeneric
Feature: Wire Cucumber Response Entity Tests

Scenario: Reponse status 500
	Given a wire mock named "get-hello-worlds"
	And that wire mock handles the GET verb with a url equal to "/hello-worlds"
	And that wire mock will return a response with status 500
	And that wire mock is finalized
	When I GET the "hello worlds" resource "default" endpoint
	Then the response status code should be 500
	And I want to verify interactions with the wire mock named "get-hello-worlds"
	And that mock should have been invoked 1 time
	And the request is verified

