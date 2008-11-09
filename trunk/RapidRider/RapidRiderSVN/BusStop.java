package RapidRiderSVN;

public class BusStop {

	private SimpleLoc myLoc;

	private String myName;

	// private long[] stopTimes;

	public BusStop() {

	}

	public BusStop(SimpleLoc loc, String name) {
		myLoc = loc;
		myName = name;
		// stopTimes = new long[10];
	}

	public String getName() {
		return myName;
	}

	public SimpleLoc getLoc() {
		return myLoc;
	}

}
