/*
 * Authors: Aaron Koenes,Arie den Breems
 * Calvin College 08-09
 */

package rapidrider;

import java.util.Vector;

public class BusRoute {

	private String routeName;

	private Vector myStops;

	// Creates a new bus route with a name.
	public BusRoute(String name) {
		routeName = name;
		myStops = new Vector();
	}

	public int routeLength() {
		System.out.println("RouteLength size: " + myStops.size());
		return myStops.size();
	}

	public String listStops() {
		String temp = "";
		if( myStops.size() > 0 ) {
			for (int i = 0; i < myStops.size(); i++) {
				temp += ((BusStop) getstop(i)).getName() + " : " + getstop(i).printLoc() + "\n";
			}
		} else {
			temp = "No stops found";
		}
		return temp;
	}

	public void addStop(BusStop stop) {
		myStops.addElement(stop);
	}

	/* Note author:  Aaron
	 * 
	 * Originally, if the user found the nearest stop more the once during a single
	 * session, the stops would be added to the vector, without the old ones being removed.
	 * This resulted in constant vector growth.
	 *
	 * This method will remove all the previously known bus stops, and add the new ones,
	 * saving space.
	 * 
	 * It is called immediately when the "Find Nearest" button is pressed (GPSMidlet.commandAction())
	 */
	public void removeAllStops() {
		myStops.removeAllElements();
	}

	public String getRouteName() {
		return routeName;
	}

	/**
	 * @param int
	 * @return the BusStop at location (i) in the BusRoute
	 */
	public BusStop getstop(int i) {
		return (BusStop) myStops.elementAt(i);
	}

}
