package rapidrider;

public class BusStop {

	private SimpleLoc myLoc;
	private String id, latitude, longitude;
	private String myName;

	// private long[] stopTimes;
	public BusStop() {

	}
	
	public BusStop(SimpleLoc loc, String name) {
		myLoc = loc;
		myName = name;
		// stopTimes = new long[10];
	}

	public SimpleLoc getLoc() {
		return myLoc;
	}
	
	public void setId(String _id) {
		id = _id;
	}

	public String getId() {
		return id;
	}
	
	public void setName(String _name) {
		myName = _name;
	}

	public String getName() {
		return myName;
	}

	public void setLatitude(String _latitude) {
		myLoc.setLat(Integer.parseInt(_latitude));
	}

	public String getLatitude() {
		return String.valueOf(myLoc.getLat());
	}
	public void setLongitude(String _longitude) {
		myLoc.setLon(Integer.parseInt(_longitude));
	}

	public String getLongitude() {
		return String.valueOf(myLoc.getLon());
	}
	




}
