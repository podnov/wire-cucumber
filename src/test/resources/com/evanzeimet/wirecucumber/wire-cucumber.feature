Feature: Wire Cucumber

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
		"meh": "feh"
	}
	"""
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "post-hello-world"
	And that mock should have been invoked 1 time
	And the request body should have been:
	"""
	{
		"meh": "feh"
	}
	"""
	And my request is verified
