package com.evanzeimet.wirecucumber.scenario.mocks.verification;

import com.github.tomakehurst.wiremock.verification.diff.WireCucumberDiffLine;
import com.google.common.base.Function;

public class VerificationConstants {

	public static Function<WireCucumberDiffLine<?>, Object> EXPECTED = new Function<WireCucumberDiffLine<?>, Object>() {

		@Override
		public Object apply(WireCucumberDiffLine<?> line) {
			Object result;

			if (line.isForNonMatch()) {
				result = line.getPrintedPatternValue();
			} else {
				result = line.getActual();
			}

			return result;
		}

	};

	public static Function<WireCucumberDiffLine<?>, Object> ACTUAL = new Function<WireCucumberDiffLine<?>, Object>() {

		@Override
		public Object apply(WireCucumberDiffLine<?> input) {
			return input.getActual();
		}

	};

}
