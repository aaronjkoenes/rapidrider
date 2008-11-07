package RapidRiderSVN;
// Sample code for CS 262
// Keith Vander Linden, Calvin College
// Fall 2006

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
//import java.lang.*;
import java.util.Date;

import ext.javax.microedition.location.Location;

public class GameBoard extends Form implements Runnable {

	private static final int DELAY = 1000; // in milliseconds
	private boolean running;
	private StringItem locationString, /*courseString, speedString,*/ timeString, statusString, nearestLocation;
	private Location location;
	private String status;
	private simpleLoc currentLoc;
	private BusStop vvclose, vclose, close, med, far;
	private Date currentDate;
	private busRoute route;

	public GameBoard() {
		super("Rapid Rider");
		running = false;
		status = "";
		locationString = new StringItem("Location: ", "");
//		courseString = new StringItem("Course: ", "");
//		speedString = new StringItem("Speed: ", "");
		timeString = new StringItem("Time: ", "");
		statusString = new StringItem("Status: ", "");
		nearestLocation = new StringItem("Nearest Location: ", "");
		/*
		currentLoc = new simpleLoc(42.93693852958363, -85.5993486738205);
		close = new BusStop(new simpleLoc(42.92693852958363, -85.5893486738205), "close");
		med = new BusStop(new simpleLoc(42.95748842941581, -85.63509106636047), "medium");
		far = new BusStop(new simpleLoc(42.963218160220414, -85.66791325807571), "far");
		*/
		currentLoc = new simpleLoc(0, 0);
		vclose = new BusStop(new simpleLoc(4, 9), "very close");
		close = new BusStop(new simpleLoc(5, 10), "close");
		med = new BusStop(new simpleLoc(15, 30), "medium");
		far = new BusStop(new simpleLoc(25, 50), "far");
		vvclose = new BusStop(new simpleLoc(2, 2), "very, VERY close");
		
		route = new busRoute("route 1");
		route.setStops(med, close, far);
		route.addStop(close);
		route.addStop(med);
		route.addStop(vclose);
		route.addStop(far);
		route.addStop(vvclose);
	
		append(locationString);
//		append(courseString);
//		append(speedString);
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
//			currentLoc.setLat(location.getQualifiedCoordinates().getLatitude());
//			currentLoc.setLon(location.getQualifiedCoordinates().getLongitude());
			locationString.setText(currentLoc.printLoc());
			currentDate = new Date(location.getTimestamp());
			timeString.setText("(" + currentDate.toString() + ")");
			
/*			locationString.setText("(" + location.getQualifiedCoordinates().getLatitude() + ", "
					+ location.getQualifiedCoordinates().getLongitude() + ", "
					+ location.getQualifiedCoordinates().getAltitude()  + ")");
			courseString.setText("(" + location.getCourse() + ")");
			speedString.setText("(" + location.getSpeed() + ")");
*/			
		}
		
		statusString.setText(status);
	}

	public void start() {
		running = true;
		Thread t = new Thread(this);
		t.start(); // Automatically calls run().
	}

	public void stop() {
		running = false;
	}
	
	
	public void findNearest() {
		System.out.println("\nStops:\n   " + route.listStops());
		// *** Will need to be called on all routes
		int nearestLoc = 0;
		float distance = 0;
		float shortest = -1;
		for(int i = 0; i < route.routeLength() ; i++ ) {
			simpleLoc location = route.getstop(i).getLoc();
			distance = currentLoc.DistanceTo(location);
			if( shortest > distance || shortest < 0) {
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
//				 If my sleep is interrupted, just continue running.
			}
		}
	}
}
