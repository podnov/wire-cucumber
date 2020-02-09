@multipleInvocations
Feature: Wire Cucumber Multiple Invocation Tests

# TODO support different responses for multiple invocations
#      - accept scenario names and map them to invocation index
#      - change invocation verifier by index to scenario names

Scenario: Multiple calls on the same mock
	Given a wire mock named "get-hello-world"
	And that wire mock handles the GET verb with a url equal to "/hello-world"
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
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should have been invoked 3 times
	And the request is verified


Scenario: Multiple calls to different mocks
	Given a wire mock named "get-hello-world"
	And that wire mock handles the GET verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	And a wire mock named "get-hello-galaxy"
	And that wire mock handles the GET verb with a url equal to "/hello-galaxy"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello Galaxy"
	And that wire mock is finalized
	When I GET the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World"
	When I GET the "hello galaxy" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello Galaxy"
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should have been invoked 1 time
	And the request is verified
	And I want to verify interactions with the wire mock named "get-hello-galaxy"
	And that mock should have been invoked 1 time
	And the request is verified


Scenario: Multiple calls verifying specific invocation index details
	Given a wire mock named "post-hello-world"
	And that wire mock handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be "Hello World"
	# invocation-1, no body:
	When I POST the "hello world" resource "default" endpoint
	Then the response status code should be 200
	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "invocation-2"
	}
	"""
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "post-hello-world"
	And that mock should have been invoked 3 times
	And the request body of invocation 0 should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request body of invocation 1 should have been empty
	And the request body of invocation 2 should have been:
	"""
	{
		"invocationName": "invocation-2"
	}
	"""
	And the request is verified


Scenario: Multiple calls verifying specific invocation index details with data tables
	Given a wire mock named "post-hello-worlds"
	And that wire mock handles the POST verb with a url equal to "/hello-worlds"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello Worlds"
	And that wire mock is finalized
	When I POST the "hello worlds" resource "default" endpoint with:
	"""
	[{"name":"given-world-1","primaryColor":"red","galaxy":"Milky Way"},{"name":"given-world-2","primaryColor":"green","galaxy":"Milky Way"},{"name":"given-world-3","primaryColor":"grey","galaxy":"Milky Way"}]
	"""
	Then the response status code should be 200
	And the response body should be "Hello Worlds"
	# invocation-1, no body:
	When I POST the "hello worlds" resource "default" endpoint
	Then the response status code should be 200
	When I POST the "hello worlds" resource "default" endpoint with:
	"""
	[{"name":"given-world-1","primaryColor":"red","galaxy":"Milky Way"},{"name":"given-world-2","primaryColor":"yellow","galaxy":"Milky Way"},{"name":"given-world-3","primaryColor":"grey","galaxy":"Milky Way"}]
	"""
	Then the response status code should be 200
	And the response body should be "Hello Worlds"
	And I want to verify interactions with the wire mock named "post-hello-worlds"
	And that mock should have been invoked 3 times
	And the request body of invocation 0 should have been these records:
	| name          | primaryColor | galaxy    |
	| given-world-1 | red          | Milky Way |
	| given-world-2 | green        | Milky Way |
	| given-world-3 | grey         | Milky Way |
	And the request body of invocation 1 should have been empty
	And the request body of invocation 2 should have been these records:
	| name          | primaryColor | galaxy    |
	| given-world-1 | red          | Milky Way |
	| given-world-2 | yellow       | Milky Way |
	| given-world-3 | grey         | Milky Way |
	And the request is verified


Scenario: Multiple calls verifying specific invocation state details
	Given a wire mock named "get-hello-world"
	And that wire mock handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock enters state "call 2"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World 2"
	And that wire mock enters state "call 3"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World 3"
	And that wire mock is finalized
	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "invocation-0"
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
		"invocationName": "invocation-2"
	}
	"""
	Then the response status code should be 200
	And the response body should be "Hello World 3"
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should have been invoked 3 times
	And the request body of state "Started" should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request body of state "call 2" should have been empty
	And the request body of state "call 3" should have been:
	"""
	{
		"invocationName": "invocation-2"
	}
	"""
	And the request is verified


Scenario: Multiple calls verifying specific invocation state details with data tables
	Given a wire mock named "get-hello-world"
	And that wire mock handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock enters state "call 2"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World 2"
	And that wire mock enters state "call 3"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World 3"
	And that wire mock is finalized
	When I POST the "hello world" resource "default" endpoint with:
	"""
	[{"name":"given-world-1","primaryColor":"red","galaxy":"Milky Way"},{"name":"given-world-2","primaryColor":"green","galaxy":"Milky Way"},{"name":"given-world-3","primaryColor":"grey","galaxy":"Milky Way"}]
	"""
	Then the response status code should be 200
	And the response body should be "Hello World"
	When I POST the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World 2"
	When I POST the "hello world" resource "default" endpoint with:
	"""
	[{"name":"given-world-1","primaryColor":"blue","galaxy":"Milky Way"},{"name":"given-world-2","primaryColor":"yellow","galaxy":"Milky Way"},{"name":"given-world-3","primaryColor":"grey","galaxy":"Milky Way"}]
	"""
	Then the response status code should be 200
	And the response body should be "Hello World 3"
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should have been invoked 3 times
	And the request body of state "Started" should have been these records:
	| name          | primaryColor | galaxy    |
	| given-world-1 | red          | Milky Way |
	| given-world-2 | green        | Milky Way |
	| given-world-3 | grey         | Milky Way |
	And the request body of state "call 2" should have been empty
	And the request body of state "call 3" should have been these records:
	| name          | primaryColor | galaxy    |
	| given-world-1 | blue         | Milky Way |
	| given-world-2 | yellow       | Milky Way |
	| given-world-3 | grey         | Milky Way |
	And the request is verified


Scenario: Multiple calls on the same mock with differing results, urls matching too
	Given a wire mock named "get-hello-world"
	And that wire mock handles the POST verb with a url matching "/hello-world/?.*"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock enters state "call 2"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World 2"
	And that wire mock enters state "call 3"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World 3"
	And that wire mock is finalized
	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be "Hello World"
	When I POST the "hello world" resource "world-two" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World 2"
	When I POST the "hello world" resource "world-three" endpoint with:
	"""
	{
		"invocationName": "invocation-2"
	}
	"""
	Then the response status code should be 200
	And the response body should be "Hello World 3"
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should have been invoked 3 times
	And the request url of state "Started" should have been "/hello-world"
	And the request body of state "Started" should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request url of state "call 2" should have been "/hello-world/world-two"
	And the request body of state "call 2" should have been empty
	And the request url of state "call 3" should have been "/hello-world/world-three"
	And the request body of state "call 3" should have been:
	"""
	{
		"invocationName": "invocation-2"
	}
	"""
	And the request is verified
