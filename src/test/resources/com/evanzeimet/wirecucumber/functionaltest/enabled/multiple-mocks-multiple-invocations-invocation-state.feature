@multipleMocksMultipleInvocations
Feature: Wire Cucumber Multiple Mocks with Multiple Invocation Tests

@current
Scenario: Multiple mocks with calls verifying specific invocation state details
	Given a wire mock named "get-hello-world" that handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And the scenario enters state "world call 2"

	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World 2"
	And the scenario enters state "world call 3"

	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World 3"
	And that wire mock is finalized

	Given a wire mock named "get-hello-galaxy" that handles the POST verb with a url equal to "/hello-galaxy"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello Galaxy"
	And the scenario enters state "galaxy call 2"

	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello Galaxy 2"
	And the scenario enters state "galaxy call 3"

	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello Galaxy 3"
	And that wire mock is finalized

	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "world-invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be "Hello World"

	When I POST the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World 2"

	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "world-invocation-2"
	}
	"""
	Then the response status code should be 200
	And the response body should be "Hello World 3"

	When I POST the "hello galaxy" resource "default" endpoint with:
	"""
	{
		"invocationName": "galaxy-invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be "Hello Galaxy"

	When I POST the "hello galaxy" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello Galaxy 2"

	When I POST the "hello galaxy" resource "default" endpoint with:
	"""
	{
		"invocationName": "galaxy-invocation-2"
	}
	"""
	Then the response status code should be 200
	And the response body should be "Hello Galaxy 3"

	And I want to verify invocations of the wire mock named "get-hello-world"
	And that wire mock should have been invoked 3 times
	And the request at the default scenario state should have had body:
	"""
	{
		"invocationName": "world-invocation-0"
	}
	"""
	And the request at the default scenario state should have had header "Content-Type" present
	And the request at the "world call 2" scenario state should have had an empty body
	And the request at the "world call 3" scenario state should have had body:
	"""
	{
		"invocationName": "world-invocation-2"
	}
	"""
	And the request at the "world call 3" scenario state should have had header "Content-Type" containing "application/json"
	And the request at the "world call 3" scenario state should have had header "Content-Type-O" absent
	And the invocations of that wire mock are verified

	And I want to verify invocations of the wire mock named "get-hello-galaxy"
	And that wire mock should have been invoked 3 times
	And the request at the default scenario state should have had body:
	"""
	{
		"invocationName": "galaxy-invocation-0"
	}
	"""
	And the request at the default scenario state should have had header "Content-Type" present
	And the request at the "galaxy call 2" scenario state should have had an empty body
	And the request at the "galaxy call 3" scenario state should have had body:
	"""
	{
		"invocationName": "galaxy-invocation-2"
	}
	"""
	And the request at the "galaxy call 3" scenario state should have had header "Content-Type" containing "application/json"
	And the request at the "galaxy call 3" scenario state should have had header "Content-Type-O" absent
	And the invocations of that wire mock are verified
