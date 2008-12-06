package rapidrider;

//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
//import java.net.*;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import ext.javax.microedition.location.Location;

public class AppController extends Form implements Runnable {

	// in milliseconds !!! NOW 10 Sec, default is 1 !!!
	private static final int DELAY = 1000;  // 1 sec.
	private boolean running;
	private StringItem locationString, timeString, statusString, nearestLocation, nearestToDest;
	private TextField destinationAddress;
	private URL target;//http:tinygeocoder.com/create-api.php?q=" + destinationAddress.getString() + " Grand Rapids, MI";
	String URL = "";
	int targetLat = 0, targetLon = 0;
									/* courseString, speedString, */
	private Location location;
	private String status;
	private SimpleLoc currentLoc;
	// private BusStop vvclose, vclose, close, med, far;
	private Date currentDate;
	private BusRoute route;

	public AppController() {
		super("Rapid Rider");
		running = false;
		status = "";
		locationString = new StringItem("Location: ", "");
		destinationAddress = new TextField("Destination: ", "", 50, TextField.ANY);
		// courseString = new StringItem("Course: ", "");
		// speedString = new StringItem("Speed: ", "");
		timeString = new StringItem("Time: ", "");
		statusString = new StringItem("Status: ", "");
		nearestLocation = new StringItem("Nearest Location: ", "");
		nearestToDest = new StringItem("Nearest to Destination: ", "");

		// TODO: Turn these tests into unit tests.

		/*
		 * !!!!! All this below is for early testing, but does it work???
		 * currentLoc = new simpleLoc(42.93693852958363, -85.5993486738205);
		 * close = new BusStop(new SimpleLoc(42.92693852958363,
		 * -85.5893486738205), "close"); med = new BusStop(new
		 * SimpleLoc(42.95748842941581, -85.63509106636047), "medium"); far =
		 * new BusStop(new SimpleLoc(42.963218160220414, -85.66791325807571),
		 * "far");
		 * 
		 * currentLoc = new SimpleLoc(0, 0); vclose = new BusStop(new
		 * SimpleLoc(4, 9), "very close"); close = new BusStop(new SimpleLoc(5,
		 * 10), "close"); med = new BusStop(new SimpleLoc(15, 30), "medium");
		 * far = new BusStop(new SimpleLoc(25, 50), "far"); vvclose = new
		 * BusStop(new SimpleLoc(2, 2), "very, VERY close");
		 * 
		 * route = new BusRoute("route 1"); route.setStops(med, close, far);
		 * route.addStop(close); route.addStop(med); route.addStop(vclose);
		 * route.addStop(far); route.addStop(vvclose);
		 */

		// This is added to get rid of a null pointer exception.
		// Why does the controller store a single route like this?
		route = new BusRoute("Testing Route");
		//route.addStop(new BusStop(new SimpleLoc(25, 50), "a stop"));

		// lets keep CurrentLoc, don't initialize it on the Palm
		currentLoc = new SimpleLoc(42.878, -85.5962);

		append(locationString);
		// append(courseString);
		// append(speedString);
		append(timeString);
		append(statusString);
		append(nearestLocation);
		append(nearestToDest);
		append(destinationAddress);
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
			// // Disabled for Emulator
			 currentLoc.setLon(location.getQualifiedCoordinates().getLongitude());
			// // Disabled for Emulator
			locationString.setText(currentLoc.printLoc());
			currentDate = new Date(location.getTimestamp());
			timeString.setText("(" + currentDate.toString() + ")");

			/*
			 * locationString.setText("(" +
			 * location.getQualifiedCoordinates().getLatitude() + ", " +
			 * location.getQualifiedCoordinates().getLongitude() + ", " +
			 * location.getQualifiedCoordinates().getAltitude() + ")");
			 * courseString.setText("(" + location.getCourse() + ")");
			 * speedString.setText("(" + location.getSpeed() + ")");
			 */
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
	
	/*
	public void getDestinationStop() throws IOException {
		String go = destinationAddress.getString();
		URL = "create-api.php?q=" + go + "%20Grand%20Rapids,%20MI";
		target = new URL("http", "tinygeocoder.com/", replaceSpaces(URL));
		//HttpURLConnection connection = (HttpURLConnection) target.openConnection();
		//InputStreamReader x = new InputStreamReader(target.openStream());
		//BufferedReader in = new BufferedReader(x);
		//String result = in.readLine();
		//in.close();
		//x.close();
		//parse(result);
		System.out.println("Target Lat:  " + targetLat + "\nTarget Long: " + targetLon);
	}
	
	public void parse(String s ) {
		String lat = "", lon = "";
		for (int i = 0; i < 10; i++ ) {
			lat += s.charAt(i);
		}
		for( int i = 11; i < 20; i++) {
			lon += s.charAt(i);
		}
		targetLat = Integer.parseInt(lat);
		targetLon = Integer.parseInt(lon);
	}	
	*/

	
	public void findNearest() {
		
		//System.out.println("\nStops:\n" + route.listStops());
		int nearestLocIndex = 0;
		double distance = 0.0;
		double shortest = -1.0;
		System.out.println(route.listStops());
		int length = route.routeLength();
		for (int i = 0; i < length; i++) {
			SimpleLoc location = new SimpleLoc();
			location = route.getstop(i).getLoc();
			System.out.println("This far: " + i + "\n");
			distance = currentLoc.distanceTo(location);
			//System.out.println("Distance to " + route.getstop(i).getName() + ": " + distance);
			if (shortest > distance || shortest < 0) {
				shortest = distance;
				nearestLocIndex = i;
			}
		}
		nearestLocation.setText(route.getstop(nearestLocIndex).getName());
		//System.out.println("****************");
	}
	
	public void findNearest(SimpleLoc l) {
		
		//System.out.println("\nStops:\n" + route.listStops());
		int nearestLocIndex = 0;
		double distance = 0.0;
		double shortest = -1.0;
		System.out.println(route.listStops());
		int length = route.routeLength();
		for (int i = 0; i < length; i++) {
			SimpleLoc location = new SimpleLoc();
			location = route.getstop(i).getLoc();
			System.out.println("This far(2): " + i + "\n");
			distance = l.distanceTo(location);
			//System.out.println("Distance to " + route.getstop(i).getName() + ": " + distance);
			if (shortest > distance || shortest < 0) {
				shortest = distance;
				nearestLocIndex = i;
			}
		}
		nearestToDest.setText(route.getstop(nearestLocIndex).getName());
		//System.out.println("****************");
	}
	
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
