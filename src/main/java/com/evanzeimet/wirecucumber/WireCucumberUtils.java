package com.evanzeimet.wirecucumber;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

import io.cucumber.datatable.DataTable;

public class WireCucumberUtils {

	protected static final ObjectMapper objectMapper = new ObjectMapper();

	public String convertDataTableToJson(DataTable dataTable) {
		String result;
		List<Map<String, String>> maps = dataTable.asMaps();

		try {
			result = getObjectMapper().writeValueAsString(maps);
		} catch (JsonProcessingException e) {
			throw new WireCucumberRuntimeException(e);
		}

		return result;
	}

	public String createIsDisabledSkippingMessage(String skipping) {
		return String.format("Skipping %s due to wire-cucumber being disabled", skipping);
	}

	public List<String> getNodeFieldNames(ObjectNode node) {
		Iterator<String> fieldNames = node.fieldNames();
		return Lists.newArrayList(fieldNames);
	}

	protected ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public ObjectNode readObject(String value) {
		return (ObjectNode) readTree(value);
	}

	public JsonNode readTree(String value) {
		JsonNode expectedNode;

		try {
			expectedNode = getObjectMapper().readTree(value);
		} catch (IOException e) {
			throw new WireCucumberRuntimeException(e);
		}

		return expectedNode;
	}

	public String writeValueAsPrettyString(Object value) {
		String result;

		try {
			result = getObjectMapper()
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new WireCucumberRuntimeException(e);
		}

		return result;
	}

	public ObjectNode valueToObject(Object value) {
		return (ObjectNode) valueToTree(value);
	}

	public JsonNode valueToTree(Object value) {
		return getObjectMapper().valueToTree(value);
	}

}
