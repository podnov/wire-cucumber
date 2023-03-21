@requestHeaders
Feature: Wire Cucumber Headers Tests


Scenario: Header expectation equal to
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
	And that wire mock expects header "accept" equal to "application/json; charset=utf-8"
	And that wire mock expects header "content-type" equal to "application/json; charset=UTF-8"
	And that wire mock will return a response with status 200
	And that wire mock response body is:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And that wire mock is finalized
	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And I want to verify invocations of the wire mock named "post-hello-world"
	And that wire mock should have been invoked 1 time
	And the request body should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request should have had header "Content-Type" containing "application/json"
	And the invocations of that wire mock are verified


Scenario: Header expectation equal to ignore case
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
	And that wire mock expects header "accept" equal to "APPLICATION/json; charset=utf-8" ignoring case
	And that wire mock expects header "content-type" equal to "APPLICATION/json; charset=UTF-8" ignoring case
	And that wire mock will return a response with status 200
	And that wire mock response body is:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And that wire mock is finalized
	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And I want to verify invocations of the wire mock named "post-hello-world"
	And that wire mock should have been invoked 1 time
	And the request body should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request should have had header "Content-Type" containing "application/json"
	And the invocations of that wire mock are verified


Scenario: Header expectation macthing
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
	And that wire mock expects header "accept" matching ".*json.*"
	And that wire mock expects header "content-type" matching ".*json.*"
	And that wire mock will return a response with status 200
	And that wire mock response body is:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And that wire mock is finalized
	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And I want to verify invocations of the wire mock named "post-hello-world"
	And that wire mock should have been invoked 1 time
	And the request body should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request should have had header "Content-Type" containing "application/json"
	And the invocations of that wire mock are verified


Scenario: Header assertion containing, bad match
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And that wire mock is finalized
	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And I want to verify invocations of the wire mock named "post-hello-world"
	And that wire mock should have been invoked 1 time
	And the request body should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request should have had header "Content-Type" containing "application/json-awesome"
	And verifying my request should yield this exception message:
	"""
	No requests exactly matched. Most similar request was:  expected:<
	POST
	/hello-world

	Content-Type [contains] : application/json-awesome

	{
		"invocationName": "invocation-0"
	}> but was:<
	POST
	/hello-world

	Content-Type: application/json; charset=UTF-8

	{
		"invocationName": "invocation-0"
	}>
	"""


Scenario: Header assertion containing, multiple requests
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And that wire mock is finalized
	#invocation 0
	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	#invocation 1
	When I POST the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And I want to verify invocations of the wire mock named "post-hello-world"
	And that wire mock should have been invoked 2 times
	And the request at invocation index 0 should have had header "Content-Type" containing "application/json"
	And the request at invocation index 1 should have had header "Content-Type" containing "application/x-www-form-urlencoded"
	And the invocations of that wire mock are verified


Scenario: Header assertion containing, multiple requests, bad match
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And that wire mock is finalized
	#invocation 0
	When I POST the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	#invocation 1
	When I POST the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And I want to verify invocations of the wire mock named "post-hello-world"
	And that wire mock should have been invoked 2 times
	And the request at invocation index 0 should have had header "Content-Type" containing "application/json"
	And the request at invocation index 1 should have had header "Content-Type" containing "application/x-www-form-urlencoded"
	And verifying my request should yield this exception message:
	"""
	for invocation at index 0, expected:<
	{
	  "url" : "/hello-world",
	  "method" : "POST",
	  "headers" : {
	    "Content-Type" : {
	      "contains" : "application/json"
	    }
	  }
	}> but was:<
	{
	  "url" : "/hello-world",
	  "method" : "POST",
	  "headers" : {
	    "Content-Type" : "application/x-www-form-urlencoded; charset=ISO-8859-1",
	    "Accept" : "*/*",
	    "Content-Length" : "0",
	    "Connection" : "keep-alive",
	    "Accept-Encoding" : "gzip,deflate"
	  }
	}>
	"""


Scenario: Header assertion present
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And that wire mock is finalized
	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And I want to verify invocations of the wire mock named "post-hello-world"
	And that wire mock should have been invoked 1 time
	And the request body should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request should have had header "Content-Type" present
	And the invocations of that wire mock are verified


Scenario: Header assertion present, bad match
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And that wire mock is finalized
	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And I want to verify invocations of the wire mock named "post-hello-world"
	And that wire mock should have been invoked 1 time
	And the request body should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request should have had header "Content-Type-O" present
	And verifying my request should yield this exception message:
	"""
	No requests exactly matched. Most similar request was:  expected:<
	POST
	/hello-world
	
	Content-Type-O [matches] : .*
	
	{
		"invocationName": "invocation-0"
	}> but was:<
	POST
	/hello-world
	
	
	
	{
		"invocationName": "invocation-0"
	}>
	"""


