@multipleInvocations
Feature: Wire Cucumber Multiple Invocation Tests

Scenario: Multiple calls on the same mock
	Given a wire mock named "get-hello-world" that handles the GET verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I GET the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World"
	When I GET the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World"
	When I GET the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify invocations of the wire mock named "get-hello-world"
	And that wire mock should have been invoked 3 times
	And the invocations of that wire mock are verified


Scenario: Multiple calls to different mocks
	Given a wire mock named "get-hello-world" that handles the GET verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	And a wire mock named "get-hello-galaxy" that handles the GET verb with a url equal to "/hello-galaxy"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello Galaxy"
	And that wire mock is finalized
	When I GET the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World"
	When I GET the "hello galaxy" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello Galaxy"
	And I want to verify invocations of the wire mock named "get-hello-world"
	And that wire mock should have been invoked 1 time
	And the invocations of that wire mock are verified
	And I want to verify invocations of the wire mock named "get-hello-galaxy"
	And that wire mock should have been invoked 1 time
	And the invocations of that wire mock are verified
