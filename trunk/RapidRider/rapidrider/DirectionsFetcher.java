package rapidrider;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * The DirectionsFetcher class fetches directions from the RapidRider server.
 * 
 * @author gjc3
 */
public class DirectionsFetcher implements Runnable {

	private static String URL = "http://153.106.117.64:8080/rapidRiderServlet/RapidRider";

	// private static String URL =
	// "http://localhost:8080/rapidRiderServlet/RapidRider";

	private String targetURL;

	private DisplayScreen screen;

	/**
	 * Constructs a directions fetcher. It uses the given screen to get the
	 * destination and current location when it is time to fetch directions.
	 */
	public DirectionsFetcher(DisplayScreen _screen) {
		screen = _screen;
	}

	/**
	 * Starts a thread to fetch directions from the starting location to the
	 * ending location.
	 */
	public void fetchDirections() {
		String destination = screen.getDestinationAddress();
		SimpleLoc currentLocation = screen.getCurrentLocation();

		targetURL = URL
				+ "?address="
				+ replaceSpaces(destination + " Grand Rapids, MI" + "&lat="
						+ currentLocation.getLat() + "&lon="
						+ currentLocation.getLon());
		System.out.println("URL:  " + targetURL);

		// Create a new thread and request directions from the server with it.
		new Thread(this).start();
	}

	/**
	 * Starts the thread that will request directions from the server.
	 */
	public void run() {
		requestDirectionsFromServer();
	}

	/**
	 * Sends an HTTP request to the server and parse the result.
	 */
	private void requestDirectionsFromServer() {
		try {
			// Create an HTTP connection to the target URL and start parsing the
			// response.
			HttpConnection conn = (HttpConnection) Connector.open(targetURL);
			KXmlParser parser = new KXmlParser();
			Directions directions = new Directions();

			// Get the result and check that it starts with the right tag.
			parser.setInput(new InputStreamReader(conn.openInputStream()));
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, "rapidrider");

			// Add each bus stop in the XML response to the directions.
			while (parser.nextTag() != XmlPullParser.END_TAG) {
				directions.addStop(parseBusStopTag(parser));
			}

			parser.require(XmlPullParser.END_TAG, null, "rapidrider");
			parser.next();
			parser.require(XmlPullParser.END_DOCUMENT, null, null);

			// Update the screen with the received directions.
			screen.setDirections(directions.toString());
		} catch (Exception e) {
			e.printStackTrace();
			// Tell the user that an error has occured.
			screen.setDirections("Error: " + e.toString());
		}
	}

	/**
	 * Extracts a BusStop object from the XML data that is being parsed.
	 */
	public BusStop parseBusStopTag(KXmlParser parser) throws IOException,
			XmlPullParserException {

		// The next tag that the parser sees must be a <busstop> tag.
		parser.require(XmlPullParser.START_TAG, null, "busstop");

		String busStopName = "";

		// Given well formed XML, this loop will be executed exactly once.
		while (parser.nextTag() != XmlPullParser.END_TAG) {
			parser.require(XmlPullParser.START_TAG, null, null);
			String tagName = parser.getName();
			busStopName = parser.nextText();
			parser.require(XmlPullParser.END_TAG, null, tagName);
		}
		parser.require(XmlPullParser.END_TAG, null, "busstop");

		// Creat a BusStop object from the name in the XML.
		return new BusStop(busStopName);
	}

	/**
	 * Replaces the spaces in a string with %20s.
	 */
	public String replaceSpaces(String s) {
		String Result = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ' ') {
				Result += "%20";
			} else {
				Result += s.charAt(i);
			}
		}
		return Result;
	}
}
