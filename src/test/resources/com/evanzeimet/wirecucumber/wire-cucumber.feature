Feature: Wire Cucumber

Scenario: GET Hello World
	Given a stub for a GET with url equal to "/hello-world" exists
	And that stub will return a response with status 200
	And that stub response body is "Hello World"
	And that stub is finalized
	When I GET the hello world resource
	Then the response status code should be 200
	And the response body should be "Hello World"
	And a GET should have been sent with url equal to "/hello-world"
	And my request is verified

Scenario: Empty POST Hello World
	Given a stub for a POST with url equal to "/hello-world" exists
	And that stub will return a response with status 200
	And that stub response body is "Hello World"
	And that stub is finalized
	When I POST the hello world resource
	Then the response status code should be 200
	And the response body should be "Hello World"
	And a POST should have been sent with url equal to "/hello-world"
	And the request body should have been empty
	And my request is verified

Scenario: Entity POST Hello World
	Given a stub for a POST with url equal to "/hello-world" exists
	And that stub will return a response with status 200
	And that stub response body is "Hello World"
	And that stub is finalized
	When I POST the hello world resource with:
	"""
	{
		"meh": "feh"
	}
	"""
	Then the response status code should be 200
	And the response body should be "Hello World"
	And a POST should have been sent with url equal to "/hello-world"
	And the request body should have been:
	"""
	{
		"meh": "feh"
	}
	"""
	And my request is verified
