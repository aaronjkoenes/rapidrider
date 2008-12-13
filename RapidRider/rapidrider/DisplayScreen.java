package rapidrider;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import ext.javax.microedition.location.Location;

public class DisplayScreen extends Form implements Runnable {

	private static final int DELAY = 5000; // five second delay

	private boolean running;

	private StringItem statusItem, directions;

	private String status;

	private TextField destinationAddress;

	private Location location;

	private SimpleLoc currentLoc;

	public DisplayScreen() {
		super("Rapid Rider");
		running = false;
		status = "";
		destinationAddress = new TextField("Destination: ", "", 50,
				TextField.ANY);
		statusItem = new StringItem("Status: ", "");

		currentLoc = new SimpleLoc(location.getQualifiedCoordinates()
				.getLatitude(), location.getQualifiedCoordinates()
				.getLongitude());

		append(statusItem);
		append(destinationAddress);
		append(directions);
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
	// Should this be synchronized?
	public SimpleLoc getCurrentLocation() {
		return currentLoc;
	}

	// Show the directions on the screen.
	// Should this be synchronized?
	public void setDirections(String d) {
		directions.setText(d);
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
