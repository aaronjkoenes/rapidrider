package test;

import junit.framework.TestCase;
import rapidrider.BusStop;

public class BusStopTest extends TestCase {

	// This is a somewhat pointless test.
	public void testGetName() {
		assertEquals("Burton & Breton", new BusStop("Burton & Breton")
				.getName());
	}

}
