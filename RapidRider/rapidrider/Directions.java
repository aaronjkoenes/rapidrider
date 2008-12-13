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
		s += "Get on at: " + stops.firstElement().toString().trim() + "\n";
		for (int i = 1; i < (stops.size() - 1); i++) {
			s += "Transfer at: " + stops.elementAt(i).toString().trim() + "\n";
		}
		s += "Get off at: " + stops.lastElement().toString().trim();
		return s;
	}
	
}
