





/**
 * The BusStop class models Rapid bus stops. Each stop has a name and a
 * location.
 */
public class BusStop {

	private SimpleLoc myLoc;
	private String myName;

	/**
	 * Constructs a bus stop with a location and a name.
	 * 
	 * @param loc
	 *            The location of the bus stop.
	 * @param name
	 *            The name of the bus stop.
	 */
	public BusStop(SimpleLoc loc, String name) {
		myLoc = loc;
		myName = name;
	}

	/**
	 * Gets the location of the bus stop.
	 * 
	 * @return SimpleLoc
	 */
	public SimpleLoc getLoc() {
		return myLoc;
	}

	/**
	 * Gets the name of the bus stop.
	 * 
	 * @return String
	 */
	public String getName() {
		return myName;
	}

	/**
	 * Gets the latitude of the bus stop.
	 * 
	 * @return double
	 */
	public double getLatitude() {
		return myLoc.getLat();
	}

	/**
	 * Gets the longitude of the bus stop.
	 * 
	 * @return double
	 */
	public double getLongitude() {
		return myLoc.getLon();
	}

	/**
	 * Returns a String representation of the location of the bus stop.
	 * 
	 * @return String
	 */
	public String printLoc() {
		return myLoc.printLoc();
	}

	/**
	 * Returns a String representation of the bus stop that includes its name
	 * and its location.
	 * 
	 * @return String
	 */
	public String toString() {
		return myName + " " + myLoc.toString();
	}
}
