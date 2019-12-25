Feature: Wire Cucumber

# TODO negative scenarios, confirm cucumber exceptions for match failures
# TODO named invocations?

Scenario: GET Hello World
	Given a wire mock named "get-hello-world"
	And that wire mock handles the GET verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I GET the hello world resource
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should have been invoked 1 time
	And my request is verified


Scenario: Empty POST Hello World
	Given a wire mock named "post-hello-world"
	And that wire mock handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I POST the hello world resource
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "post-hello-world"
	And that mock should have been invoked 1 time
	And the request body should have been empty
	And my request is verified


Scenario: Entity POST Hello World
	Given a wire mock named "post-hello-world"
	And that wire mock handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I POST the hello world resource with:
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
	And my request is verified


Scenario: Multiple GET Hello World
	Given a wire mock named "get-hello-world"
	And that wire mock handles the GET verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I GET the hello world resource
	Then the response status code should be 200
	And the response body should be "Hello World"
	When I GET the hello world resource
	Then the response status code should be 200
	And the response body should be "Hello World"
	When I GET the hello world resource
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should have been invoked 3 times
	And my request is verified


Scenario: Multiple GETs
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
	When I GET the hello world resource
	Then the response status code should be 200
	And the response body should be "Hello World"
	When I GET the hello galaxy resource
	Then the response status code should be 200
	And the response body should be "Hello Galaxy"
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should have been invoked 1 time
	And my request is verified
	And I want to verify interactions with the wire mock named "get-hello-galaxy"
	And that mock should have been invoked 1 time
	And my request is verified


Scenario: Multiple POST Hello World
	Given a wire mock named "post-hello-world"
	And that wire mock handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I POST the hello world resource with:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be "Hello World"
	When I POST the hello world resource with:
	"""
	{
		"invocationName": "invocation-1"
	}
	"""
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "post-hello-world"
	And that mock should have been invoked 2 times
	And the request body of invocation 0 should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request body of invocation 1 should have been:
	"""
	{
		"invocationName": "invocation-1"
	}
	"""
	And my request is verified
