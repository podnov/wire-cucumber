package com.evanzeimet.wirecucumber;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.github.tomakehurst.wiremock.WireMockServer;

public class WireCucumberTest {

	private WireCucumber wireCucumber;

	@Before
	public void setUp() {
		wireCucumber = spy(new WireCucumber());
	}

	@Test
	public void close_wireMockServer_notNull() throws Exception {
		WireMockServer givenWireMockServer = mock(WireMockServer.class);
		wireCucumber.wireMockServer = givenWireMockServer;

		wireCucumber.close();

		verify(givenWireMockServer).shutdownServer();
		assertNull(wireCucumber.wireMockServer);
	}

	@Test
	public void close_wireMockServer_null() {
		WireMockServer givenWireMockServer = null;
		wireCucumber.wireMockServer = givenWireMockServer;

		Exception actualException = null;

		try {
			wireCucumber.close();
		} catch (Exception e) {
			actualException = e;
		}

		assertNull(actualException);
	}

	@Test
	public void initialize_nullOptions() {
		WireCucumberOptions givenOptions = null;

		doNothing().when(wireCucumber).startWireMockServer(any());
		doNothing().when(wireCucumber).createSteps(any());

		wireCucumber.initialize(givenOptions);

		verify(wireCucumber).startWireMockServer(notNull());
		verify(wireCucumber).createSteps(notNull());
	}
}
