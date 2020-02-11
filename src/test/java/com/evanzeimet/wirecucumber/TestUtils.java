package com.evanzeimet.wirecucumber;

public class TestUtils {

	public static String dos2unix(String value) {
		return value.replaceAll("\r\n", "\n");
	}

}
