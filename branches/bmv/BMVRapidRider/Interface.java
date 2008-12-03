import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import ext.javax.microedition.location.Location;

public class Interface extends Form implements Runnable {

	private static final int DELAY = 1000; // in milliseconds

	private boolean running;

	private StringItem locationString, timeString, statusString;
	
	private TextField addressField;

	private Location location;

	private String status;

	public Interface() {
		super("RapidRider");
		running = false;
		status = "";
		locationString = new StringItem("Location: ", "");
		timeString = new StringItem("Time: ", "");
		statusString = new StringItem("Status: ", "");
		addressField = new TextField("Dest. Address", "", 50, TextField.ANY);
		append(addressField);
		append(locationString);
		append(timeString);
		append(statusString);
	}

	public synchronized void setStatus(String s) {
		status = s;
	}

	public synchronized void setLocation(Location l) {
		location = l;
	}

	private void updateDisplay() {
		if (location != null) {
			locationString.setText("(" + location.getQualifiedCoordinates().getLatitude() + ", "
					+ location.getQualifiedCoordinates().getLongitude() + ", "
					+ location.getQualifiedCoordinates().getAltitude() + ")");
			timeString.setText("(" + location.getTimestamp() + ")");
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
