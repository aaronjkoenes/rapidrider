package test;

import junit.framework.TestCase;
import rapidrider.SimpleLoc;

public class SimpleLocTest extends TestCase {

	SimpleLoc myLoc;

	public void setUp() {
		myLoc = new SimpleLoc(31.1, 90.2);
	}

	public void testDistanceTo() {
		assertEquals(100.0, myLoc.DistanceTo(new SimpleLoc(131.1, 90.2)), 0.01);
	}

}
