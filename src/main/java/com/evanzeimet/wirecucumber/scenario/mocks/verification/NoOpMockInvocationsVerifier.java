package com.evanzeimet.wirecucumber.scenario.mocks.verification;

import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;

import io.cucumber.datatable.DataTable;

public class NoOpMockInvocationsVerifier
		implements MockInvocationsVerifier {

	private RequestPatternBuilder requestPatternBuilder;

	public NoOpMockInvocationsVerifier() {
		requestPatternBuilder = RequestPatternBuilder.newRequestPattern();
	}

	@Override
	public void addBodyAbsentVerification(Integer invocationIndex) {

	}

	@Override
	public void addBodyEqualToVerification(Integer invocationIndex, DataTable dataTable) {

	}

	@Override
	public void addBodyEqualToVerification(Integer invocationIndex, String requestBody) {

	}

	@Override
	public void addHeaderAbsentVerification(Integer invocationIndex, String headerName) {

	}

	@Override
	public void addHeaderContainingVerification(Integer invocationIndex, String headerName, String headerValue) {

	}

	@Override
	public void addHeaderPresentVerification(Integer invocationIndex, String headerName) {

	}

	@Override
	public void addUrlVerification(Integer invocationIndex, String url) {

	}

	@Override
	public void addInvocationStateBodyAbsentVerification(String state) {

	}

	@Override
	public void addInvocationStateBodyEqualToVerification(String state, DataTable dataTable) {

	}

	@Override
	public void addInvocationStateBodyEqualToVerification(String state, String requestBody) {

	}

	@Override
	public void addInvocationStateHeaderAbsentVerification(String state, String headerName) {

	}

	@Override
	public void addInvocationStateHeaderContainingVerification(String state, String headerName, String headerValue) {

	}

	@Override
	public void addInvocationStateHeaderPresentVerification(String state, String headerName) {

	}

	@Override
	public void addInvocationStateUrlVerification(String state, String url) {

	}

	@Override
	public void setExpectedMockInvocationCount(Integer count) {

	}

	@Override
	public void verify() {

	}

	@Override
	public RequestPatternBuilder withHeader(String requestIdHeader, StringValuePattern equalTo) {
		return requestPatternBuilder;
	}

	@Override
	public RequestPatternBuilder withHeaderAbsent(String name) {
		return requestPatternBuilder;
	}

	@Override
	public RequestPatternBuilder withHeaderContaining(String name, String value) {
		return requestPatternBuilder;
	}

	@Override
	public RequestPatternBuilder withHeaderPresent(String name) {
		return requestPatternBuilder;
	}

	@Override
	public RequestPatternBuilder withRequestBodyAbsent() {
		return requestPatternBuilder;
	}

	@Override
	public RequestPatternBuilder withRequestBodyEqualTo(DataTable dataTable) {
		return requestPatternBuilder;
	}

	@Override
	public RequestPatternBuilder withRequestBodyEqualTo(String requestBody) {
		return requestPatternBuilder;
	}

	@Override
	public RequestPatternBuilder withUrl(String url) {
		return requestPatternBuilder;
	}

}
