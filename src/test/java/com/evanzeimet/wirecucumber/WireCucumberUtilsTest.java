package com.evanzeimet.wirecucumber;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.cucumber.datatable.DataTable;

public class WireCucumberUtilsTest {

	private WireCucumberUtils utils;

	@Before
	public void setUp() {
		utils = spy(new WireCucumberUtils());
	}

	@Test
	public void convertDataTableToJson_exception() throws JsonProcessingException {
		DataTable givenDataTable = DataTable.create(Collections.emptyList());

		ObjectMapper givenObjectMapper = mock(ObjectMapper.class);
		doThrow(JsonProcessingException.class)
				.when(givenObjectMapper)
				.writeValueAsString(any());

		doReturn(givenObjectMapper)
				.when(utils)
				.getObjectMapper();

		WireCucumberRuntimeException actual = null;

		try {
			utils.convertDataTableToJson(givenDataTable);
		} catch (WireCucumberRuntimeException e) {
			actual = e;
		}

		assertNotNull(actual);
	}

	@Test
	public void getNodeFieldNames() {
		Pojo givenValue = new Pojo();
		ObjectNode givenNode = utils.valueToTree(givenValue);

		List<String> actual = utils.getNodeFieldNames(givenNode);

		assertThat(actual, containsInAnyOrder("field1", "field2", "field3"));
	}

	@Test
	public void readTree_exception() throws IOException {
		String givenValue = "given-value";

		ObjectMapper givenObjectMapper = mock(ObjectMapper.class);
		doThrow(IOException.class)
				.when(givenObjectMapper)
				.readTree(any(String.class));

		doReturn(givenObjectMapper)
				.when(utils)
				.getObjectMapper();

		WireCucumberRuntimeException actual = null;

		try {
			utils.readTree(givenValue);
		} catch (WireCucumberRuntimeException e) {
			actual = e;
		}

		assertNotNull(actual);
	}

	@Test
	public void writeValueAsPrettyString_exception() throws JsonProcessingException {
		String givenValue = "given-value";

		ObjectWriter givenObjectWriter = mock(ObjectWriter.class);
		doThrow(JsonProcessingException.class)
				.when(givenObjectWriter)
				.writeValueAsString(any());

		ObjectMapper givenObjectMapper = mock(ObjectMapper.class);
		doReturn(givenObjectWriter)
				.when(givenObjectMapper)
				.writerWithDefaultPrettyPrinter();

		doReturn(givenObjectMapper)
				.when(utils)
				.getObjectMapper();

		WireCucumberRuntimeException actual = null;

		try {
			utils.writeValueAsPrettyString(givenValue);
		} catch (WireCucumberRuntimeException e) {
			actual = e;
		}

		assertNotNull(actual);
	}

	@SuppressWarnings("unused")
	private static class Pojo {

		private String field1;
		private Integer field2;
		private List<String> field3;

		public String getField1() {
			return field1;
		}

		public void setField1(String field1) {
			this.field1 = field1;
		}

		public Integer getField2() {
			return field2;
		}

		public void setField2(Integer field2) {
			this.field2 = field2;
		}

		public List<String> getField3() {
			return field3;
		}

		public void setField3(List<String> field3) {
			this.field3 = field3;
		}
	}
}
