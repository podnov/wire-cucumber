@requestHeaders
Feature: Wire Cucumber Headers Tests

Scenario: JSON request
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
	And that wire mock accepts "application/json"
	And that wire mock content type is "application/json"
	And that wire mock will return a response with status 200
	And that wire mock response body is:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And that wire mock is finalized
	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And I want to verify interactions with the wire mock named "post-hello-world"
	And that wire mock should have been invoked 1 time
	And the request body should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request should have had header "Content-Type" "application/json"
	And the interactions with that wire mock are verified


Scenario: JSON request
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
	And that wire mock accepts "application/json"
	And that wire mock content type is "application/json"
	And that wire mock will return a response with status 200
	And that wire mock response body is:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And that wire mock is finalized
	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And I want to verify interactions with the wire mock named "post-hello-world"
	And that wire mock should have been invoked 1 time
	And the request body should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request should have had header "Content-Type" "application/json-awesome"
	And verifying my request should yield this exception message:
	"""
	No requests exactly matched. Most similar request was:  expected:<
	POST
	/hello-world

	Accept: application/json, application/javascript, text/javascript, text/json
	Content-Type [contains] : application/json-awesome

	{
		"invocationName": "invocation-0"
	}> but was:<
	POST
	/hello-world

	Accept: application/json, application/javascript, text/javascript, text/json
	Content-Type: application/json

	{
		"invocationName": "invocation-0"
	}>
	"""
