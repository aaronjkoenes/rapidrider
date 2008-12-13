package rapidrider;

import java.util.Vector;

// This class represents directions that will be given to the user.
// ...
public class Directions {

	private Vector stops;

	public Directions() {
		stops = new Vector();
	}

	public void addStop(BusStop stop) {
		stops.addElement(stop);
	}

	// Represent the directions as a simple string.
	// Right now this representation only includes the names of the stops.
	// But, as we learned from the user tests, it should include more info.
	public String toString() {
		String s = "";
		for (int i = 0; i < stops.size(); i++) {
			// toString on BusStops just returns their name right now.
			s += stops.elementAt(i).toString() + "\n";
		}
		return s;
	}
}
