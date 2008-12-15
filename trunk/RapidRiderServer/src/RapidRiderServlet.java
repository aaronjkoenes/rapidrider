





import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




/**
 * The RapidRiderServlet class is a JBoss servlet.
 * 
 * @web.servlet name="RapidRider" display-name="Name for RapidRider"
 *              description="Description for RapidRider"
 * @web.servlet-mapping url-pattern="/RapidRider"
 * @web.servlet-init-param name="A parameter" value="A value"
 */
public class RapidRiderServlet extends HttpServlet {
	static final long serialVersionUID = 1;

	// Holds a database connection
	Connection conn;

	// Holds an SQL statement
	Statement stmt;

	private double targetLat = 0.0, targetLon = 0.0;
	private URL target;
	private BusRoute route = new BusRoute("test Route");
	private SimpleLoc currentLoc;

	/**
	 * Handles an HTTP GET request. This method expects that the request
	 * contains an address, a latitude, and a longitude.
	 * 
	 * @param req
	 *            The request object.
	 * @param resp
	 *            The response object. The response is written to in a way
	 *            similar to how standard output is written to.
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println(req.getQueryString());

		PrintWriter out = resp.getWriter();
		out.println("<?xml version=\"1.0\"?>");
		out.println("<rapidrider>");

		String destinationAddress = req.getParameter("address");
		double currentLat = Double.parseDouble(req.getParameter("lat"));
		double currentLon = Double.parseDouble(req.getParameter("lon"));
		currentLoc = new SimpleLoc(currentLat, currentLon);

		getDestinationCoords(destinationAddress);

		try {
			Class.forName("org.postgresql.Driver");

			conn = DriverManager.getConnection("jdbc:postgresql:rapidrider",
					"postgres", "bjarne");
			stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery("SELECT * FROM bus_stops");

			// TODO Question: Is it possible for res to be null?
			if (res != null) {
				while (res.next()) {
					String stopName = res.getString(2);
					String revisedStopName = replaceEscapeCharacters(stopName);
					double longitude = Double.parseDouble(res.getString(4));
					double latitude = Double.parseDouble(res.getString(5));

					// I don't really understand what temporaryStop is or why it
					// is added to the route. Also, what is route?
					BusStop temporaryStop = new BusStop(new SimpleLoc(latitude,
							longitude), revisedStopName);
					route.addStop(temporaryStop);

					// TODO Do we still want any of this?
					// out.println("<busstop>");
					// // temp2 = route.getstop(route.routeLength());
					// out.println("<stopName>" + temp.getName() + " " +
					// temp.getLatitude() + " " + temp.getLongitude() +
					// "</stopName>");
					// out.println("</busstop>");

					// String latitude = res.getString(3); String longitude =
					// res.getString(4); out.println("<busstop>");
					// out.println("<stopID>" + stopID + "</stopID>");
					// out.println("<stopName>" + revisedStopName +
					// "</stopName>");
					// out.println("<latitude>" + latitude + "</latitude>");
					// out.println("<longitude>" + longitude + "</longitude>");
					// out.println("</busstop>");

				}

				BusStop nearestStop = findNearest(currentLoc);
				out.println("<busstop>");
				out.println("<stopName>" + nearestStop.getName()
						+ "</stopName>");
				out.println("</busstop>");
				out.println("<busstop>");

				// TODO what if TinyGeoCoder hasn't returned a value by the time
				// we get to this part of the code?
				out.println("<stopName>"
						+ findNearest(new SimpleLoc(targetLat, targetLon))
								.getName() + "</stopName>");
				out.println("</busstop>");
			}
			res.close();
			stmt.close();
			conn.close();

		} catch (Exception e) {
			out.println("Error in JDBC access: " + e.getClass() + " : "
					+ e.getMessage());
			e.printStackTrace();
		}
		out.println("</rapidrider>");
	}

	/**
	 * Returns the nearest BusStop to the given location out of all of the stops
	 * in route.
	 * 
	 * @param l
	 *            The location the find the nearest bus stop to.
	 * @return BusStop
	 */
	public BusStop findNearest(SimpleLoc l) {
		int nearestLocIndex = -1;
		double distance = 0.0;
		double shortest = -1.0;
		for (int i = 0; i < route.routeLength(); i++) {
			double locationLat = route.getstop(i).getLatitude();
			double locationLon = route.getstop(i).getLongitude();
			SimpleLoc location = new SimpleLoc(locationLat, locationLon);

			// TODO Isn't this the same as the location created in the above
			// three lines?
			route.getstop(i).getLoc();

			distance = l.distanceTo(location);
			// System.out.print("Distance found: " + distance);
			if (shortest > distance || shortest < 0) {
				// System.out.println(". Shorter. Setting to " +
				// route.getstop(i).getName());
				shortest = distance;
				nearestLocIndex = i;
			} else {
				// System.out.println();
			}
		}
		return route.getstop(nearestLocIndex);
		// nearestToDest.setText(route.getstop(nearestLocIndex).getName());
	}

	/**
	 * Uses TinyGeoCoder to set the destination coordinates from a description
	 * of a place.
	 * 
	 * @param placeDescription
	 *            The String to pass to TinyGeoCoder.
	 * 
	 * @throws IOException
	 */
	// TODO Shouldn't this method return its result?
	public void getDestinationCoords(String placeDescription)
			throws IOException {
		String URLx = "http://tinygeocoder.com/create-api.php?q="
				+ replaceSpaces(placeDescription);
		System.out.println(URLx);
		target = new URL(URLx);
		InputStreamReader x = new InputStreamReader(target.openStream());
		BufferedReader in = new BufferedReader(x);
		String result = in.readLine();
		in.close();
		x.close();
		parse(result);
	}

	/**
	 * Parses the latitude and longitude out of a string from TinyGeoCoder.
	 * 
	 * @param s
	 *            The String from TinyGeoCoder
	 */
	public void parse(String s) {
		String[] s2 = s.split(",");
		targetLat = Double.parseDouble(s2[0]);
		targetLon = Double.parseDouble(s2[1]);
	}

	/**
	 * Replaces spaces in the given String with %20s.
	 * 
	 * @param s
	 *            The String to alter.
	 * 
	 * @return String
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

	/**
	 * Protects the XML by searching for ampersands and replacing them with the
	 * HTML equivalent (&amp;)
	 * 
	 * @param s
	 *            The String to modify.
	 */
	public String replaceEscapeCharacters(String stopName) {
		String revisedStopName = "";
		// TODO: Should we use a character array instead of repeatedly adding to
		// immutable string objects?
		for (int i = 0; i < stopName.length(); i++) {
			char c = stopName.charAt(i);
			// TODO: Should other characters be escaped also?
			if (c == '&') {
				revisedStopName += "&amp;";
			} else {
				revisedStopName += c;
			}
		}
		return revisedStopName;
	}
}
