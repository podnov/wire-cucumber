package com.evanzeimet.wirecucumber.scenario;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.Before;
import org.junit.Test;

import com.evanzeimet.wirecucumber.WireCucumberOptions;
import com.evanzeimet.wirecucumber.scenario.mocks.builder.MocksBuilder;
import com.evanzeimet.wirecucumber.scenario.mocks.verification.MocksInvocationsVerifier;

import io.cucumber.java8.Scenario;
import io.cucumber.java8.Status;

public class ScenarioBuilderTest {

	private ScenarioBuilder builder;

	private ScenarioContext givenContext;
	private MocksBuilder givenMocksBuilder;
	private MocksInvocationsVerifier givenMocksVerifier;

	@Before
	public void setUp() {
		builder = new ScenarioBuilder();

		givenContext = mock(ScenarioContext.class);
		builder.context = givenContext;

		givenMocksBuilder = mock(MocksBuilder.class);
		builder.mocksBuilder = givenMocksBuilder;

		givenMocksVerifier = mock(MocksInvocationsVerifier.class);
		builder.mocksVerifier = givenMocksVerifier;
	}

	@Test
	public void closeScenario_passed_false() {
		WireCucumberOptions givenOptions = new WireCucumberOptions();

		Status givenScenarioStatus = Status.FAILED;

		Scenario givenScenario = mock(Scenario.class);
		doReturn(givenScenarioStatus).when(givenScenario).getStatus();

		doReturn(givenScenario).when(givenContext).getCurrentScenario();


		builder.closeScenario(givenOptions);

		verifyNoInteractions(givenMocksBuilder);
		verifyNoInteractions(givenMocksVerifier);
	}

	@Test
	public void closeScenario_passed_true_isDisabled_false_requireMockFinalization_false() {
		boolean givenIsDisabled = false;
		boolean givenRequireMockFinalization = false;

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setIsDisabled(givenIsDisabled);
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);

		Status givenScenarioStatus = Status.PASSED;

		Scenario givenScenario = mock(Scenario.class);
		doReturn(givenScenarioStatus).when(givenScenario).getStatus();

		doReturn(givenScenario).when(givenContext).getCurrentScenario();

		builder.closeScenario(givenOptions);

		verify(givenMocksBuilder, never()).verifyMocksFinalized();
	}

	@Test
	public void closeScenario_passed_true_isDisabled_false_requireMockFinalization_true() {
		boolean givenIsDisabled = false;
		boolean givenRequireMockFinalization = true;

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setIsDisabled(givenIsDisabled);
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);

		Status givenScenarioStatus = Status.PASSED;

		Scenario givenScenario = mock(Scenario.class);
		doReturn(givenScenarioStatus).when(givenScenario).getStatus();

		doReturn(givenScenario).when(givenContext).getCurrentScenario();

		builder.closeScenario(givenOptions);

		verify(givenMocksBuilder).verifyMocksFinalized();
	}

	@Test
	public void closeScenario_passed_true_isDisabled_true_requireMockFinalization_true() {
		boolean givenIsDisabled = true;
		boolean givenRequireMockFinalization = true;

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setIsDisabled(givenIsDisabled);
		givenOptions.setRequireMockFinalization(givenRequireMockFinalization);

		Status givenScenarioStatus = Status.PASSED;

		Scenario givenScenario = mock(Scenario.class);
		doReturn(givenScenarioStatus).when(givenScenario).getStatus();

		doReturn(givenScenario).when(givenContext).getCurrentScenario();

		builder.closeScenario(givenOptions);

		verify(givenMocksBuilder, never()).verifyMocksFinalized();
	}

	@Test
	public void closeScenario_passed_true_isDisabled_false_requireMockInvocationsVerification_false() {
		boolean givenIsDisabled = false;
		boolean givenRequireMockInvocationsVerification = false;

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setIsDisabled(givenIsDisabled);
		givenOptions.setRequireMockInvocationsVerification(givenRequireMockInvocationsVerification);

		Status givenScenarioStatus = Status.PASSED;

		Scenario givenScenario = mock(Scenario.class);
		doReturn(givenScenarioStatus).when(givenScenario).getStatus();

		doReturn(givenScenario).when(givenContext).getCurrentScenario();

		builder.closeScenario(givenOptions);

		verify(givenMocksVerifier, never()).verifyMockInvocationsVerified();
	}

	@Test
	public void closeScenario_passed_true_isDisabled_false_requireMockInvocationsVerification_true() {
		boolean givenIsDisabled = false;
		boolean givenRequireMockInvocationsVerification = true;

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setIsDisabled(givenIsDisabled);
		givenOptions.setRequireMockInvocationsVerification(givenRequireMockInvocationsVerification);

		Status givenScenarioStatus = Status.PASSED;

		Scenario givenScenario = mock(Scenario.class);
		doReturn(givenScenarioStatus).when(givenScenario).getStatus();

		doReturn(givenScenario).when(givenContext).getCurrentScenario();

		builder.closeScenario(givenOptions);

		verify(givenMocksVerifier).verifyMockInvocationsVerified();
	}

	@Test
	public void closeScenario_passed_true_isDisabled_true_requireMockInvocationsVerification_true() {
		boolean givenIsDisabled = true;
		boolean givenRequireMockInvocationsVerification = true;

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setIsDisabled(givenIsDisabled);
		givenOptions.setRequireMockInvocationsVerification(givenRequireMockInvocationsVerification);

		Status givenScenarioStatus = Status.PASSED;

		Scenario givenScenario = mock(Scenario.class);
		doReturn(givenScenarioStatus).when(givenScenario).getStatus();

		doReturn(givenScenario).when(givenContext).getCurrentScenario();

		builder.closeScenario(givenOptions);

		verify(givenMocksVerifier, never()).verifyMockInvocationsVerified();
	}

}
