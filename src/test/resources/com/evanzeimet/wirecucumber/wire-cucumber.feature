Feature: Wire Cucumber

# TODO negative scenarios, confirm cucumber exceptions for match failures
# TODO named invocations?

Scenario: DELETE Hello World
	Given a wire mock named "delete-hello-world"
	And that wire mock handles the DELETE verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I DELETE the hello world resource
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "delete-hello-world"
	And that mock should have been invoked 1 time
	And my request is verified


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


Scenario: PATCH Hello World
	Given a wire mock named "patch-hello-world"
	And that wire mock handles the PATCH verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I PATCH the hello world resource
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "patch-hello-world"
	And that mock should have been invoked 1 time
	And my request is verified


Scenario: POST Hello World
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


Scenario: PUT Hello World
	Given a wire mock named "put-hello-world"
	And that wire mock handles the PUT verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I PUT the hello world resource
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "put-hello-world"
	And that mock should have been invoked 1 time
	And my request is verified


Scenario: Request with entity
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


Scenario: Multiple calls on the same mock
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


Scenario: Multiple calls verifying specific invocation details
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
	# invocation-1, no body:
	When I POST the hello world resource
	When I POST the hello world resource with:
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
	And my request is verified
