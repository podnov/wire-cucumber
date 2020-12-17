@unfinalizedMock
Feature: Wire Cucumber Unfinalized Mock

Scenario: Unfinalized 
	Given a wire mock named "any-hello-world" that handles any verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	# oops, should have finalized here
	When I close my scenario
	Then I should receive this exception message:
	"""
	Found unfinalized mock [any-hello-world]
	"""
