/**
 * The SimpleLoc class represents a location with a latitude and longitude.
 */
public class SimpleLoc {

	private double myLat, myLon;

	/**
	 * Constructs a location from the given latitude and longitude.
	 * 
	 * @param lat
	 *            The latitude.
	 * @param lon
	 *            The longitude.
	 */
	public SimpleLoc(double lat, double lon) {
		myLat = lat;
		myLon = lon;
	}

	/**
	 * Gets the latitude of the location.
	 * 
	 * @return double
	 */
	public double getLat() {
		return myLat;
	}

	/**
	 * Gets the longitude of the location.
	 * 
	 * @return double
	 */
	public double getLon() {
		return myLon;
	}

	/**
	 * Returns a String representation of the location.
	 * 
	 * @return String
	 */
	public String printLoc() {
		return myLat + ", " + myLon;
	}

	/**
	 * Calculates the distance from this location to the target location.
	 * 
	 * @param target
	 *            The location to find the distance to from this one.
	 * 
	 * @return double
	 */
	public double distanceTo(SimpleLoc target) {
		return Math.sqrt((Math.pow((myLat - target.getLat()), 2))
				+ (Math.pow((myLon - target.getLon()), 2)));
	}
}
