package rapidrider;

import it.tidalwave.jsr179.ExtendedCriteria;
import ext.javax.microedition.location.Location;
import ext.javax.microedition.location.LocationListener;
import ext.javax.microedition.location.LocationProvider;

/**
 * The GPS class allows the midlet to communicate with the GPS device.
 */
public class GPSDevice {

	// Works for a PalmOS 5 Bluetooth connection to a Holux GPSlim236.
	static final String CONNECTION_STRING = "comm:rfcm;baudrate=38400";

	// In seconds. (Tidalwave setLocationListener appears to ignore this.)
	static final int GPS_SAMPLE_RATE = 5;

	private LocationProvider locationProvider;

	private DisplayScreen screen;

	private LocationListener locationListener = new LocationListener() {

		/**
		 * Responds to the change in state of the location provider.
		 */
		public void providerStateChanged(LocationProvider provider, int newState) {
			if (newState != LocationProvider.AVAILABLE) {
				screen.setStatus("Unavailable...");
			} else {
				screen.setStatus("Connected...");
			}
		}

		/**
		 * Updates the screen when the location is updated.
		 */
		public void locationUpdated(LocationProvider provider, Location location) {
			screen.setLocation(location);
		}
	};

	/**
	 * Constructs a new GPS device.
	 * 
	 * @param _screen
	 *            The screen that the GPS should report its updates to.
	 */
	public GPSDevice(DisplayScreen _screen) {
		screen = _screen;
	}

	/**
	 * Starts the connection to the GPS device.
	 */
	public void start() {
		if (locationProvider == null) {
			try {
				ExtendedCriteria criteria = new ExtendedCriteria();
				criteria.setConnectionObject(CONNECTION_STRING);
				locationProvider = LocationProvider.getInstance(criteria);
				locationProvider.setLocationListener(locationListener,
						GPS_SAMPLE_RATE, -1, -1);
			} catch (Throwable t) {
				screen.setStatus("Unable to connect...");
			}
		}
	}

	/**
	 * Stops the connection to the GPS device.
	 */
	public void stop() {
		if (locationProvider != null) {
			locationProvider.setLocationListener(null, -1, -1, -1);
			locationProvider.reset();
			locationProvider = null;
		}
	}

}
