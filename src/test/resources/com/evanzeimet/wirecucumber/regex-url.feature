@regexUrl
Feature: Wire Cucumber Regex URL Tests


Scenario: Mock based on regex url, default endpoint
	Given a wire mock named "get-hello-world"
	And that wire mock handles the GET verb with a url matching "/hello-world/?.*"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I GET the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should have been invoked 1 time
	And the request url should have been "/hello-world"
	And the request is verified


Scenario: Mock based on regex url, other endpoint
	Given a wire mock named "get-hello-world"
	And that wire mock handles the GET verb with a url matching "/hello-world/?.*"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I GET the "hello world" resource "abc-def/hij" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should have been invoked 1 time
	And the request url should have been "/hello-world/abc-def/hij"
	And the request is verified


Scenario: Wrong url
	Given a wire mock named "get-hello-world"
	And that wire mock handles the GET verb with a url matching "/hello-world/?.*"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I GET the "hello world" resource "abc-def/hij" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should have been invoked 1 time
	And the request url should have been "/hello-world/abc-def/bad"
	And verifying my request should yield this exception message:
	"""
	No requests exactly matched. Most similar request was:  expected:<
	GET
	/hello-world/abc-def/bad
	> but was:<
	GET
	/hello-world/abc-def/hij
	>
	"""
