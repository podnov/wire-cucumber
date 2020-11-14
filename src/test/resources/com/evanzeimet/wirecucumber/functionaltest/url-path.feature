@urlPath
Feature: Wire Cucumber URL Path Tests


Scenario: Mock based on url path equal to, no query params
	Given a wire mock named "get-hello-world" that handles the GET verb with a url path equal to "/hello-world"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I GET the "hello world" resource "default" endpoint
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should have been invoked 1 time
	And the request url should have been "/hello-world"
	And the request is verified


Scenario: Mock based on url path equal to, single query param equality
	Given a wire mock named "get-hello-world" that handles the GET verb with a url path equal to "/hello-world"
	And that wire mock expects a url query string parameter "a" equal to "1"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I GET the "hello world" resource "default" endpoint and query string "a=1"
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should have been invoked 1 time
	And the request url should have been "/hello-world?a=1"
	And the request is verified


Scenario: Mock based on url path equal to, multiple query params equality
	Given a wire mock named "get-hello-world" that handles the GET verb with a url path equal to "/hello-world"
	And that wire mock expects a url query string parameter "a" equal to "1"
	And that wire mock expects a url query string parameter "b" equal to "2"
	And that wire mock expects a url query string parameter "c" equal to "3"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I GET the "hello world" resource "default" endpoint and query string "a=1&b=2&c=3"
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should have been invoked 1 time
	And the request url should have been "/hello-world?a=1&b=2&c=3"
	And the request is verified


Scenario: Mock based on url path equal to, single query param equality failure
	Given a wire mock named "get-hello-world" that handles the GET verb with a url path equal to "/hello-world"
	And that wire mock expects a url query string parameter "a" equal to "1"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I GET the "hello world" resource "default" endpoint and query string "a=2"
	Then the response status code should be 404
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should not have been invoked
	And the request is verified


Scenario: Mock based on url path equal to, single query param matching
	Given a wire mock named "get-hello-world" that handles the GET verb with a url path equal to "/hello-world"
	And that wire mock expects a url query string parameter "a" matching "[1]"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I GET the "hello world" resource "default" endpoint and query string "a=1"
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should have been invoked 1 time
	And the request url should have been "/hello-world?a=1"
	And the request is verified


Scenario: Mock based on url path equal to, multiple query params matching
	Given a wire mock named "get-hello-world" that handles the GET verb with a url path equal to "/hello-world"
	And that wire mock expects a url query string parameter "a" matching "[1]"
	And that wire mock expects a url query string parameter "b" matching "[2]"
	And that wire mock expects a url query string parameter "c" matching "[3]"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I GET the "hello world" resource "default" endpoint and query string "a=1&b=2&c=3"
	Then the response status code should be 200
	And the response body should be "Hello World"
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should have been invoked 1 time
	And the request url should have been "/hello-world?a=1&b=2&c=3"
	And the request is verified


Scenario: Mock based on url path equal to, multiple query params matching failure
	Given a wire mock named "get-hello-world" that handles the GET verb with a url path equal to "/hello-world"
	And that wire mock expects a url query string parameter "a" matching "[1]"
	And that wire mock expects a url query string parameter "b" matching "[2]"
	And that wire mock expects a url query string parameter "c" matching "[3]"
	And that wire mock will return a response with status 200
	And that wire mock response body is "Hello World"
	And that wire mock is finalized
	When I GET the "hello world" resource "default" endpoint and query string "a=2&b=3&c=1"
	Then the response status code should be 404
	And I want to verify interactions with the wire mock named "get-hello-world"
	And that mock should not have been invoked
	And the request is verified
