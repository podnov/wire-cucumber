package com.evanzeimet.wirecucumber;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class WireCucumberUtilsTest {

	private WireCucumberUtils utils;

	@Before
	public void setUp() {
		utils = new WireCucumberUtils();
	}

	@Test
	public void getNodeFieldNames() {
		Pojo givenValue = new Pojo();
		ObjectNode givenNode = utils.valueToTree(givenValue);

		List<String> actual = utils.getNodeFieldNames(givenNode);

		assertThat(actual, containsInAnyOrder("field1", "field2", "field3"));
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
