package rapidrider;

/**
 * The SimpleLoc class represents simple locations, storing latitudes and a
 * longitudes.
 */
public class SimpleLoc {

	private double latitude, longitude;

	/**
	 * Constructs a simple location.
	 * 
	 * @param lat
	 *            The latitude.
	 * @param lon
	 *            The longitude.
	 */
	public SimpleLoc(double lat, double lon) {
		latitude = lat;
		longitude = lon;
	}

	/**
	 * Gets the latitude.
	 * 
	 * @return double
	 */
	public double getLat() {
		return latitude;
	}

	/**
	 * Gets the longitude.
	 * 
	 * @return double
	 */
	public double getLon() {
		return longitude;
	}

	/**
	 * Sets the latitude.
	 * 
	 * @param lat
	 *            The new latitude.
	 */
	public void setLat(double lat) {
		latitude = lat;
	}

	/**
	 * Sets the longitude.
	 * 
	 * @param lon
	 *            The new longitude.
	 */
	public void setLon(double lon) {
		longitude = lon;
	}

	/**
	 * Calculates the distance in degrees from this location to the target
	 * location.
	 * 
	 * @param target
	 *            Another SimpleLoc
	 * 
	 * @return double
	 */
	public double distanceTo(SimpleLoc target) {
		double latitudeDifference = latitude - target.getLat();
		double longitudeDifference = longitude - target.getLon();
		return Math.sqrt(latitudeDifference * latitudeDifference
				+ longitudeDifference * longitudeDifference);
	}

}
