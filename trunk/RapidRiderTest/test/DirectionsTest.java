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
		assertEquals("Calvin College & Burton\nFulton & Fuller\n", directions
				.toString());
	}

}
