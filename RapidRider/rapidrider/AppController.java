package rapidrider;

import java.util.Date;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import ext.javax.microedition.location.Location;

public class AppController extends Form implements Runnable {

	// in milliseconds !!! NOW 10 Sec, default is 1 !!!
	private static final int DELAY = 10000;

	private boolean running;

	private StringItem locationString, /* courseString, speedString, */
	timeString, statusString, nearestLocation;

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
		// courseString = new StringItem("Course: ", "");
		// speedString = new StringItem("Speed: ", "");
		timeString = new StringItem("Time: ", "");
		statusString = new StringItem("Status: ", "");
		nearestLocation = new StringItem("Nearest Location: ", "");

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
		route = new BusRoute("some route");
		route.addStop(new BusStop(new SimpleLoc(25, 50), "a stop"));

		// lets keep CurrentLoc, don't initialize it on the Palm
		currentLoc = new SimpleLoc(0, 0);

		append(locationString);
		// append(courseString);
		// append(speedString);
		append(timeString);
		append(statusString);
		append(nearestLocation);
	}

	public synchronized void setStatus(String s) {
		status = s;
	}

	public synchronized void setLocation(Location l) {
		location = l;
	}

	private void updateDisplay() {
		if (location != null) {
			// currentLoc.setLat(location.getQualifiedCoordinates().getLatitude());
			// // Disabled for Emulator
			// currentLoc.setLon(location.getQualifiedCoordinates().getLongitude());
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

	public void findNearest() {
		System.out.println("\nStops:\n" + route.listStops());
		int nearestLoc = 0;
		double distance = 0;
		double shortest = -1;
		for (int i = 0; i < route.routeLength(); i++) {
			SimpleLoc location = route.getstop(i).getLoc();
			distance = currentLoc.distanceTo(location);
			if (shortest > distance || shortest < 0) {
				shortest = distance;
				nearestLoc = i;
			}
		}
		nearestLocation.setText(route.getstop(nearestLoc).getName());
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
