package rapidrider;

public class BusStop {

	private SimpleLoc myLoc;
	private String id;
	private String myName;

	public BusStop() {
		myLoc = new SimpleLoc();
	}
	
	public BusStop(SimpleLoc loc, String name) {
		myLoc = loc;
		myName = name;
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
		myLoc.setLat(Double.parseDouble(_latitude));
	}

	public String getLatitude() {
		return String.valueOf(myLoc.getLat());
	}
	
	public void setLongitude(String _longitude) {
		myLoc.setLon(Double.parseDouble(_longitude));
	}

	public String getLongitude() {
		return String.valueOf(myLoc.getLon());
	}
	
	public String printLoc() {
		return myLoc.printLoc();
	}
	
	public String toString() {
		return myName + " " + id + myLoc.toString();
	}
}
