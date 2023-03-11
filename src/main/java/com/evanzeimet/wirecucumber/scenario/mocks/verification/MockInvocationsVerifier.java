package com.evanzeimet.wirecucumber.scenario.mocks.verification;

import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;

import io.cucumber.datatable.DataTable;

public interface MockInvocationsVerifier {

	void addBodyAbsentVerification(Integer invocationIndex);

	void addBodyEqualToVerification(Integer invocationIndex, DataTable dataTable);

	void addBodyEqualToVerification(Integer invocationIndex, String requestBody);

	void addHeaderAbsentVerification(Integer invocationIndex, String headerName);

	void addHeaderContainingVerification(Integer invocationIndex, String headerName, String headerValue);

	void addHeaderPresentVerification(Integer invocationIndex, String headerName);

	void addUrlVerification(Integer invocationIndex, String url);

	void addInvocationStateBodyAbsentVerification(String state);

	void addInvocationStateBodyEqualToVerification(String state, DataTable dataTable);

	void addInvocationStateBodyEqualToVerification(String state, String requestBody);

	void addInvocationStateHeaderAbsentVerification(String state, String headerName);

	void addInvocationStateHeaderContainingVerification(String state, String headerName, String headerValue);

	void addInvocationStateHeaderPresentVerification(String state, String headerName);

	void addInvocationStateUrlVerification(String state, String url);

	void setExpectedMockInvocationCount(Integer count);

	void verify();

	RequestPatternBuilder withHeader(String requestIdHeader, StringValuePattern equalTo);

	RequestPatternBuilder withHeaderAbsent(String name);

	RequestPatternBuilder withHeaderContaining(String name, String value);

	RequestPatternBuilder withHeaderPresent(String name);

	RequestPatternBuilder withRequestBodyAbsent();

	RequestPatternBuilder withRequestBodyEqualTo(DataTable dataTable);

	RequestPatternBuilder withRequestBodyEqualTo(String requestBody);

	RequestPatternBuilder withUrl(String url);

}
