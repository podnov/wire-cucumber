@requestEntity
Feature: Wire Cucumber Request Entity Tests


Scenario: Request with entity
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
	And I want to verify interactions with the wire mock named "post-hello-world"
	And that mock should have been invoked 1 time
	And the request body should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request is verified


Scenario: Request with entity, verify invocation datatable
	Given a wire mock named "post-hello-worlds" that handles the POST verb with a url equal to "/hello-worlds"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello Worlds"
	And that wire mock is finalized
	When I POST the "hello worlds" resource "default" endpoint with:
	"""
	[{"name":"given-world-1","primaryColor":"blue","galaxy":"Milky Way"},{"name":"given-world-2","primaryColor":"green","galaxy":"Milky Way"},{"name":"given-world-2","primaryColor":"grey","galaxy":"Milky Way"}]
	"""
	Then the response status code should be 200
	And the response body should be "Hello Worlds"
	And I want to verify interactions with the wire mock named "post-hello-worlds"
	And that mock should have been invoked 1 time
	And the request body should have been these records:
	| name          | primaryColor | galaxy    |
	| given-world-1 | blue         | Milky Way |
	| given-world-2 | green        | Milky Way |
	| given-world-2 | grey         | Milky Way |
	And the request is verified


Scenario: Wrong entity
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
	And I want to verify interactions with the wire mock named "post-hello-world"
	And that mock should have been invoked 1 time
	And the request body should have been:
	"""
	{
		"invocationName": "invocation-42"
	}
	"""
	And verifying my request should yield this exception message:
	"""
	No requests exactly matched. Most similar request was:  expected:<
	POST
	/hello-world

	{
		"invocationName": "invocation-42"
	}> but was:<
	POST
	/hello-world

	{
		"invocationName": "invocation-0"
	}>
	"""


Scenario: Wrong entity datatable
	Given a wire mock named "post-hello-worlds" that handles the POST verb with a url equal to "/hello-worlds"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello Worlds"
	And that wire mock is finalized
	When I POST the "hello worlds" resource "default" endpoint with:
	"""
	[{"name":"given-world-1","primaryColor":"blue","galaxy":"Milky Way"},{"name":"given-world-2","primaryColor":"green","galaxy":"Milky Way"},{"name":"given-world-2","primaryColor":"grey","galaxy":"Milky Way"}]
	"""
	Then the response status code should be 200
	And the response body should be "Hello Worlds"
	And I want to verify interactions with the wire mock named "post-hello-worlds"
	And that mock should have been invoked 1 time
	And the request body should have been these records:
	| name          | primaryColor | galaxy    |
	| given-world-1 | blue         | Milky Way |
	| given-world-2 | fuchsia      | Milky Way |
	| given-world-2 | grey         | Milky Way |
	And verifying my request should yield this exception message:
	"""
	No requests exactly matched. Most similar request was:  expected:<
	POST
	/hello-worlds

	[{"name":"given-world-1","primaryColor":"blue","galaxy":"Milky Way"},{"name":"given-world-2","primaryColor":"fuchsia","galaxy":"Milky Way"},{"name":"given-world-2","primaryColor":"grey","galaxy":"Milky Way"}]> but was:<
	POST
	/hello-worlds

	[{"name":"given-world-1","primaryColor":"blue","galaxy":"Milky Way"},{"name":"given-world-2","primaryColor":"green","galaxy":"Milky Way"},{"name":"given-world-2","primaryColor":"grey","galaxy":"Milky Way"}]>
	"""
