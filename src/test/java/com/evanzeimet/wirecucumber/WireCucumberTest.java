package com.evanzeimet.wirecucumber;

import static org.junit.Assert.assertNull;
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
	public void startWireMockServer_isDisabled_true() {
		boolean givenIsDisabled = true;

		WireCucumberOptions givenOptions = new WireCucumberOptions();
		givenOptions.setIsDisabled(givenIsDisabled);

		wireCucumber.options = givenOptions;
		wireCucumber.startWireMockServer();

		assertNull(wireCucumber.wireMockServer);
	}

}
