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

	// I swear Brett and I already fixed this...
	// That's the beauty of having the same class in two different projects...
	// TODO test.
	public double distanceTo(SimpleLoc tar) {
		double latitudeDifference = latitude - tar.getLat();
		double longitudeDifference = longitude - tar.getLon();
		return Math.sqrt(latitudeDifference * latitudeDifference
				+ longitudeDifference * longitudeDifference);
	}

}
