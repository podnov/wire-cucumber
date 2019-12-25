Feature: Wire Cucumber

Scenario: Hello World
	Given a stub for a GET with url equal to "/hello-world" exists
	And that stub will return a response with status 200
	And that stub response body is "Hello World"
	And that stub is configured
	When I call the hello world resource
	Then the response status code should be 200
	And the response body should be "Hello World"