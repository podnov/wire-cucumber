package com.github.tomakehurst.wiremock.verification.diff;

import com.github.tomakehurst.wiremock.matching.NamedValueMatcher;

public class WireCucumberDiffLine<V>
		extends DiffLine<V> {

	public WireCucumberDiffLine(String requestAttribute,
		NamedValueMatcher<V> pattern,
		V value,
		String printedPatternValue) {
		super(requestAttribute, pattern, value, printedPatternValue);
	}

}