Scenario: Header assertion present, multiple requests
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And that wire mock is finalized
	#invocation 0
	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	#invocation 1
	When I POST the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And I want to verify invocations of the wire mock named "post-hello-world"
	And that wire mock should have been invoked 2 times
	And the request at invocation index 0 should have had header "Content-Type" present
	And the request at invocation index 1 should have had header "Content-Type" present
	And the invocations of that wire mock are verified


Scenario: Header assertion present, multiple requests, bad match
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And that wire mock is finalized
	#invocation 0
	When I POST the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	#invocation 1
	When I POST the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And I want to verify invocations of the wire mock named "post-hello-world"
	And that wire mock should have been invoked 2 times
	And the request at invocation index 0 should have had header "Content-Type" present
	And the request at invocation index 1 should have had header "Content-Type-O" present
	And verifying my request should yield this exception message:
	"""
	for invocation at index 1, expected:<
	{
	  "url" : "/hello-world",
	  "method" : "POST",
	  "headers" : {
	    "Content-Type-O" : {
	      "matches" : ".*"
	    }
	  }
	}> but was:<
	{
	  "url" : "/hello-world",
	  "method" : "POST",
	  "headers" : {
	    "Content-Type" : "application/x-www-form-urlencoded; charset=ISO-8859-1",
	    "Accept" : "*/*",
	    "Content-Length" : "0",
	    "Connection" : "keep-alive",
	    "Accept-Encoding" : "gzip,deflate"
	  }
	}>
	"""


Scenario: Header assertion absent
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And that wire mock is finalized
	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And I want to verify invocations of the wire mock named "post-hello-world"
	And that wire mock should have been invoked 1 time
	And the request body should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request should have had header "Content-Type-O" absent
	And the invocations of that wire mock are verified


Scenario: Header assertion absent, bad match
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And that wire mock is finalized
	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And I want to verify invocations of the wire mock named "post-hello-world"
	And that wire mock should have been invoked 1 time
	And the request body should have been:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	And the request should have had header "Content-Type" absent
	And verifying my request should yield this exception message:
	"""
	No requests exactly matched. Most similar request was:  expected:<
	POST
	/hello-world
	
	Content-Type [absent] : (absent)
	
	{
		"invocationName": "invocation-0"
	}> but was:<
	POST
	/hello-world
	
	Content-Type: application/json; charset=UTF-8
	
	{
		"invocationName": "invocation-0"
	}>
	"""


Scenario: Header assertion absent, multiple requests
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And that wire mock is finalized
	#invocation 0
	When I POST the "hello world" resource "default" endpoint with:
	"""
	{
		"invocationName": "invocation-0"
	}
	"""
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	#invocation 1
	When I POST the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And I want to verify invocations of the wire mock named "post-hello-world"
	And that wire mock should have been invoked 2 times
	And the request at invocation index 0 should have had header "Content-Type-O" absent
	And the request at invocation index 1 should have had header "Content-Type-O" absent
	And the invocations of that wire mock are verified


Scenario: Header assertion absent, multiple requests, bad match
	Given a wire mock named "post-hello-world" that handles the POST verb with a url equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And that wire mock is finalized
	#invocation 0
	When I POST the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	#invocation 1
	When I POST the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be:
	"""
	{
		"given-response-key": "given-response-value"
	}
	"""
	And I want to verify invocations of the wire mock named "post-hello-world"
	And that wire mock should have been invoked 2 times
	And the request at invocation index 0 should have had header "Content-Type" absent
	And the request at invocation index 1 should have had header "Content-Type" absent
	And verifying my request should yield this exception message:
	"""
	for invocation at index 0, expected:<
	{
	  "url" : "/hello-world",
	  "method" : "POST",
	  "headers" : {
	    "Content-Type" : {
	      "absent" : true
	    }
	  }
	}> but was:<
	{
	  "url" : "/hello-world",
	  "method" : "POST",
	  "headers" : {
	    "Content-Type" : "application/x-www-form-urlencoded; charset=ISO-8859-1",
	    "Accept" : "*/*",
	    "Content-Length" : "0",
	    "Connection" : "keep-alive",
	    "Accept-Encoding" : "gzip,deflate"
	  }
	}>
	for invocation at index 1, expected:<
	{
	  "url" : "/hello-world",
	  "method" : "POST",
	  "headers" : {
	    "Content-Type" : {
	      "absent" : true
	    }
	  }
	}> but was:<
	{
	  "url" : "/hello-world",
	  "method" : "POST",
	  "headers" : {
	    "Content-Type" : "application/x-www-form-urlencoded; charset=ISO-8859-1",
	    "Accept" : "*/*",
	    "Content-Length" : "0",
	    "Connection" : "keep-alive",
	    "Accept-Encoding" : "gzip,deflate"
	  }
	}>
	"""
