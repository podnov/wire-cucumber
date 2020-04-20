package com.evanzeimet.wirecucumber;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
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
			result = objectMapper.writeValueAsString(maps);
		} catch (JsonProcessingException e) {
			throw new WireCucumberRuntimeException(e);
		}

		return result;
	}

	public List<String> getNodeFieldNames(ObjectNode node) {
		return Lists.newArrayList(node.fieldNames());
	}

	public ObjectNode readTree(String expected) {
		ObjectNode expectedNode;

		try {
			expectedNode = (ObjectNode) objectMapper.readTree(expected);
		} catch (IOException e) {
			throw new WireCucumberRuntimeException(e);
		}

		return expectedNode;
	}

	protected ObjectNode valueToTree(Object value) {
		return (ObjectNode) objectMapper.valueToTree(value);
	}

	public String writeValueAsPrettyString(Object value) {
		String result;

		try {
			result = objectMapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new WireCucumberRuntimeException(e);
		}

		return result;
	}

}
