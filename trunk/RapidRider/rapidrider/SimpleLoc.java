package rapidrider;

public class SimpleLoc {
	
	private float myLat;

	private float myLon;

	public SimpleLoc() {
		myLat = Float.NaN;
		myLon = Float.NaN;
	}

	public SimpleLoc(double lat, double lon) {
		myLat = (float) lat;
		myLon = (float) lon;
	}

	public float getLat() {
		return myLat;
	}

	public float getLon() {
		return myLon;
	}

	public void setLat(double lat) {
		myLat = (float) lat;
	}

	public void setLon(double lon) {
		myLon = (float) lon;
	}

	public String printLoc() {
		return String.valueOf(myLat) + ", " + String.valueOf(myLon);
	}

	public float DistanceTo(SimpleLoc tar) {
		double dist = Math.sqrt( 
			 ( (Math.abs(myLat) - Math.abs(tar.getLat()) ) * ( Math.abs(myLat) - Math.abs(tar.getLat()) ) ) +
			 ( (Math.abs(myLon) - Math.abs(tar.getLon()) ) * ( Math.abs(myLon) - Math.abs(tar.getLon()) ) )
			 );
		return (float) dist;
	}
}
