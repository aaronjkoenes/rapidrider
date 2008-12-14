package test;

import junit.framework.TestCase;
import rapidrider.BusStop;
import rapidrider.Directions;
import rapidrider.SimpleLoc;

public class DirectionsTest extends TestCase {

	public void testToString() {
		Directions directions = new Directions();
		directions.addStop(new BusStop(new SimpleLoc(42.931, -85.587),
				"Calvin College & Burton"));
		directions.addStop(new BusStop(new SimpleLoc(42.962, -85.632),
				"Fulton & Fuller"));
		assertEquals("\nGet on at: Calvin College & Burton\n"
				+ "Get off at: Fulton & Fuller", directions.toString());
	}

}
