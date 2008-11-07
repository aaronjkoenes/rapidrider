package RapidRider;


public class BusStop {

	private simpleLoc myLoc;
	private String myName;

//	private long[] stopTimes;
	
	public BusStop() {
		
	}
	
	public BusStop(simpleLoc loc, String name) {
		myLoc = loc; 
		myName = name;

//		stopTimes = new long[10];
	}
	
	public String getName() {
		return myName;
	}
	
	public simpleLoc getLoc() {
		return myLoc;
	}
	
	
}
