@responseEntity
Feature: Wire Cucumber Response Entity Tests


Scenario: Mock data table response
	Given a wire mock named "get-hello-worlds" that handles the GET verb with a url equal to "/hello-worlds"
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
	And that wire mock should have been invoked 1 time
	And the interactions with that wire mock are verified


Scenario: Body file name
	Given a wire mock named "get-hello-world" that handles the GET verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is the contents of file "hello-world.json"
	And that wire mock is finalized
	When I GET the "hello world" resource "default" endpoint
	Then the response status code should be 200
	Then the response body should be:
	"""
	{"hello":"world"}
	"""
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that wire mock should have been invoked 1 time
	And the interactions with that wire mock are verified
