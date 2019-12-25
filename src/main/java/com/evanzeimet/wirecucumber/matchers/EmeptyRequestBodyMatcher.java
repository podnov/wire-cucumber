package com.evanzeimet.wirecucumber.matchers;

import org.apache.commons.lang3.StringUtils;

import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.ValueMatcher;

public class EmeptyRequestBodyMatcher implements ValueMatcher<Request> {

	@Override
	public MatchResult match(Request value) {
		String actualBody = value.getBodyAsString();
		return MatchResult.of(StringUtils.isEmpty(actualBody));
	}

}