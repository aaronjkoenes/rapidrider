package rapidrider;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import ext.javax.microedition.location.Location;

// The main screen.
// Should this be renamed?  I think so..
public class AppController extends Form implements Runnable {

	private static final int DELAY = 10000; // Ten second delay
	private boolean running;

	// I removed the location and time String items.
	private StringItem statusItem, resultItem;
	private String status;
	private TextField destinationAddress;
	private Location location;
	private SimpleLoc currentLoc;

	public AppController() {
		super("Rapid Rider");
		running = false;
		status = "";
		destinationAddress = new TextField("Destination: ", "", 50,
				TextField.ANY);
		statusItem = new StringItem("Status: ", "");
		resultItem = new StringItem("Result: ", "");

		// I presume this represents Calvin College.
		// TODO Is that correct?
		// And.. maybe this should be connected to the actual current
		// location from the GPS
		currentLoc = new SimpleLoc(42.927, -85.5903);

		append(statusItem);
		append(destinationAddress);
		// Rename: ?
		append(resultItem);
	}

	// This is used by the GPS device.
	public synchronized void setStatus(String s) {
		status = s;
	}

	// This is used by the GPS device.
	public synchronized void setLocation(Location l) {
		location = l;
	}

	// Update the display... and what?
	// Question: when is this called?
	// Answer: Constantly, with a delay of DELAY between the calls.
	private void updateDisplay() {
		if (location != null) {
			currentLoc.setLat(location.getQualifiedCoordinates().getLatitude());
			currentLoc
					.setLon(location.getQualifiedCoordinates().getLongitude());
			// locationString.setText(currentLoc.printLoc());
			// currentDate = new Date(location.getTimestamp());
			// timeString.setText("(" + currentDate.toString() + ")");
		}
		statusItem.setText(status);
	}

	// Creates a new thread and starts the run() method of this class in that
	// thread.
	public void start() {
		running = true;
		Thread t = new Thread(this);
		t.start(); // Automatically calls run().
	}

	// Stop the screen?
	public void stop() {
		running = false;
	}

	// Get the address that the user wants to destine to.
	public String getDestinationAddress() {
		return destinationAddress.getString();
	}

	// Get the current location of the GPS.
	// TODO Really? The AppController / Screen thing knows this?
	// Should this be synchronized?
	public SimpleLoc getCurrentLocation() {
		return currentLoc;
	}

	// Show the directions on the screen.
	// Should this be synchronized?
	// I think YES TODO
	public void setDirections(String directions) {
		resultItem.setText(directions);
	}

	// Run like the wind!
	// TODO Actually, a nice description here would be really nice!
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
