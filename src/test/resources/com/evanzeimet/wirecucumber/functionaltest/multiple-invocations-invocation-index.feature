@multipleInvocations
Feature: Wire Cucumber Multiple Invocation Tests


Scenario: Multiple calls verifying specific invocation index details
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
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
	And the request body of invocation index 0 should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request body of invocation index 1 should have been empty
	And the request body of invocation index 2 should have been:
	"""
	{
		"invocationName": "invocation-2"
	}
	"""
	And the request is verified


Scenario: Multiple calls verifying specific invocation index details with data tables
	Given a wire mock named "post-hello-worlds" that handles the POST verb with a url equal to "/hello-worlds"
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
	And the request body of invocation index 0 should have been these records:
	| name          | primaryColor | galaxy    |
	| given-world-1 | red          | Milky Way |
	| given-world-2 | green        | Milky Way |
	| given-world-3 | grey         | Milky Way |
	And the request body of invocation index 1 should have been empty
	And the request body of invocation index 2 should have been these records:
	| name          | primaryColor | galaxy    |
	| given-world-1 | red          | Milky Way |
	| given-world-2 | yellow       | Milky Way |
	| given-world-3 | grey         | Milky Way |
	And the request is verified

@current
Scenario: Multiple calls on the same mock with differing results, invocation index urls matching too
	Given a wire mock named "get-hello-world" that handles the POST verb with a url matching "/hello-world/?.*"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock enters invocation state "call 2"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World 2"
	And that wire mock enters invocation state "call 3"
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
	And the request url of invocation index 0 should have been "/hello-world"
	And the request body of invocation index 0 should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request url of invocation index 1 should have been "/hello-world/world-two"
	And the request body of invocation index 1 should have been empty
	And the request url of invocation index 2 should have been "/hello-world/world-three"
	And the request body of invocation index 2 should have been:
	"""
	{
		"invocationName": "invocation-2"
	}
	"""
	And the request is verified


Scenario: Invocation index failures
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
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
	And the request body of invocation index 0 should have been:
	"""
	{
		"invocationName": "invocation-42"
	}
	"""
	And the request body of invocation index 1 should have been empty
	And the request body of invocation index 2 should have been:
	"""
	{
		"invocationName": "invocation-43"
	}
	"""
	And verifying my request should yield this exception message:
	"""
	for invocation at index 0, expected:<
	{
	  "url" : "/hello-world",
	  "method" : "POST",
	  "bodyPatterns" : [ {
	    "equalTo" : "{\n\t\"invocationName\": \"invocation-42\"\n}"
	  } ]
	}> but was:<
	{
	  "url" : "/hello-world",
	  "method" : "POST",
	  "body" : "{\n\t\"invocationName\": \"invocation-0\"\n}"
	}>
	for invocation at index 2, expected:<
	{
	  "url" : "/hello-world",
	  "method" : "POST",
	  "bodyPatterns" : [ {
	    "equalTo" : "{\n\t\"invocationName\": \"invocation-43\"\n}"
	  } ]
	}> but was:<
	{
	  "url" : "/hello-world",
	  "method" : "POST",
	  "body" : "{\n\t\"invocationName\": \"invocation-2\"\n}"
	}>
	"""


Scenario: Invocation index details with data tables failures
	Given a wire mock named "post-hello-worlds" that handles the POST verb with a url equal to "/hello-worlds"
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
	And the request body of invocation index 0 should have been these records:
	| name          | primaryColor | galaxy    |
	| given-world-1 | red          | Milky Way |
	| given-world-2 | fuchsia      | Milky Way |
	| given-world-3 | grey         | Milky Way |
	And the request body of invocation index 1 should have been empty
	And the request body of invocation index 2 should have been these records:
	| name          | primaryColor | galaxy    |
	| given-world-1 | red          | Milky Way |
	| given-world-2 | fuchsia      | Milky Way |
	| given-world-3 | grey         | Milky Way |
	And verifying my request should yield this exception message:
	"""
	for invocation at index 0, expected:<
	{
	  "url" : "/hello-worlds",
	  "method" : "POST",
	  "bodyPatterns" : [ {
	    "equalTo" : "[{\"name\":\"given-world-1\",\"primaryColor\":\"red\",\"galaxy\":\"Milky Way\"},{\"name\":\"given-world-2\",\"primaryColor\":\"fuchsia\",\"galaxy\":\"Milky Way\"},{\"name\":\"given-world-3\",\"primaryColor\":\"grey\",\"galaxy\":\"Milky Way\"}]"
	  } ]
	}> but was:<
	{
	  "url" : "/hello-worlds",
	  "method" : "POST",
	  "body" : "[{\"name\":\"given-world-1\",\"primaryColor\":\"red\",\"galaxy\":\"Milky Way\"},{\"name\":\"given-world-2\",\"primaryColor\":\"green\",\"galaxy\":\"Milky Way\"},{\"name\":\"given-world-3\",\"primaryColor\":\"grey\",\"galaxy\":\"Milky Way\"}]"
	}>
	for invocation at index 2, expected:<
	{
	  "url" : "/hello-worlds",
	  "method" : "POST",
	  "bodyPatterns" : [ {
	    "equalTo" : "[{\"name\":\"given-world-1\",\"primaryColor\":\"red\",\"galaxy\":\"Milky Way\"},{\"name\":\"given-world-2\",\"primaryColor\":\"fuchsia\",\"galaxy\":\"Milky Way\"},{\"name\":\"given-world-3\",\"primaryColor\":\"grey\",\"galaxy\":\"Milky Way\"}]"
	  } ]
	}> but was:<
	{
	  "url" : "/hello-worlds",
	  "method" : "POST",
	  "body" : "[{\"name\":\"given-world-1\",\"primaryColor\":\"red\",\"galaxy\":\"Milky Way\"},{\"name\":\"given-world-2\",\"primaryColor\":\"yellow\",\"galaxy\":\"Milky Way\"},{\"name\":\"given-world-3\",\"primaryColor\":\"grey\",\"galaxy\":\"Milky Way\"}]"
	}>
	"""

@current
Scenario: invocation index urls matching failures
	Given a wire mock named "get-hello-world" that handles the POST verb with a url matching "/hello-world/?.*"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock enters invocation state "call 2"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World 2"
	And that wire mock enters invocation state "call 3"
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
	And the request url of invocation index 0 should have been "/hello-world"
	And the request body of invocation index 0 should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request url of invocation index 1 should have been "/hello-world/galaxy-two"
	And the request body of invocation index 1 should have been empty
	And the request url of invocation index 2 should have been "/hello-world/galayx-three"
	And the request body of invocation index 2 should have been:
	"""
	{
		"invocationName": "invocation-2"
	}
	"""
	And verifying my request should yield this exception message:
	"""
	for invocation at index 1, expected:<
	{
	  "url" : "/hello-world/galaxy-two",
	  "method" : "POST"
	}> but was:<
	{
	  "url" : "/hello-world/world-two",
	  "method" : "POST"
	}>
	for invocation at index 2, expected:<
	{
	  "url" : "/hello-world/galayx-three",
	  "method" : "POST"
	}> but was:<
	{
	  "url" : "/hello-world/world-three",
	  "method" : "POST"
	}>
	"""
