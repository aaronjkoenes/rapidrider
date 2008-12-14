package rapidrider;

/**
 * This class represents a bus stop.
 */
public class BusStop {

	private SimpleLoc location;

	private String name;

	/**
	 * Constructs a bus stop with the given name, leaving the location null
	 * 
	 * @param _name
	 *            The name of the bus stop.
	 */
	public BusStop(String _name) {
		location = null;
		name = _name;
	}

	/**
	 * Constructs a new bus stop given a location and a name.
	 * 
	 * @param loc
	 *            The location of the bus stop.
	 * @param _name
	 *            The name of the bus stop.
	 */
	public BusStop(SimpleLoc loc, String _name) {
		location = loc;
		name = _name;
	}

	/**
	 * Gets the location of the bus stop.
	 * 
	 * @return SimpleLoc
	 */
	public SimpleLoc getLoc() {
		return location;
	}

	/**
	 * Gets the latitude of the bus stop.
	 * 
	 * @return double
	 */
	public double getLatitude() {
		return location.getLat();
	}

	/**
	 * Gets the longitude of the bus stop.
	 * 
	 * @return double
	 */
	public double getLongitude() {
		return location.getLon();
	}

	/**
	 * Gets the name of the bus stop.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Represents the bus stop as a String
	 * 
	 * @return String
	 */
	public String toString() {
		return name;
	}
}
