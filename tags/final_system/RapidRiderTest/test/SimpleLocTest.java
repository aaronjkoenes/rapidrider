package test;

import junit.framework.TestCase;
import rapidrider.SimpleLoc;

public class SimpleLocTest extends TestCase {

	SimpleLoc myLoc;

	public void setUp() {
		myLoc = new SimpleLoc(31.1, 90.2);
	}

	public void testDistanceTo() {
		assertEquals(100.0, myLoc.distanceTo(new SimpleLoc(131.1, 90.2)), 0.01);
		assertEquals(70.0, myLoc.distanceTo(new SimpleLoc(31.1, 20.2)), 0.01);
		assertEquals(110.4, myLoc.distanceTo(new SimpleLoc(31.1, -20.2)), 0.01);
		assertEquals(5.0, new SimpleLoc(0, 3).distanceTo(new SimpleLoc(4, 0)),
				0.01);
	}
}
