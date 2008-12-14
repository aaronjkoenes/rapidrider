package rapidrider;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import ext.javax.microedition.location.Location;

/**
 * The DisplayScreen class is the main Form for the RapidRider midlet.
 */
public class DisplayScreen extends Form implements Runnable {

	private static final int DELAY = 5000; // Five second delay

	private boolean running;

	private StringItem statusItem, directions;

	private String status;

	private TextField destinationAddress;

	private Location location;

	private SimpleLoc currentLoc;

	/**
	 * Constructs a display screen.
	 */
	public DisplayScreen() {
		super("Rapid Rider");
		running = false;
		status = "";
		destinationAddress = new TextField("Destination: ", "", 50,
				TextField.ANY);
		statusItem = new StringItem("Status: ", "");
		// Initialize the current location to be Calvin College. If the GPS
		// device is connected, the location will be updated.
		currentLoc = new SimpleLoc(42.927, -85.5903);
		directions = new StringItem("Directions: ", "");
		append(statusItem);
		append(destinationAddress);
		append(directions);
	}

	/**
	 * Sets the status of the midlet for the user to see once the display is
	 * updated
	 * 
	 * This is used by the GPS device.
	 * 
	 * @param s
	 *            The status
	 */
	public synchronized void setStatus(String s) {
		status = s;
	}

	/**
	 * Sets the location for the user to see once the display is updated
	 * 
	 * This is used by the GPS device.
	 * 
	 * @param l
	 *            The location
	 */
	public synchronized void setLocation(Location l) {
		location = l;
	}

	/**
	 * Updates the display of the midlet application.
	 * 
	 * This method is constantly called with a delay of DELAY between each call.
	 */
	private void updateDisplay() {
		if (location != null) {
			currentLoc.setLat(location.getQualifiedCoordinates().getLatitude());
			currentLoc
					.setLon(location.getQualifiedCoordinates().getLongitude());
		}
		statusItem.setText(status);
	}

	/**
	 * Creates a new thread and starts the run() method of this class in that
	 * thread.
	 */
	public void start() {
		running = true;
		Thread t = new Thread(this);
		t.start(); // Automatically calls run().
	}

	/**
	 * Stops the screen from updating itself.
	 */
	public void stop() {
		running = false;
	}

	/**
	 * Returns the address that the user has entered.
	 * 
	 * @return String
	 */
	public String getDestinationAddress() {
		return destinationAddress.getString();
	}

	/**
	 * Returns the current location of the GPS device.
	 * 
	 * @return SimpleLoc
	 */
	public SimpleLoc getCurrentLocation() {
		return currentLoc;
	}

	/**
	 * Set the directions that show up on the screen.
	 * 
	 * @param d
	 *            A String representation of the directions.
	 */
	public void setDirections(String d) {
		directions.setText(d);
	}

	/**
	 * In a thread, continually updates the display, with a delay of DELAY
	 * between each update.
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
