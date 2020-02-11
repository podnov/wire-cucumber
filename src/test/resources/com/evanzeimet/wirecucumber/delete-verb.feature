@deleteVerb
Feature: Wire Cucumber Delete Verb Tests


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


Scenario: Wrong verb
	Given a wire mock named "delete-hello-world"
	And that wire mock handles the DELETE verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I GET the "hello world" resource "default" endpoint
	Then the response status code should be 404
	And I want to verify interactions with the wire mock named "delete-hello-world"
	And that mock should have been invoked 2 times
	And verifying my request should yield this exception message:
	"""
	No requests exactly matched. Most similar request was:  expected:<
	DELETE
	/hello-world
	> but was:<
	GET
	/hello-world
	>
	"""


Scenario: Number of invocations
	Given a wire mock named "delete-hello-world"
	And that wire mock handles the DELETE verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I DELETE the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "delete-hello-world"
	And that mock should have been invoked 2 times
	And verifying my request should yield this exception message:
	"""
	Expected exactly 2 requests matching the following pattern but received 1:
	{
	  "url" : "/hello-world",
	  "method" : "DELETE"
	}
	"""