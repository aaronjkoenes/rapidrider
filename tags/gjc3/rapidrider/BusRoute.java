package rapidrider;

import java.util.Vector;

public class BusRoute {

	private String routeName;

	private Vector myStops;

	// Creates a bus route with a name.
	public BusRoute(String name) {
		routeName = name;
		myStops = new Vector();
	}

	public int routeLength() {
		return myStops.size();
	}

	public String listStops() {
		String temp = "";
		
		// FIXME: This fails when myStops is an empty vector.
		// Test this while fixing it.
		for (int i = 0; i < myStops.size(); i++) {
			temp += ((BusStop) myStops.elementAt(i)).getName() + "\n";
		}
		return temp;
	}

	public void addStop(BusStop stop) {
		myStops.addElement(stop);
	}

	public String getRouteName() {
		return routeName;
	}

	public BusStop getstop(int i) {
		return (BusStop) myStops.elementAt(i);
	}

}
