


import java.util.Vector;




/**
 * The BusRoute class models Rapid bus routes. Each bus route has a name and a
 * list of stops.
 */
public class BusRoute {

	private String routeName;
	private Vector<BusStop> myStops;

	/**
	 * Creates a new bus route with a name.
	 * 
	 * @param name
	 *            The name of the bus route.
	 */
	public BusRoute(String name) {
		routeName = name;
		myStops = new Vector<BusStop>();
	}

	/**
	 * Returns the length of the route.
	 * 
	 * @return int
	 */
	public int routeLength() {
		return myStops.size();
	}

	/**
	 * Returns a String representation of the stops on this route.
	 * 
	 * @return String
	 */
	public String listStops() {
		String temp = "";
		if (myStops.size() > 0) {
			for (int i = 0; i < myStops.size(); i++) {
				temp += ((BusStop) getstop(i)).getName() + " : "
						+ getstop(i).printLoc() + "\n";
			}
		} else {
			temp = "No stops found";
		}
		return temp;
	}

	/**
	 * Adds a bus stop to this route.
	 * 
	 * @param stop
	 *            The bus stop to add.
	 */
	public void addStop(BusStop stop) {
		myStops.addElement(stop);
	}

	/**
	 * Originally, if the user found the nearest stop more the once during a
	 * single session, the stops would be added to the vector, without the old
	 * ones being removed. This resulted in constant vector growth.
	 * 
	 * This method will remove all the previously known bus stops, and add the
	 * new ones, saving space.
	 * 
	 * @author Aaron
	 */
	public void removeAllStops() {
		myStops.removeAllElements();
	}

	/**
	 * Gets the route name.
	 * 
	 * @return String
	 */
	public String getRouteName() {
		return routeName;
	}

	/**
	 * Gets the bus stop stored at the given index.
	 * 
	 * @param index
	 *            The index of the BusStop on the route.
	 * @return BusStop
	 */
	public BusStop getstop(int index) {
		return (BusStop) myStops.elementAt(index);
	}
}
