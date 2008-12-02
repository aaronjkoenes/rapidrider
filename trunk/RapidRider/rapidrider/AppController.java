package rapidrider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import ext.javax.microedition.location.Location;

public class AppController extends Form implements Runnable {

	// in milliseconds !!! NOW 10 Sec, default is 1 !!!
	private static final int DELAY = 1000;  // 1 sec.
	private boolean running;
	private StringItem locationString, timeString, statusString, nearestLocation;

	private TextField destinationAddress;
									/* courseString, speedString, */
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
		route = new BusRoute("Testing Route");
		//route.addStop(new BusStop(new SimpleLoc(25, 50), "a stop"));

		// lets keep CurrentLoc, don't initialize it on the Palm
		currentLoc = new SimpleLoc(0, 0);

		append(locationString);
		// append(courseString);
		// append(speedString);
		append(timeString);
		append(statusString);
		append(nearestLocation);
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
	
	//*
	public void getDestinationStop() throws IOException {
		InputStream is = null;
		OutputStream os = null;
		String dest = "http://tinygeocoder.com/create-api.php?q=" + 
			destinationAddress.getString() + " Grand Rapids, MI";
		StreamConnection c = null;
		InputStream s = null;
		
		try {
			c = (StreamConnection) Connector.open(dest);
			s = c.openInputStream();
			int ch;
			while((ch = s.read()) != -1 ) {
				System.out.println(ch);
			}
		} finally {
			if (s != null)
				s.close();
			if (c != null) 
				c.close();
		}
	}
		
		/*
			try {
				String site = "http://tinygeocoder.com/create-api.php?q=" + destinationAddress + " Grand Rapids, MI";
				HttpConnection connection = (HttpConnection) Connector.open(site);
				connection.setRequestMethod("GET");
				///connection.setRequestProperty("User-Agent", "Profile/MIDP-2.0 Configuration/CLDC-1.0");
				//connection.setRequestProperty("Content-Language", "en-US");
				//os = connection.openOutputStream();
				
				rc = connection.getResponseCode();
				/*if( rc != HttpConnection.HTTP_OK) {
					throw new IOException("HTTP failed.  Response code " + rc);
				}
				
				is = connection.openInputStream();
				String type = connection.getType();
				//processType(type);
				int len = (int) connection.getLength();
				if( len > 0 ) {
					int actual = 0;
					int read = 0;
					byte[] data = new byte[len];
					while ((read != len) && (actual != -1)) {
						actual = is.read(data, read, len - read);
						read += actual;
					}
				} else {
					int ch;
					while ((ch = is.read()) != -1 ) {
						// ...
					}
				}
	
				/*BufferedReader reader = new BufferedReader( new InputStreamReader(connection.getInputStream()));
				String line = null;
				while((line = reader.readLine()) != null ) {
					System.out.println(line);
				}
				KXmlParser parser = new KXmlParser();
				parser.setInput(new InputStreamReader(connection.openInputStream()));
				parser.nextTag();
				parser.require(XmlPullParser.START_TAG, null, "rapidrider");
				while (parser.nextTag() != XmlPullParser.END_TAG)
					readXMLData(parser);
				parser.require(XmlPullParser.END_TAG, null, "rapidrider");
				parser.next();
				parser.require(XmlPullParser.END_DOCUMENT, null, null);
				
				
			} catch (Exception e) {
				e.printStackTrace();
				//resultItem.setLabel("Error:");
				//resultItem.setText(e.toString());
			}
//		}*/
	

	
	public void findNearest() {
		
		//System.out.println("\nStops:\n" + route.listStops());
		int nearestLoc = 0;
		double distance = 0;
		double shortest = -1;
		for (int i = 0; i < route.routeLength(); i+=4) {
			SimpleLoc location = route.getstop(i).getLoc();
			distance = currentLoc.distanceTo(location);
			System.out.println("Distance to " + route.getstop(i).getName() + ": " + distance);
			if (shortest > distance || shortest < 0) {
				shortest = distance;
				nearestLoc = i;
			}
		}
		nearestLocation.setText(route.getstop(nearestLoc).getName());
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
