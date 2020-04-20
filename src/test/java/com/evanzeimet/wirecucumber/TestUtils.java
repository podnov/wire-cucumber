package com.evanzeimet.wirecucumber;

import java.io.InputStream;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

	protected static final ObjectMapper objectMapper = new ObjectMapper();

	public static String dos2unix(String value) {
		return value.replaceAll("\r\n", "\n");
	}

	public static String readRelativeResource(Class<?> clazz,
			String name) {
		InputStream stream = clazz.getResourceAsStream(name);

		if (stream == null) {
			String message = String.format("Resource [%s] relative to [%s] not found",
					name,
					clazz.getCanonicalName());
			throw new WireCucumberRuntimeException(message);
		}

		Scanner scanner = new Scanner(stream);
		String result = scanner.useDelimiter("\\A").next();
		scanner.close();

		return dos2unix(result);
	}

	public static String stringify(Object value) {
		String result;

		try {
			result = objectMapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new WireCucumberRuntimeException(e);
		}

		return dos2unix(result);
	}

}
