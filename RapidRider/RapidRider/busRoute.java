package RapidRider;
import java.util.Vector;

//import java.util.Vector;

public class busRoute {
	
	private String routeName;
	private BusStop[] stops = new BusStop[4];
	private Vector myVec;

	public busRoute() {
		
	}
	
	public busRoute(String name) {
		routeName = name;
		myVec = new Vector();
	}
	
	public int routeLength() {
		return myVec.size();
	}
	
	public String listStops() {
		//String temp = stops[0] + "\n" + stops[1] + "\n" + stops[2];
		String temp = "";
		for( int i = 0; i < myVec.size(); i++ ) {
			temp += ( (BusStop) myVec.elementAt(i) ).getName() + "\n";
		}
		return temp;
	}
	
	public void addStop(BusStop stop) {
		myVec.addElement(stop);
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
		return (BusStop) myVec.elementAt(i); //stops[i];
	}
	
}
