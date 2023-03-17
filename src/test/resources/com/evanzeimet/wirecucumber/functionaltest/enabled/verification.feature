@verification
Feature: Wire Cucumber Verification Tests


Scenario: Skip verifying all mocks
	Given a wire mock named "get-hello-world" that handles the GET verb with a url path equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	Given a wire mock named "get-hello-galaxy" that handles the GET verb with a url path equal to "/hello-galaxy"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello Galaxy"
	And that wire mock is finalized
	Then I want to skip verifying invocations of the all wire mocks



Scenario: Skip verifying single mock
	Given a wire mock named "get-hello-world" that handles the GET verb with a url path equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	Given a wire mock named "get-hello-galaxy" that handles the GET verb with a url path equal to "/hello-galaxy"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello Galaxy"
	And that wire mock is finalized
	Then I want to skip verifying invocations of the wire mock named "get-hello-world"
	Then I want to skip verifying invocations of the wire mock named "get-hello-galaxy"
