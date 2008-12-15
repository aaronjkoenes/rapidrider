import junit.framework.TestCase;

public class BusRouteTest extends TestCase {

	private BusRoute busRoute;

	// The locations used are not accurate.
	public void setUp() {
		busRoute = new BusRoute("Route 28");
		busRoute.addStop(new BusStop(new SimpleLoc(3, 3), "Breton & 28th"));
		busRoute.addStop(new BusStop(new SimpleLoc(4, 4), "Clyde Park & 28th"));
		busRoute.addStop(new BusStop(new SimpleLoc(5, 5), "Byron Center & 28th"));
	}

	public void testGetRouteName() {
		assertEquals("The route name should be what it was created as.",
				"Route 28", busRoute.getRouteName());
	}

	public void testGetBusStop() {
		assertEquals("Breton & 28th", busRoute.getstop(0).getName());
		assertEquals("Clyde Park & 28th", busRoute.getstop(1).getName());
		assertEquals("Byron Center & 28th", busRoute.getstop(2).getName());
	}

	public void testRouteLength() {
		assertEquals(3, busRoute.routeLength());
		assertEquals(0, new BusRoute("Lunar Route").routeLength());
	}

}
