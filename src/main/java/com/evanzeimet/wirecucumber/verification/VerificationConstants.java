package com.evanzeimet.wirecucumber.verification;

import com.github.tomakehurst.wiremock.verification.diff.WireCucumberDiffLine;
import com.google.common.base.Function;

public class VerificationConstants {

	public static Function<WireCucumberDiffLine<?>, Object> EXPECTED = new Function<WireCucumberDiffLine<?>, Object>() {

		@Override
		public Object apply(WireCucumberDiffLine<?> line) {
			return line.isForNonMatch() ? line.getPrintedPatternValue() : line.getActual();
		}

	};

	public static Function<WireCucumberDiffLine<?>, Object> ACTUAL = new Function<WireCucumberDiffLine<?>, Object>() {

		@Override
		public Object apply(WireCucumberDiffLine<?> input) {
			return input.getActual();
		}

	};

}
