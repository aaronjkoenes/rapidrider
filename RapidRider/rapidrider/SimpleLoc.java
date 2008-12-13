package rapidrider;

// Represents a simple location, storing a latitude and a longitude.
public class SimpleLoc {

	private double latitude, longitude;

	public SimpleLoc(double lat, double lon) {
		latitude = lat;
		longitude = lon;
	}

	public double getLat() {
		return latitude;
	}

	public double getLon() {
		return longitude;
	}

	public void setLat(double lat) {
		latitude = lat;
	}

	public void setLon(double lon) {
		longitude = lon;
	}

	public String printLoc() {
		return latitude + ", " + longitude;
	}

	// Calculates distance (in degrees) from this to target.
	public double distanceTo(SimpleLoc target) {
		double latitudeDifference = latitude - target.getLat();
		double longitudeDifference = longitude - target.getLon();
		return Math.sqrt(latitudeDifference * latitudeDifference
				+ longitudeDifference * longitudeDifference);
	}

}
