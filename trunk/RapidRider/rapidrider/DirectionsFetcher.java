package rapidrider;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class DirectionsFetcher implements Runnable {

	private static String URL = "http://153.106.117.64:8080/rapidRiderServlet/RapidRider";
	// private static String URL =
	// "http://localhost:8080/rapidRiderServlet/RapidRider";

	private String targetURL;
	private AppController screen;

	// Construct a directions fetcher.
	// It uses the given screen to get the destination and current location
	// when it is time to fetch directions.
	public DirectionsFetcher(AppController _screen) {
		screen = _screen;
	}

	// Start a thread to fetch directions from the starting location to the
	// ending location.
	public void fetchDirections() {
		// These were going to be parameters, but now they don't have to be.
		String destination = screen.getDestinationAddress();
		SimpleLoc currentLocation = screen.getCurrentLocation();

		// What a long and awkward line..
		// ShoULd this be done in the thread?
		// It could be.
		targetURL = URL
				+ "?address="
				+ replaceSpaces(destination + " Grand Rapids, MI" + "&lat="
						+ currentLocation.getLat() + "&lon="
						+ currentLocation.getLon());
		System.out.println("URL:  " + targetURL);

		// Threads can't really be recycled very easily as far as I understand.
		// So, we make a new one each time we want to fetch some server stuff.
		// This basically just calls run() in a new thread.
		new Thread(this).start();
	}

	// Start the thread.
	public void run() {
		requestDirectionsFromServer();
	}

	private void requestDirectionsFromServer() {
		try {
			HttpConnection conn = (HttpConnection) Connector.open(targetURL);

			KXmlParser parser = new KXmlParser();

			Directions directions = new Directions();

			// Get the result and check that it starts with the right tag.
			parser.setInput(new InputStreamReader(conn.openInputStream()));
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, "rapidrider");

			// Is this really all that is supposed to be in this while loop?
			// I need my brackets.
			// What does it do?
			while (parser.nextTag() != XmlPullParser.END_TAG) {
				// The next tag better be a bus stop tag.
				directions.addStop(parseBusStopTag(parser));
			}

			parser.require(XmlPullParser.END_TAG, null, "rapidrider");
			parser.next();
			parser.require(XmlPullParser.END_DOCUMENT, null, null);

			screen.setDirections(directions.toString());
		} catch (Exception e) {
			e.printStackTrace();
			// OR, it could call something like screen.setError().
			screen.setDirections("Error! " + e.toString());
		}
	}

	// This should be private, but it should also be tested.
	// Expects a busstop tag to be next up in the given parser.
	public BusStop parseBusStopTag(KXmlParser parser) throws IOException,
			XmlPullParserException {

		// explain this TODO
		parser.require(XmlPullParser.START_TAG, null, "busstop");

		String busStopName = "";

		// This shouldn't be a loop, should it?
		// Does this really have to be here at all?
		while (parser.nextTag() != XmlPullParser.END_TAG) {
			parser.require(XmlPullParser.START_TAG, null, null);
			// What is the difference between getName() and nextText() ?
			// Notice that KXML parser basically has no documentation.
			String tagName = parser.getName();
			busStopName = parser.nextText();
			parser.require(XmlPullParser.END_TAG, null, tagName);
		}
		parser.require(XmlPullParser.END_TAG, null, "busstop");

		// It would be nice to have a location to pass along too...
		// And maybe a bus route number...
		// And maybe a time too...
		// Really. We could do that.
		return new BusStop(busStopName);
	}

	// Replace the spaces in a string with %20s.
	// I think this could be simplified quite a bit.
	// TODO test.
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
