package rapidrider;

// This class represents a bus stop.
public class BusStop {

	private SimpleLoc location;
	private String name;

	// TODO Should this also know its bus route? And a time??? Or should that
	// be another class?

	// But, for now, since we are on the client side. I suppose I will allow
	// a constructor that only takes a name.
	public BusStop(String _name) {
		// Let's explicitely leave location null. Maybe bad idea.
		location = null;
		name = _name;
	}

	// Create a new bus stop given a location and a name.
	public BusStop(SimpleLoc loc, String _name) {
		location = loc;
		name = _name;
	}

	// Too many accessors?
	public SimpleLoc getLoc() {
		return location;
	}

	public double getLatitude() {
		return location.getLat();
	}

	public double getLongitude() {
		return location.getLon();
	}

	// This isn't used right now.
	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}
}
