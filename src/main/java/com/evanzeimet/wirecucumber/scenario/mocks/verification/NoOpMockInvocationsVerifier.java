package com.evanzeimet.wirecucumber.scenario.mocks.verification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.evanzeimet.wirecucumber.WireCucumberUtils;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;

import io.cucumber.datatable.DataTable;

public class NoOpMockInvocationsVerifier
		implements MockInvocationsVerifier {

	private static final Logger logger = LoggerFactory.getLogger(NoOpMockInvocationsVerifier.class);
	private static final WireCucumberUtils utils = new WireCucumberUtils();

	private String mockName;
	private RequestPatternBuilder requestPatternBuilder;

	public NoOpMockInvocationsVerifier(String mockName) {
		this.mockName = mockName;
		this.requestPatternBuilder = RequestPatternBuilder.newRequestPattern();
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

	protected String createSkippingVerifyMessage() {
		String skipping = String.format("%s mock verification", mockName);
		return utils.createIsDisabledSkippingMessage(skipping);
	}

	@Override
	public void setExpectedMockInvocationCount(Integer count) {

	}

	@Override
	public void verify() {
		String message = createSkippingVerifyMessage();
		logger.info(message);
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
