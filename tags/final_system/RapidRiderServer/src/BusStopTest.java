import junit.framework.TestCase;

public class BusStopTest extends TestCase {

	// This is a trivial test.
	// The locations are not accurate.
	public void testGetName() {
		assertEquals("Burton & Breton", new BusStop(new SimpleLoc(0, 0),
				"Burton & Breton").getName());
		assertEquals("Fulton & Fuller", new BusStop(new SimpleLoc(1, 1),
				"Fulton & Fuller").getName());
	}

}
