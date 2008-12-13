package rapidrider;

import it.tidalwave.jsr179.ExtendedCriteria;
import ext.javax.microedition.location.Location;
import ext.javax.microedition.location.LocationListener;
import ext.javax.microedition.location.LocationProvider;

public class GPSDevice {

	// Works for a PalmOS 5 Bluetooth connection to a Holux GPSlim236.
	static final String CONNECTION_STRING = "comm:rfcm;baudrate=38400";

	// in seconds... (Tidalwave setLocationListener appears to ignore this.)
	static final int GPS_SAMPLE_RATE = 5;

	private LocationProvider locationProvider;

	private DisplayScreen screen;

	// Construct a new GPS device object...
	public GPSDevice(DisplayScreen _screen) {
		screen = _screen;
	}

	// huh?
	private LocationListener locationListener = new LocationListener() {
		// TODO: Figure out how to handle the case where the Palm looses
		// communication from the GPS device (say when the
		// device is manually turned off).
		public void providerStateChanged(LocationProvider provider, int newState) {
			if (newState != LocationProvider.AVAILABLE) {
				screen.setStatus("Unavailable...");
			} else {
				screen.setStatus("Connected...");
			}
		}

		public void locationUpdated(LocationProvider provider, Location location) {
			screen.setLocation(location);
		}
	};

	// Start connection to GPS Device
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

	// Stop connection to GPS Device
	public void stop() {
		if (locationProvider != null) {
			locationProvider.setLocationListener(null, -1, -1, -1);
			locationProvider.reset();
			locationProvider = null;
		}
	}

}
