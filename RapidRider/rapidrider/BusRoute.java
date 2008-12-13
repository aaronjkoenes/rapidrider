/*
 * Authors: Aaron Koenes, Arie den Breems
 * Calvin College 08-09
 */

package rapidrider;

import java.util.Vector;

// This class is not used right now.  But I will leave it around anyhow.
public class BusRoute {

	private String routeName;
	private Vector myStops;

	// Creates a new bus route with a name.
	public BusRoute(String name) {
		routeName = name;
		myStops = new Vector();
	}
}
