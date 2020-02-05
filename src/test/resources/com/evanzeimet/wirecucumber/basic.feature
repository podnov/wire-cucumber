@basic
Feature: Wire Cucumber Basic Tests

# TODO negative scenarios, confirm cucumber exceptions for match failures
# TODO named invocations?
# TODO handles ANY verb in addition to handles the X verb

Scenario: any Hello World
	Given a wire mock named "any-hello-world"
	And that wire mock handles any verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I OPTIONS the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "any-hello-world"
	And that mock should have been invoked 1 time
	And the request is verified

Scenario: DELETE Hello World
	Given a wire mock named "delete-hello-world"
	And that wire mock handles the DELETE verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I DELETE the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "delete-hello-world"
	And that mock should have been invoked 1 time
	And the request is verified


Scenario: GET Hello World
	Given a wire mock named "get-hello-world"
	And that wire mock handles the GET verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I GET the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should have been invoked 1 time
	And the request is verified

Scenario: PATCH Hello World
	Given a wire mock named "patch-hello-world"
	And that wire mock handles the PATCH verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I PATCH the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "patch-hello-world"
	And that mock should have been invoked 1 time
	And the request is verified


Scenario: POST Hello World
	Given a wire mock named "post-hello-world"
	And that wire mock handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I POST the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "post-hello-world"
	And that mock should have been invoked 1 time
	And the request body should have been empty
	And the request is verified


Scenario: PUT Hello World
	Given a wire mock named "put-hello-world"
	And that wire mock handles the PUT verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I PUT the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "put-hello-world"
	And that mock should have been invoked 1 time
	And the request is verified


Scenario: Request with entity
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
	And I want to verify interactions with the wire mock named "post-hello-world"
	And that mock should have been invoked 1 time
	And the request body should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request is verified


Scenario: Mock data table response
	Given a wire mock named "get-hello-worlds"
	And that wire mock handles the GET verb with a url equal to "/hello-worlds"
	And that wire mock will return a response with status 200
	And that wire mock response body is these records:
	| name          | primaryColor | galaxy    |
	| given-world-1 | blue         | Milky Way |
	| given-world-2 | green        | Milky Way |
	| given-world-2 | grey         | Milky Way |
	And that wire mock is finalized
	When I GET the "hello worlds" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be:
	"""
	[{"name":"given-world-1","primaryColor":"blue","galaxy":"Milky Way"},{"name":"given-world-2","primaryColor":"green","galaxy":"Milky Way"},{"name":"given-world-2","primaryColor":"grey","galaxy":"Milky Way"}]
	"""
	And I want to verify interactions with the wire mock named "get-hello-worlds"
	And that mock should have been invoked 1 time
	And the request is verified


Scenario: Request with entity, verify invocation datatable
	Given a wire mock named "post-hello-worlds"
	And that wire mock handles the POST verb with a url equal to "/hello-worlds"
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
