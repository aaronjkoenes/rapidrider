package rapidrider;

import java.util.Vector;

public class BusRoute {
	
	private String routeName;
	
	// TODO Are both of these instance variables still necessary?
	private BusStop[] stops = new BusStop[4];
	private Vector myStops;

	public BusRoute() {
		
	}
	
	public BusRoute(String name) {
		routeName = name;
		myStops = new Vector();
	}
	
	public int routeLength() {
		return myStops.size();
	}
	
	public String listStops() {
		//String temp = stops[0] + "\n" + stops[1] + "\n" + stops[2];
		String temp = "";
		for( int i = 0; i < myStops.size(); i++ ) {
			temp += ( (BusStop) myStops.elementAt(i) ).getName() + "\n";
		}
		return temp;
	}
	
	public void addStop(BusStop stop) {
		myStops.addElement(stop);
	}
	
	public String getRouteName() {
		return routeName;
	}
	
	public void setStops( BusStop stop1, BusStop stop2, BusStop stop3) {
		stops[0] = stop1;
		stops[1] = stop2;
		stops[2] = stop3;
	}
	
	public BusStop getstop(int i) {
		return (BusStop) myStops.elementAt(i); //stops[i];
	}
	
}
