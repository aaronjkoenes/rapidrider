package rapidrider;

import java.util.Date;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import ext.javax.microedition.location.Location;

public class AppController extends Form implements Runnable {

	// delay in milliseconds
	private static final int DELAY = 10000;  // 1 sec
	private boolean running;
	private StringItem locationString, timeString, statusString, allStops, nearestToDest;
	private TextField destinationAddress;
	String URL = "";
	int targetLat = 0, targetLon = 0;
	private Location location;
	private String status;
	private SimpleLoc currentLoc;
	private Date currentDate;
	private BusRoute route;

	public AppController() {
		super("Rapid Rider");
		running = false;
		status = "";
		locationString = new StringItem("Location: ", "");
		destinationAddress = new TextField("Destination: ", "", 50, TextField.ANY);
		timeString = new StringItem("Time: ", "");
		statusString = new StringItem("Status: ", "");
		allStops = new StringItem("Nearest Location: ", "");
		nearestToDest = new StringItem("Nearest to Destination: ", "");

		route = new BusRoute("Testing Route");

		currentLoc = new SimpleLoc(42.92780267230398, -85.58997631072998);

		append(locationString);
		append(timeString);
		append(statusString);
		
//		append(nearestToDest);
		append(destinationAddress);
		append(allStops);
	}

	public BusRoute getRoute () {
		return route;
	}
	
	public synchronized void setStatus(String s) {
		status = s;
	}

	public synchronized void setLocation(Location l) {
		location = l;
	}

	private void updateDisplay() {
		if (location != null) {
			 currentLoc.setLat(location.getQualifiedCoordinates().getLatitude());
			 currentLoc.setLon(location.getQualifiedCoordinates().getLongitude());
			locationString.setText(currentLoc.printLoc());
			currentDate = new Date(location.getTimestamp());
			timeString.setText("(" + currentDate.toString() + ")");
		}

		statusString.setText(status);
	}

	// Creates a new thread and starts the run() method of this class in that
	// thread.
	public void start() {
		running = true;
		Thread t = new Thread(this);
		t.start(); // Automatically calls run().
	}

	public void stop() {
		running = false;
	}
	
	public String getDestinationAddress() {
		return destinationAddress.getString();
	}

	public SimpleLoc getCurrentLocation() {
		return currentLoc;
	}
	
	public void setNearestLoc(String s) {
		allStops.setText(s);
	}
	
	public void setNearestDestLoc(String s)  {
		nearestToDest.setText(s);
	}
	
	public void setStopList(String s) {
		allStops.setText(s);
	}
	
	/*
	public void findNearest() {
		int nearestLocIndex = 0;
		double distance = 0.0;
		double shortest = -1.0;
		System.out.println(route.listStops());
		int length = route.routeLength();
		for (int i = 0; i < length; i++) {
			distance = currentLoc.distanceTo(new SimpleLoc(route.getstop(i).getLatitude(),route.getstop(i).getLongitude()));
			if (shortest > distance || shortest < 0) {
				shortest = distance;
				nearestLocIndex = i;
			}
		}
		nearestLocation.setText(route.getstop(nearestLocIndex).getName());
	}
	
	public void findNearest(SimpleLoc l) {
		int nearestLocIndex = 0;
		double distance = 0.0;
		double shortest = -1.0;
		System.out.println(route.listStops());
		int length = route.routeLength();
		for (int i = 0; i < length; i++) {
			double locationLat = route.getstop(i).getLatitude();
			double locationLon = route.getstop(i).getLongitude();
			SimpleLoc location = new SimpleLoc(locationLat, locationLon);
			distance = l.distanceTo(location);
			System.out.print("Distance found: " + distance);
			if (shortest > distance || shortest < 0) {
				System.out.println(".  Shorter.  Setting to " + route.getstop(i).getName());
				shortest = distance;
				nearestLocIndex = i;
			} else {
				System.out.println();
			}
		}
		nearestToDest.setText(route.getstop(nearestLocIndex).getName());
	}
	*/
	public void run() {
		while (running) {
			synchronized (this) {
				updateDisplay();
			}
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
				// If my sleep is interrupted, just continue running.
			}
		}
	}
}
