package de.grouponshop.conny.api.tests;

import static org.junit.Assert.*;
import org.junit.Test;

import de.grouponshop.conny.api.Client;

public class ClientTest {

	@Test
	public void testDomainGenerator() {
		
		assertEquals("https://conny.grouponshop.de/api-v1", Client.getSiteFromCountry("de"));
		assertEquals("https://conny.grouponshop.co.uk/api-v1", Client.getSiteFromCountry("gb"));
		assertEquals("https://conny.groupon-shop.it/api-v1", Client.getSiteFromCountry("it"));
	}
}
