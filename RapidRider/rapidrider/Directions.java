package rapidrider;

import java.util.Vector;

/**
 * This class represents directions that will be given to the user.
 * 
 */
public class Directions {

	private Vector stops;

	/**
	 * Consutrcts a directions object.
	 */
	public Directions() {
		stops = new Vector();
	}

	/**
	 * Adds a stop to the list of stops in the directions.
	 * 
	 * @param stop
	 */
	public void addStop(BusStop stop) {
		stops.addElement(stop);
	}

	/**
	 * Represents the directions as a String.
	 * 
	 * @return String
	 */
	public String toString() {
		String s = "\n";
		s += "Get on at: " + stops.firstElement().toString().trim() + "\n";
		for (int i = 1; i < (stops.size() - 1); i++) {
			s += "Transfer at: " + stops.elementAt(i).toString().trim() + "\n";
		}
		s += "Get off at: " + stops.lastElement().toString().trim();
		return s;
	}
}
