package com.evanzeimet.wirecucumber.scenario.verification;

import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.verification.diff.WireCucumberDiffLine;

public class MatchInvocationResult<T> {

	private WireCucumberDiffLine<T> diffLine;
	private int invocationIndex;
	private MatchResult matchResult;

	public WireCucumberDiffLine<T> getDiffLine() {
		return diffLine;
	}

	public void setDiffLine(WireCucumberDiffLine<T> diffLine) {
		this.diffLine = diffLine;
	}

	public int getInvocationIndex() {
		return invocationIndex;
	}

	public void setInvocationIndex(int invocationIndex) {
		this.invocationIndex = invocationIndex;
	}

	public MatchResult getMatchResult() {
		return matchResult;
	}

	public void setMatchResult(MatchResult matchResult) {
		this.matchResult = matchResult;
	}


}
